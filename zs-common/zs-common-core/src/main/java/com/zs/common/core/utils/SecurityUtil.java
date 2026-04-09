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
        return (LoginUserInfo) authentication.getPrincipal();
    }

    public static Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((LoginUserInfo) authentication.getPrincipal()).getUserId();
    }

    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((LoginUserInfo) authentication.getPrincipal()).getUsername();
    }

    public static String getRealName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((LoginUserInfo) authentication.getPrincipal()).getRealName();
    }

    public static boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserInfo loginUserInfo = (LoginUserInfo) authentication.getPrincipal();
        return loginUserInfo.getIsAdmin() == AdminEnum.Admin.getValue();
    }


}
