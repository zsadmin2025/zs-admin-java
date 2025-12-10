package com.zs.common.core.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zsadmin
 */
public class LoginUserInfo implements UserDetails {

    @Setter
    @Getter
    public SysUser sysUser;

    @Setter
    @Getter
    private Set<String> permissions;

    @Setter
    @Getter
    private DataPermission dataPermission;


    public LoginUserInfo(SysUser sysUser, Set<String> permissions, DataPermission dataPermission) {
        this.sysUser = sysUser;
        this.permissions = permissions;
        this.dataPermission = dataPermission;
    }


    @NotNull
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .filter(Objects::nonNull) // 确保权限字符串不为 null
                .filter(permission -> !permission.trim().isEmpty()) // 确保权限字符串不为空或仅包含空白字符
                .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return sysUser.getPassword();
    }


    @JsonIgnore
    @Override
    public String getUsername() {
        return sysUser.getUsername();
    }

    @JsonIgnore
    public Long getSysUserId() {
        return sysUser.getSysUserId();
    }

    /**
     * 用户没过期返回true，反之则false
     */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /**
     * 用户没锁定返回true，反之则false
     */
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    /**
     * 用户凭据(通常为密码)没过期返回true，反之则false
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /**
     *
     */
    @Override
    public boolean isEnabled() {
        return sysUser.getStatus() == 1;
    }

}
