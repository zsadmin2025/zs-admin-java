package com.zs.common.security.service;


import com.zs.common.core.enums.UserTypeEnum;
import com.zs.common.core.model.LoginUserInfo;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * 自定义账号密码验证逻辑
 *
 * @author zsadmin
 */
public interface CustomUserDetailsService {

    /**
     * 根据用户名加载用户
     */
    UserDetails loadUserByUsername(String username);


    /**
     * 根据用户类型加载用户
    */
    LoginUserInfo loadUserByUserType(String username, String tenantId, UserTypeEnum userTypeEnum);
}
