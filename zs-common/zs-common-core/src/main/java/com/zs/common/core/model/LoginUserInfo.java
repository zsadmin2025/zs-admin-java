package com.zs.common.core.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zs.common.core.enums.UserTypeEnum;
import com.zs.common.core.model.user.PlatformUserInfo;
import com.zs.common.core.model.user.SysUser;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zsadmin
 * 登录用户信息
 */
@Data
public class LoginUserInfo implements UserDetails {


    // 登录用户基础信息
    private BaseUserInfo userInfo;

    // 权限信息
    private Set<String> permissions;

    // 数据权限信息
    private DataPermission dataPermission;


    // ==================== 构造函数 ====================

    public LoginUserInfo() {
    }
    
    public LoginUserInfo(BaseUserInfo userInfo, Set<String> permissions, DataPermission dataPermission) {
        this.userInfo = userInfo;
        this.permissions = permissions;
        this.dataPermission = dataPermission;
    }

    // ==================== 工厂方法 ====================
    //  平台端用户
    public static LoginUserInfo ofPlatform(PlatformUserInfo user, Set<String> permissions, DataPermission dataPermission) {
        return new LoginUserInfo(user, permissions, dataPermission);
    }
    


    // ==================== 类型安全获取方法 ====================

    public UserTypeEnum getUserType() {
        return userInfo.getUserType();
    }

    public Long getUserId() {
        return userInfo.getUserId();
    }

    // 平台端用户
    public SysUser getPlatformUser() {
        if (userInfo instanceof SysUser p) {
            return p;
        }
        throw new IllegalStateException("当前用户不是平台端用户，实际类型: " + getUserType());
    }

    public SysUser getSysUser() {
        return getPlatformUser();
    }

    public String getRealName() {
        return userInfo.getRealName();
    }

    public Integer getIsAdmin() {
        if (userInfo instanceof SysUser s) {
            return s.getIsAdmin();
        }
        if (userInfo instanceof PlatformUserInfo p) {
            return p.getIsAdmin();
        }
        return null;
    }



    @NotNull
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (permissions == null || permissions.isEmpty()) {
            return Collections.emptySet();
        }
        return permissions.stream()
                .filter(Objects::nonNull) // 确保权限字符串不为 null
                .filter(permission -> !permission.trim().isEmpty()) // 确保权限字符串不为空或仅包含空白字符
                .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return userInfo.getPassword();
    }


    @JsonIgnore
    @Override
    public String getUsername() {
        return userInfo.getUsername();
    }

    @JsonIgnore
    public Long getSysUserId() {
        return userInfo.getUserId();
    }

    /**
     * 用户没过期返回true，反之则false
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 用户没锁定返回true，反之则false
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 用户凭据(通常为密码)没过期返回true，反之则false
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     *
     */
    @Override
    public boolean isEnabled() {
        return userInfo != null && userInfo.isEnabled();
    }

}
