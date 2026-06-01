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
    
    // 改为使用 getMethods()（包含继承方法）+ ConcurrentHashMap 缓存
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

        // 待执行 SQL Where 条件表达式
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

        // 管理员/全部权限：直接返回原始WHERE
        if (Objects.equals(loginUserInfo.getIsAdmin(), AdminEnum.Admin.getValue())) {
            return where;
        }


        Set<DataScopeEnum> dataScopeTypes = Optional.of(loginUserInfo.getDataPermission())
                .map(DataPermission::getDataScopeTypes)
                .orElse(Collections.emptySet());
        // 无数据权限, 直接返回原始WHERE。 全部权限，跳过数据权限过滤，返回原始WHERE
        if (dataScopeTypes.isEmpty() || dataScopeTypes.contains(DataScopeEnum.ALL)) {
            return where;
        }

        Table table = (Table) plainSelect.getFromItem();
        String aliasName = getTableAlias(table, dataScope);
        String depIdColumn = Optional.ofNullable(dataScope.deptField()).orElse(aliasName + ".create_dept_id");
        String userIdColumn = Optional.ofNullable(dataScope.userField()).orElse(aliasName + ".creator");


        List<Expression> expressions = dataScopeTypes.stream()
                .filter(scope -> scope != DataScopeEnum.ALL) // 跳过所有权限
                .map(scope -> createExpressionForScope(scope, loginUserInfo, depIdColumn, userIdColumn))
                .filter(Objects::nonNull)
                .toList();


        Expression dataScopesExpression = expressions.stream().reduce(OrExpression::new).orElse(null);

        // 修复后代码
        if (dataScopesExpression == null) {
            // 无有效数据权限条件，直接返回原始WHERE
            return where;
        }
        return where == null ? dataScopesExpression : new AndExpression(where, new Parenthesis(dataScopesExpression));

    }

    private String getTableAlias(Table table, DataScope dataScope) {
        String annotationAlias = dataScope.tableAlias();
        if (annotationAlias != null && !annotationAlias.isEmpty()) {
            return annotationAlias;
        }
        return Optional.ofNullable(table.getAlias())
                .map(Alias::getName)
                .orElse(table.getName());
    }

    /**
     * 创建特定数据范围的SQL表达式
     * @param dataScope 数据范围类型
     * @param loginUserInfo 登录用户信息
     * @return SQL表达式
     */
    private Expression createExpressionForScope(DataScopeEnum dataScope, LoginUserInfo loginUserInfo, String depIdColumn, String userIdColumn) {
        return switch (dataScope) {
            case CUSTOM -> createCustomDeptPermissionExpression(loginUserInfo, depIdColumn);
            case DEPT -> createOwnDeptPermissionExpression(loginUserInfo, depIdColumn);
            case DEPT_AND_CHILD -> createDeptWithChildrenPermissionExpression(loginUserInfo, depIdColumn);
            case SELF -> createCreatedByUserPermissionExpression(loginUserInfo,  userIdColumn);
            default -> null;
        };
    }

    // 自定义部门权限表达式
    @Nullable
    private Expression createCustomDeptPermissionExpression(LoginUserInfo loginUserInfo, String depIdColumn) {
        Set<Long> deptIds = loginUserInfo.getDataPermission().getDeptIds();
        if (deptIds.isEmpty()) {
            return null;
        }
        List<Expression> deptIdList = deptIds.stream()
                .map(LongValue::new)
                .map(longValue -> (Expression) longValue)
                .collect(Collectors.toList());

        ExpressionList<Expression> deptIdExpressions = new ExpressionList<>(deptIdList);
        return new InExpression(new Column(depIdColumn), new Parenthesis(deptIdExpressions));
    }

    // 自己部门权限表达式
    @NotNull
    private Expression createOwnDeptPermissionExpression(@NotNull LoginUserInfo loginUserInfo,  String depIdColumn) {
        return new EqualsTo(
                new Column(depIdColumn),
                new LongValue(loginUserInfo.getSysUser().getSysDeptId())
        );
    }

    // 部门及子部门权限表达式
    @Nullable
    private Expression createDeptWithChildrenPermissionExpression(LoginUserInfo loginUserInfo, String depIdColumn) {
        Set<Long> deptIdsWithChildren = loginUserInfo.getDataPermission().getDeptIds();
        if (deptIdsWithChildren.isEmpty()) {
            return null;
        }

        List<Expression> deptIdList = deptIdsWithChildren.stream()
                .map(LongValue::new)
                .map(longValue -> (Expression) longValue)  // 显式转换类型
                .collect(Collectors.toList());

        ExpressionList<Expression> deptIdExpressions = new ExpressionList<>(deptIdList);
        return new InExpression(new Column(depIdColumn), new Parenthesis(deptIdExpressions));
    }

    // 本人权限表达式
    @NotNull
    private Expression createCreatedByUserPermissionExpression(@NotNull LoginUserInfo loginUserInfo, String userIdColumn) {
        return new EqualsTo(
                new Column(userIdColumn),
                new LongValue(loginUserInfo.getUserId())
        );
    }


    /**
     * 从类和方法中获取数据范围注解
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
                // ✅ getMethods() 包含本类及继承的全部 public 方法
                Method method = Arrays.stream(clazz.getMethods())
                        .filter(m -> m.getName().equals(methodName)
                                && m.isAnnotationPresent(DataScope.class))
                        .findFirst()
                        .orElse(null);
                return method != null ? method.getAnnotation(DataScope.class) : null;
            } catch (ClassNotFoundException e) {
                logger.error("获取mapper类失败", e);
            }
            return null;
        });
    }

}
