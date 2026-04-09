package com.zs.common.core.model.user;

import com.zs.common.core.enums.UserTypeEnum;
import com.zs.common.core.model.BaseUserInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * @author zsadmin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseUserInfo{

    private Long sysDeptId;

    private String deptName;

    private Long sysPostId;

    private String postName;

    private Integer isAdmin;

    private Integer age;

    private Integer sex;

    private String employeeNumber;

    private String ip;

    private String ipAddress;

    private String browser;
    
    private String os;

    private Date loginTime;

    private String createTime;

    private Long tenantId;

    @Override
    public UserTypeEnum getUserType() {
        return UserTypeEnum.PLATFORM;
    }
}
