# 数据权限系统优化设计

> 日期：2026-06-01
> 状态：待实施
> 方案：方案 B（深度优化）

## 1. 背景与目标

### 1.1 当前问题汇总

| # | 严重度 | 问题 | 描述 |
|---|--------|------|------|
| 1 | 🔴 Bug | `deptField` 默认值与数据库列名不一致 | 注解默认 `"creator_dept_id"`，数据库实际列名为 `"creator_dept"`。Handler 中 `Optional.ofNullable()` 因注解有非 null 默认值导致回退逻辑 `orElse` 永不可达 |
| 2 | 🔴 Bug | 显式字段名缺少表别名前缀 | `deptField` 显式设置时直接使用原值不加别名，多表 JOIN 时产生列歧义 |
| 3 | 🟠 缺陷 | 权限变更不生效 | `DataPermissionChangedEvent` 已定义但零引用——从未 publish，从未被 listen。角色/部门变更后 Redis 中 `LoginUserInfo` 保持旧值，Token 有效期内权限永不更新 |
| 4 | 🟠 缺陷 | 多角色权限合并策略不明确 | 所有角色的 `dataScope` 直接取 Set 并集，OR 拼接。例如 DEPT_AND_CHILD + SELF 同时生效，暴露范围超出预期 |
| 5 | 🟡 设计 | 同列 OR 条件冗余 | 多个 scope 作用在同一列时生成 `dept_id = X OR dept_id IN (...)`，应合并为一个 IN |
| 6 | 🟡 设计 | 不支持类级别注解 | `@DataScope` 仅支持方法级别，需在每个 Mapper 方法上重复标注 |
| 7 | 🟡 设计 | 不支持 Service 层注解 | 无法在 Service 类/方法上标注 `@DataScope` |
| 8 | 🟡 设计 | `getDataScope()` 反射脆弱 | 仅查 `getMethods()`（public 方法），重载方法匹配不精确，`Class.forName` 失败后不重试 |
| 9 | 🟢 次要 | 缺少单元测试 | `MyDataPermissionHandler` 零测试覆盖 |

### 1.2 设计目标

- 修复列名 Bug，确保默认行为直接可用
- 权限变更后**主动推送**最新 `LoginUserInfo` 到 Redis，实现即时生效
- 支持 `@DataScope` 注解在 Mapper 类/方法、Service 类/方法四层
- 同列 OR 条件合并为 IN，优化 SQL 可读性和执行计划
- 编写完整的单元测试

## 2. 改动范围

```
zs-common-core
  └── events/
      └── DataPermissionChangedEvent.java   [修改] 增加 ChangeType 枚举、tenantId

zs-common-mybatis
  ├── annotation/
  │   └── DataScope.java                   [修改] @Target 增加 TYPE, deptField 默认值修正为 "creator_dept"
  ├── handler/
  │   └── MyDataPermissionHandler.java     [重构] 修Bug + 四层注解查找 + 同列OR合并为IN
  ├── aspect/
  │   └── DataScopeAspect.java             [新增] Service 层 AOP 切面
  └── context/
      └── DataScopeContext.java            [新增] ThreadLocal 容器

zs-system-service
  ├── events/
  │   └── DataPermissionSyncListener.java  [新增] 主动重建权限并推送 Redis
  ├── role/service/impl/
  │   ├── SysRoleServiceImpl.java          [修改] role 变更时发布事件
  │   └── SysRoleDeptServiceImpl.java      [修改] 自定义部门变更时发布事件
  ├── dept/service/impl/
  │   └── SysDeptServiceImpl.java          [修改] 部门结构变更时发布事件
  └── user/service/impl/
      └── SysUserRoleServiceImpl.java      [修改] 用户角色关联变更时发布事件
```

## 3. 详细设计

### 3.1 `@DataScope` 注解

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /** 表别名，多表关联时指定，如 "o" */
    String tableAlias() default "";

    /** 部门字段名，数据库列名：creator_dept */
    String deptField() default "creator_dept";

    /** 用户字段名，数据库列名：creator */
    String userField() default "creator";
}
```

**改动说明**：
- `deptField()` 默认值：`"creator_dept_id"` → `"creator_dept"`（对齐 DB 列名）
- `@Target`：增加 `ElementType.TYPE`，支持类级别
- `userField()` 默认值不变（DB 列名已是 `creator`）

### 3.2 四层注解查找优先级

```
Service 方法 @DataScope   →  ① ThreadLocal（AOP 传入）
Service 类   @DataScope   →  ② ThreadLocal（AOP 传入）
Mapper 方法  @DataScope   →  ③ 反射解析
Mapper 类   @DataScope    →  ④ 反射解析
        全部未找到        →  不应用数据权限
```

①和②通过 `DataScopeAspect` + `DataScopeContext`（ThreadLocal）传递。
③和④由 `MyDataPermissionHandler.getDataScope()` 反射解析。

#### DataScopeAspect

```java
@Aspect
@Order(1)
@Component
public class DataScopeAspect {

    @Around("@within(com.zs.common.mp.annotation.DataScope) || "
          + "@annotation(com.zs.common.mp.annotation.DataScope)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        DataScope dataScope = resolveScope(joinPoint);
        if (dataScope != null) {
            DataScopeContext.set(dataScope);
        }
        try {
            return joinPoint.proceed();
        } finally {
            DataScopeContext.clear();
        }
    }

    private DataScope resolveScope(ProceedingJoinPoint jp) {
        MethodSignature sig = (MethodSignature) jp.getSignature();
        // 方法级别优先
        DataScope m = sig.getMethod().getAnnotation(DataScope.class);
        if (m != null) return m;
        // 类级别
        return jp.getTarget().getClass().getAnnotation(DataScope.class);
    }
}
```

#### DataScopeContext

```java
public class DataScopeContext {
    private static final ThreadLocal<DataScope> HOLDER = new ThreadLocal<>();
    public static void set(DataScope s)  { HOLDER.set(s); }
    public static DataScope get()        { return HOLDER.get(); }
    public static void clear()           { HOLDER.remove(); }
}
```

### 3.3 MyDataPermissionHandler 重构

#### 3.3.1 注解解析

```java
@Nullable
private DataScope getDataScope(@NotNull String mappedStatementId) {
    return dataScopeCache.computeIfAbsent(mappedStatementId, id -> {
        // ① 优先 ThreadLocal（Service 层 AOP 传入）
        DataScope serviceScope = DataScopeContext.get();
        if (serviceScope != null) return serviceScope;

        // ② 解析 Mapper 反射
        int lastDot = id.lastIndexOf(".");
        String className = id.substring(0, lastDot);
        String methodName = id.substring(lastDot + 1);

        try {
            Class<?> clazz = Class.forName(className);

            // ③ Mapper 方法级别
            Method method = findAnnotatedMethod(clazz, methodName);
            if (method != null) return method.getAnnotation(DataScope.class);

            // ④ Mapper 类级别
            if (clazz.isAnnotationPresent(DataScope.class)) {
                return clazz.getAnnotation(DataScope.class);
            }
        } catch (ClassNotFoundException e) {
            logger.error("获取mapper类失败: {}", className, e);
        }
        return null;
    });
}
```

#### 3.3.2 字段名解析（修 Bug）

```java
private String resolveColumn(String fieldValue, String aliasName, String defaultColumn) {
    String field = (fieldValue != null && !fieldValue.isEmpty())
            ? fieldValue        // 注解显式设置 → 如 "custom_dept"
            : defaultColumn;    // 兜底默认 → 如 "creator_dept"
    return aliasName + "." + field;
}

// 调用
String depIdColumn  = resolveColumn(dataScope.deptField(), aliasName, "creator_dept");
String userIdColumn = resolveColumn(dataScope.userField(), aliasName, "creator");
```

**Bug 分析**：

当前代码：
```java
String depIdColumn = Optional.ofNullable(dataScope.deptField())
    .orElse(aliasName + ".create_dept_id");
```

因为注解 `deptField()` 有非 null 默认值 `"creator_dept_id"`，`Optional.ofNullable()` 永远不为 null，`orElse("create_dept_id")` 永不可达。生成的 SQL 引用不存在的列 `creator_dept_id`。

修复后：字段值总是加别名前缀 + 正确的默认列名 `creator_dept`。

#### 3.3.3 同列 OR 合并为 IN

```java
private Expression buildPermissionExpression(Set<DataScopeEnum> scopeTypes,
                                              LoginUserInfo user,
                                              String depColumn, String userColumn) {
    Set<Long> deptIds = new LinkedHashSet<>();  // 去重 + 保序
    boolean hasUserCondition = false;

    for (DataScopeEnum scope : scopeTypes) {
        switch (scope) {
            case DEPT -> deptIds.add(user.getSysUser().getSysDeptId());
            case DEPT_AND_CHILD, CUSTOM ->
                deptIds.addAll(user.getDataPermission().getDeptIds());
            case SELF -> hasUserCondition = true;
        }
    }

    List<Expression> parts = new ArrayList<>();

    // 同列合并为一个 IN
    if (!deptIds.isEmpty()) {
        List<Expression> idExprs = deptIds.stream()
                .map(LongValue::new)
                .collect(Collectors.toList());
        parts.add(new InExpression(
                new Column(depColumn),
                new Parenthesis(new ExpressionList<>(idExprs))));
    }

    if (hasUserCondition) {
        parts.add(new EqualsTo(
                new Column(userColumn),
                new LongValue(user.getUserId())));
    }

    return parts.stream().reduce(OrExpression::new).orElse(null);
}
```

**效果**：

```sql
-- 优化前（冗余 OR）
WHERE (o.creator_dept = 123) OR (o.creator_dept IN (123, 124, 125))

-- 优化后（合并）
WHERE o.creator_dept IN (123, 124, 125) OR o.creator = 5
```

### 3.4 权限变更事件体系

#### 3.4.1 DataPermissionChangedEvent

```java
public class DataPermissionChangedEvent extends ApplicationEvent {

    public enum ChangeType {
        ROLE_UPDATED,       // 角色 dataScope 变更
        ROLE_DELETED,       // 角色删除
        ROLE_DEPT_CHANGED,  // 角色自定义部门变更
        DEPT_CHANGED,       // 部门结构变更（影响 DEPT_AND_CHILD）
        USER_ROLE_CHANGED,  // 用户-角色关联变更
    }

    private final ChangeType changeType;
    private final Set<Long> affectedUserIds;

    public DataPermissionChangedEvent(Object source, ChangeType changeType,
                                       Set<Long> affectedUserIds) {
        super(source);
        this.changeType = changeType;
        this.affectedUserIds = affectedUserIds;
    }

    public ChangeType getChangeType()           { return changeType; }
    public Set<Long> getAffectedUserIds()       { return affectedUserIds; }
}
```

#### 3.4.2 事件发布点

| 位置 | 触发条件 | ChangeType | affectedUserIds 来源 |
|------|----------|------------|---------------------|
| `SysRoleServiceImpl.update()` | `dataScope` 或 `deptList` 变更 | `ROLE_UPDATED` | `iSysUserRoleService.queryByRoleId(roleId)` |
| `SysRoleServiceImpl.deleteById()` | 角色删除 | `ROLE_DELETED` | 删除前查询 |
| `SysRoleDeptServiceImpl.save()` | 自定义部门变更 | `ROLE_DEPT_CHANGED` | `iSysUserRoleService.queryByRoleId(roleId)` |
| `SysDeptServiceImpl.save()` | 新增部门 | `DEPT_CHANGED` | 父部门及上级部门下的用户 |
| `SysDeptServiceImpl.update()` | 修改部门（影响子部门递归） | `DEPT_CHANGED` | 该部门及子部门下的用户 |
| `SysDeptServiceImpl.removeById()` | 删除部门 | `DEPT_CHANGED` | 删除前查询部门下的用户 |
| `SysUserRoleServiceImpl.save()` | 用户角色分配变更 | `USER_ROLE_CHANGED` | 被分配的用户 ID 集合 |

#### 3.4.3 主动推送监听器

位置：`zs-modules/zs-system/zs-system-service/src/main/java/com/zs/sys/events/DataPermissionSyncListener.java`

前提：需在 Spring Boot 配置类中启用 `@EnableAsync`（如果尚未启用）。

```java
@Component
@Slf4j
public class DataPermissionSyncListener {

    @Resource private SysUserMapper sysUserMapper;
    @Resource private ISysRoleService roleService;
    @Resource private ISysDeptService deptService;
    @Resource private ISysMenuService menuService;
    @Resource private RedisUtil redisUtil;
    @Resource private JwtUtil jwtUtil;

    @EventListener
    @Async
    public void onDataPermissionChanged(DataPermissionChangedEvent event) {
        log.info("收到数据权限变更事件: type={}, affectedCount={}",
                event.getChangeType(), event.getAffectedUserIds().size());

        for (Long userId : event.getAffectedUserIds()) {
            try {
                syncUserPermission(userId);
            } catch (Exception e) {
                log.error("同步用户[{}]权限缓存失败", userId, e);
            }
        }
    }

    private void syncUserPermission(Long userId) {
        SysUserEntity entity = sysUserMapper.selectById(userId);
        if (entity == null) {
            log.warn("用户[{}]不存在，跳过权限同步", userId);
            return;
        }

        // 1. 重建基础信息
        SysUser sysUser = toSysUser(entity);

        // 2. 重建 DataPermission
        List<SysRoleEntity> roles = roleService.findByUserId(userId);
        DataPermission dp = buildDataPermission(sysUser, roles);

        // 3. 重建权限集合
        Set<String> permissions = buildPermissions(entity);

        // 4. 组装 LoginUserInfo
        LoginUserInfo updated = new LoginUserInfo(sysUser, permissions, dp);

        // 5. 主动推送至 Redis
        String redisKey = "sys_login_info:" + userId;
        redisUtil.setObject(redisKey, updated, jwtUtil.getExpirationTime(), TimeUnit.SECONDS);

        log.debug("已同步用户[{}]权限缓存到Redis, roles={}", userId, roles.size());
    }

    private SysUser toSysUser(SysUserEntity entity) {
        SysUser u = BeanUtil.toBean(entity, SysUser.class);
        u.setUserId(entity.getSysUserId());
        return u;
    }

    private DataPermission buildDataPermission(SysUser sysUser, List<SysRoleEntity> roles) {
        DataPermission dp = new DataPermission();
        dp.setUserId(sysUser.getSysUserId());
        dp.setDeptId(sysUser.getSysDeptId());
        dp.setRoleIds(roles.stream().map(SysRoleEntity::getSysRoleId).collect(Collectors.toSet()));

        Set<Integer> scopeValues = roles.stream()
                .map(SysRoleEntity::getDataScope).filter(Objects::nonNull)
                .collect(Collectors.toSet());

        dp.setDataScopeTypes(scopeValues.stream()
                .map(DataScopeEnum::value).collect(Collectors.toSet()));

        Set<Long> deptIds = new HashSet<>();
        if (scopeValues.contains(DataScopeEnum.CUSTOM.getValue())) {
            deptIds.addAll(roleService.getRoleDeptIds(sysUser.getSysUserId()));
        }
        if (scopeValues.contains(DataScopeEnum.DEPT_AND_CHILD.getValue())) {
            deptIds.addAll(deptService.getDeptAndChildrenDeptIds(sysUser.getSysDeptId()));
        }
        dp.setDeptIds(deptIds);

        return dp;
    }

    private Set<String> buildPermissions(SysUserEntity entity) {
        if (entity.getIsAdmin() == AdminEnum.Admin.getValue()) {
            return menuService.getAllPermissions();
        }
        return menuService.getPermissions(entity.getSysUserId());
    }
}
```

## 4. 数据流

```
角色/部门/用户角色变更
       │
       │ publish DataPermissionChangedEvent(affectedUserIds)
       ▼
┌──────────────────────────────────┐
│  @Async @EventListener           │  ← zs-system-service
│  DataPermissionSyncListener      │
│                                  │
│  for each userId:                 │
│    ① 查用户实体                   │
│    ② buildDataPermission()       │
│    ③ getPermissions()            │
│    ④ new LoginUserInfo(...)      │
│    ⑤ redisUtil.setObject(...)    │  ← 主动推送最新权限
└──────────────────────────────────┘
       │
       │ 用户下次请求
       ▼
┌──────────────────────────────────┐
│  JwtAuthenticationTokenFilter     │
│  Redis 已有最新 LoginUserInfo    │
│  → set SecurityContext            │
└──────────────┬───────────────────┘
               ▼
┌──────────────────────────────────┐
│  MyDataPermissionHandler         │
│  ① 四层注解查找                   │
│  ② 字段名解析（别名+列名）         │
│  ③ 同列合并为 IN                  │
│  ④ 注入 SQL WHERE 条件            │
└──────────────────────────────────┘
```

## 5. 测试策略

### 5.1 MyDataPermissionHandler 单元测试

| 测试场景 | 输入 | 预期输出 |
|----------|------|----------|
| 无注解方法 | mappedStatementId 对应无 @DataScope 方法 | 返回 null / 原 where |
| 方法级注解 | @DataScope(deptField="custom") | SQL 含 `alias.custom IN (...)` |
| 类级注解 | Mapper 类有 @DataScope，方法无 | 应用类级别配置 |
| 方法覆盖类 | 方法注解覆盖类注解 | 方法注解生效 |
| Service AOP 传入 | ThreadLocal 有 DataScope | 优先于 Mapper 注解 |
| 单角色 DEPT | dataScopeTypes={DEPT} | `WHERE alias.creator_dept = userDeptId` |
| 多角色 DEPT+SELF | dataScopeTypes={DEPT, SELF} | `WHERE alias.creator_dept IN (deptId) OR alias.creator = userId` |
| ALL 权限 | dataScopeTypes={ALL} | 不附加任何条件 |
| Admin 用户 | isAdmin = Admin | 不附加任何条件 |
| 同列合并 | DEPT + DEPT_AND_CHILD | 合并为一个 IN 表达式 |
| 无用户信息 | SecurityContext 无值 | 跳过，返回原 where |
| 表别名 | tableAlias="o" | 使用别名 "o" 构造列名 |

### 5.2 事件流程集成测试

| 测试场景 | 验证点 |
|----------|--------|
| 角色 dataScope 变更 | Redis 中 LoginUserInfo 已更新，dataScopeTypes 变化 |
| 自定义部门变更 | Redis 中 LoginUserInfo.deptIds 已更新 |
| 部门增删 | 受影响用户的 deptIds 已更新 |
| 用户角色分配 | Redis 中用户权限已更新 |
| 异步执行不阻塞主流程 | update 接口响应时间不因事件处理显著增加 |

## 6. 风险与缓解

| 风险 | 缓解 |
|------|------|
| `@Async` 失败（线程池耗尽/Redis 不可用） | `@Async` 失败不阻塞主流程；下次请求 JWT Filter 从 Redis 读不到缓存会重新调用 `loadUserByUsername` 重建 |
| 反射性能 | 已用 `ConcurrentHashMap` 缓存注解解析结果 |
| `DataScopeSyncListener` 中的 `buildDataPermission` 重复了 `SysUserServiceImpl` 的逻辑 | 后续可提取为独立的 `DataPermissionBuilder` 组件，避免重复 |
| 多租户场景 | 监听器使用与主流程相同的 Mapper，自动继承租户隔离 |
| 大量用户同时受影响 | `@Async` 异步处理，不阻塞 API 响应；可配置线程池大小 |
