package com.zs.common.core.utils;


import com.zs.common.core.enums.AdminEnum;
import com.zs.common.core.model.LoginUserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * springSecurity工具类
 *
 * @author zsadmin
 */
public class SecurityUtil {


    public static LoginUserInfo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }
        return (LoginUserInfo) authentication.getPrincipal();
    }

    public static Long getUserId() {
        LoginUserInfo userInfo = getUserInfo();
        return userInfo != null ? userInfo.getUserId() : null;
    }

    public static String getUsername() {
        LoginUserInfo userInfo = getUserInfo();
        return userInfo != null ? userInfo.getUsername() : null;
    }

    public static String getRealName() {
        LoginUserInfo userInfo = getUserInfo();
        return userInfo != null && userInfo.getSysUser() != null
                ? userInfo.getSysUser().getRealName() : null;
    }

    public static boolean isAdmin() {
        LoginUserInfo userInfo = getUserInfo();
        return userInfo != null && userInfo.getIsAdmin() == AdminEnum.Admin.getValue();
    }




}
