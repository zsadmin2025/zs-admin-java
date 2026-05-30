package com.zs.common.security.service.impl;

import com.zs.common.core.enums.UserTypeEnum;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.tenant.TenantContext;
import com.zs.common.security.service.CustomUserDetailsService;
import com.zs.common.security.service.PlatformUserDetailsService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * 平台端用户详情服务实现
 * 会员端走 MemberLoginController 直接调用 MemberUserDetailsService，不经过此类
 *
 * @author zsadmin
 */
@Service
public class MultiUserDetailsServiceImpl implements CustomUserDetailsService {

    @Resource
    private PlatformUserDetailsService platformUserDetailsService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        String tenantId = TenantContext.getTenantId();
        return loadUserByUserType(username, tenantId, UserTypeEnum.PLATFORM);
    }

    @Override
    public LoginUserInfo loadUserByUserType(String username, String tenantId, UserTypeEnum userTypeEnum) {
        return switch (userTypeEnum) {
            case PLATFORM -> platformUserDetailsService.loadUserByUsernameAndTenant(username, tenantId);
            case MEMBER -> null;
            default -> throw new IllegalArgumentException("Unsupported user type: " + userTypeEnum);
        };
    }

}
