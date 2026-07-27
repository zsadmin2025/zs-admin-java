package com.zs.common.mp.handler;


import com.zs.common.core.enums.AdminEnum;
import com.zs.common.core.enums.DataScopeEnum;
import com.zs.common.core.model.DataPermission;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.utils.SecurityUtil;
import com.zs.common.mp.annotation.DataScope;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class MyDataPermissionHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyDataPermissionHandler.class);

    private final Map<String, DataScope> dataScopeCache = new ConcurrentHashMap<>();


    /**
     * 根据SQL语句和映射语句ID生成数据权限SQL片段
     * @param plainSelect SQL语句
     * @param mappedStatementId 映射语句ID
     * @return 数据权限SQL表达式
     */
    @Nullable
    public Expression getSqlSegment(@NotNull PlainSelect plainSelect, @NotNull String mappedStatementId) {
        DataScope dataScope = this.getDataScope(mappedStatementId);
        if (dataScope == null) {
            return plainSelect.getWhere(); // 无注解，返回原始WHERE
        }

        Expression where = plainSelect.getWhere();
        return doFilter(plainSelect, where, dataScope);
    }


    /**
     * 执行数据权限过滤
     * @param plainSelect SQL语句
     * @param where 原始WHERE条件
     * @return 过滤后的WHERE条件
     */
    @Nullable
    public Expression doFilter(@NotNull PlainSelect plainSelect, @Nullable Expression where, @NotNull DataScope dataScope) {
        // 获取当前登录用户信息
        LoginUserInfo loginUserInfo = SecurityUtil.getUserInfo();

        if (loginUserInfo == null || loginUserInfo.getUserInfo() == null) {
            logger.warn("未获取到登录用户信息，跳过数据权限过滤");
            return where;
        }

        if (loginUserInfo.getDataPermission() == null || loginUserInfo.getDataPermission().getDataScopeTypes() == null) {
            logger.warn("用户数据权限信息为空，跳过数据权限过滤");
            return where;
        }

        // 管理员：直接返回原始WHERE
        if (Objects.equals(loginUserInfo.getIsAdmin(), AdminEnum.Admin.getValue())) {
            return where;
        }

        Set<DataScopeEnum> dataScopeTypes = Optional.of(loginUserInfo.getDataPermission())
                .map(DataPermission::getDataScopeTypes)
                .orElse(Collections.emptySet());
        if (dataScopeTypes.isEmpty() || dataScopeTypes.contains(DataScopeEnum.ALL)) {
            return where;
        }

        String aliasName = getTableAlias(plainSelect, dataScope);
        String depIdColumn = resolveColumn(dataScope.deptField(), aliasName, "creator_dept");
        String userIdColumn = resolveColumn(dataScope.userField(), aliasName, "creator");

        // 同列 OR 合并为 IN，生成优化后的权限表达式
        Expression dataScopesExpression = buildPermissionExpression(dataScopeTypes, loginUserInfo, depIdColumn, userIdColumn);

        if (dataScopesExpression == null) {
            return where;
        }
        return where == null ? dataScopesExpression : new AndExpression(where, new Parenthesis(dataScopesExpression));
    }

    /**
     * 解析表别名：优先用注解别名，其次用SQL解析出的别名，最后用表名
     * 兼容子查询、JOIN等 getFromItem() 不是 Table 类型的场景
     */
    private String getTableAlias(PlainSelect plainSelect, DataScope dataScope) {
        // 注解显式指定了别名 → 直接使用
        String annotationAlias = dataScope.tableAlias();
        if (annotationAlias != null && !annotationAlias.isEmpty()) {
            return annotationAlias;
        }

        // 从 SQL 解析别名
        FromItem fromItem = plainSelect.getFromItem();
        if (fromItem instanceof Table table) {
            return Optional.ofNullable(table.getAlias())
                    .map(Alias::getName)
                    .orElse(table.getName());
        }

        // 子查询 / JOIN / UNION 等非标准 Table 场景：不追加别名前缀
        logger.debug("getFromItem() 不是 Table 类型 ({}), 使用无别名模式", fromItem.getClass().getSimpleName());
        return "";
    }

    /**
     * 统一解析字段列名：有别名时加前缀，无别名时直接使用列名
     *
     * @param fieldValue    注解中设置的字段值（可为空串）
     * @param aliasName     表别名（可为空串）
     * @param defaultColumn 默认列名
     * @return 列引用，"o.creator_dept" 或直接 "creator_dept"
     */
    private String resolveColumn(String fieldValue, String aliasName, String defaultColumn) {
        String field = (fieldValue != null && !fieldValue.isEmpty())
                ? fieldValue        // 注解显式设置
                : defaultColumn;    // 兜底默认值
        if (aliasName != null && !aliasName.isEmpty()) {
            return aliasName + "." + field;
        }
        return field;
    }

    /**
     * 构建数据权限 SQL 表达式，将同一列的多个条件合并为一个 IN 表达式
     *
     * @param scopeTypes    数据权限类型集合
     * @param loginUserInfo 登录用户信息
     * @param depColumn     部门字段列名（含别名）
     * @param userColumn    用户字段列名（含别名）
     * @return 优化后的 SQL 表达式
     */
    @Nullable
    private Expression buildPermissionExpression(Set<DataScopeEnum> scopeTypes,
                                                  LoginUserInfo loginUserInfo,
                                                  String depColumn, String userColumn) {
        Set<Long> deptIds = new LinkedHashSet<>();  // 去重 + 保持插入顺序
        boolean hasUserCondition = false;

        for (DataScopeEnum scope : scopeTypes) {
            switch (scope) {
                case DEPT -> {
                    Long userDeptId = loginUserInfo.getSysUser() != null
                            ? loginUserInfo.getSysUser().getSysDeptId() : null;
                    if (userDeptId != null) {
                        deptIds.add(userDeptId);
                    }
                }
                case DEPT_AND_CHILD, CUSTOM -> {
                    DataPermission dp = loginUserInfo.getDataPermission();
                    if (dp != null && dp.getDeptIds() != null) {
                        deptIds.addAll(dp.getDeptIds());
                    }
                }
                case SELF -> hasUserCondition = true;
                case ALL -> { /* ALL 已在外层提前返回，此处不处理 */ }
            }
        }

        List<Expression> parts = new ArrayList<>();

        // 同列合并：多个 dept ID → 一个 IN 表达式
        if (!deptIds.isEmpty()) {
            List<Expression> idExprs = deptIds.stream()
                    .filter(Objects::nonNull)         // 过滤 null 值，防止 LongValue 拆箱 NPE
                    .map(LongValue::new)
                    .collect(Collectors.toList());
            if (!idExprs.isEmpty()) {
                parts.add(new InExpression(
                        new Column(depColumn),
                        new Parenthesis(new ExpressionList<>(idExprs))));
            }
        }

        if (hasUserCondition) {
            parts.add(new EqualsTo(
                    new Column(userColumn),
                    new LongValue(loginUserInfo.getUserId())));
        }

        // 不同列之间用 OR 连接
        return parts.stream().reduce(OrExpression::new).orElse(null);
    }


    /**
     * 从 Mapper 接口获取数据范围注解，两级优先级查找：
     * ① Mapper 方法级别（反射解析）
     * ② Mapper 类级别（反射解析）
     *
     * @param mappedStatementId 映射语句ID
     * @return 数据范围注解
     */
    @Nullable
    private DataScope getDataScope(@NotNull String mappedStatementId) {
        return dataScopeCache.computeIfAbsent(mappedStatementId, id -> {
            int lastDotIndex = id.lastIndexOf(".");
            if (lastDotIndex == -1) {
                logger.warn("无效的mappedStatementId格式: {}", id);
                return null;
            }

            String className = id.substring(0, lastDotIndex);
            String methodName = id.substring(lastDotIndex + 1);

            try {
                Class<?> clazz = Class.forName(className);

                // ① Mapper 方法级别优先
                Method method = Arrays.stream(clazz.getMethods())
                        .filter(m -> m.getName().equals(methodName)
                                && m.isAnnotationPresent(DataScope.class))
                        .findFirst()
                        .orElse(null);

                if (method != null) {
                    return method.getAnnotation(DataScope.class);
                }

                // ② Mapper 类级别兜底
                if (clazz.isAnnotationPresent(DataScope.class)) {
                    return clazz.getAnnotation(DataScope.class);
                }

                return null;
            } catch (ClassNotFoundException e) {
                logger.error("获取mapper类失败: {}", className, e);
                return null;
            }
        });
    }

}
