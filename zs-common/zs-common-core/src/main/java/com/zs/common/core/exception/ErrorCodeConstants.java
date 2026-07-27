package com.zs.common.core.exception;

public interface ErrorCodeConstants {

    ErrorCode LOGIN_ERROR = new ErrorCode(401, "登录失败");
   ErrorCode SYSTEM_ERROR = new ErrorCode(500, "系统错误");
    ErrorCode CAPTCHA_ERROR = new ErrorCode(10001, "验证码错误");

    ErrorCode USER_NOT_EXIST = new ErrorCode(10002, "用户不存在");

    ErrorCode USER_PASSWORD_ERROR = new ErrorCode(10003, "用户密码错误");

    ErrorCode USER_NOT_LOGIN = new ErrorCode(10004, "用户未登录");

    ErrorCode USER_NOT_PERMISSION = new ErrorCode(10005, "用户没有权限");

    ErrorCode USER_NOT_ROLE = new ErrorCode(10006, "用户没有角色");

    ErrorCode USER_NOT_ENABLE = new ErrorCode(10007, "用户未启用");

    ErrorCode USER_LOGIN_ERROR = new ErrorCode(10008, "用户登录失败");

    ErrorCode USER_LOGOUT_ERROR = new ErrorCode(10009, "用户登出失败");

    ErrorCode USER_LOGIN_NAME_EXIST = new ErrorCode(10010, "用户登录名已存在");

    ErrorCode TENANT_NOT_EXIST = new ErrorCode(10011, "租户不存在");

    ErrorCode TENANT_NOT_ENABLE = new ErrorCode(10012, "租户未启用");

    ErrorCode TENANT_PACKAGE_NOT_EXIST = new ErrorCode(10013, "租户套餐不存在");

    ErrorCode TENANT_PACKAGE_NOT_ENABLE = new ErrorCode(10014, "租户套餐未启用");

    ErrorCode TENANT_NAME_EXIST = new ErrorCode(10015, "租户名称已存在");

    ErrorCode TENANT_SYSTEM_NOT_DELETE = new ErrorCode(10016, "系统租户，不能删除");

    ErrorCode TENANT_ADMIN_ROLE_NOT_EXIST = new ErrorCode(10017, "未找到租户管理员角色");
}
