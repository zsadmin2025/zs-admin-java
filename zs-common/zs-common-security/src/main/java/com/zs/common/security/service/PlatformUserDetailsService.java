package com.zs.common.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.zs.common.core.model.LoginUserInfo;

/**
 * 平台端用户详情服务
 *
 * @author zsadmin
 */
public interface PlatformUserDetailsService {

    /**
     * 根据用户名加载用户
     */
    UserDetails loadUserByUsername(String username);

    /**
     * 根据用户名和租户ID加载用户
     */
    LoginUserInfo loadUserByUsernameAndTenant(String username, String tenantId);
}
