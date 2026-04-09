package com.zs.common.core.model.user;

import com.zs.common.core.enums.UserTypeEnum;
import com.zs.common.core.model.BaseUserInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PlatformUserInfo extends BaseUserInfo {
    
    private static final long serialVersionUID = 1L;
    
    private Long tenantId;
    
    private Long sysDeptId;
    
    private String deptName;
    
    private Long sysPostId;
    
    private String postName;
    
    private Integer isAdmin;
    
    private String employeeNumber;
    
    private String ip;
    
    private String ipAddress;
    
    private String browser;
    
    private String os;
    
    @Override
    public UserTypeEnum getUserType() {
        return UserTypeEnum.PLATFORM;
    }
    
    public boolean isSuperAdmin() {
        return isAdmin != null && isAdmin == 1;
    }
}