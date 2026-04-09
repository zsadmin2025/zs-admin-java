package com.zs.common.security.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.zs.common.core.enums.UserTypeEnum;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.security.service.CustomUserDetailsService;
import com.zs.common.security.service.PlatformUserDetailsService;

import jakarta.annotation.Resource;

/**
 * 多用户类型业务实现
 *
 * @author zsadmin
 */
@Service
public class MultiUserDetailsServiceImpl implements CustomUserDetailsService {

    @Resource
    private PlatformUserDetailsService platformUserDetailsService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return platformUserDetailsService.loadUserByUsername(username);
    }

    @Override
    public LoginUserInfo loadUserByUserType(String username, String tenantId, UserTypeEnum userTypeEnum) {
        return switch (userTypeEnum) {
            case PLATFORM -> platformUserDetailsService.loadUserByUsernameAndTenant(username, tenantId);
            default -> throw new IllegalArgumentException("Unsupported user type: " + userTypeEnum);
        };
    }
    
}
