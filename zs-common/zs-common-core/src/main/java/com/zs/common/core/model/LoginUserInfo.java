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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zsadmin
 */
@Data
public class LoginUserInfo implements UserDetails {

   
    private BaseUserInfo userInfo;

    private Set<String> permissions;

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

    public SysUser getPlatformUser() {
        if (userInfo instanceof SysUser p) {
            return p;
        }
        throw new IllegalStateException("当前用户不是平台端用户，实际类型: " + getUserType());
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
        return userInfo.getStatus() == 1;
    }

}
