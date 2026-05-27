package com.zs.common.core.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zs.common.core.enums.UserTypeEnum;
import com.zs.common.core.model.BaseUserInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 平台端用户信息
 * @author zsadmin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseUserInfo {


    private Long sysUserId;
    private String username;

    @JsonIgnore
    private String password;
    private String realName;

    private String avatar;
    private String phone;
    private String email;
    private Integer age;
    private Integer sex;
    private String employeeNumber;
    private Integer isAdmin;

    private Long sysDeptId;
    private String deptName;

    private Long sysPostId;
    private String postName;


    private String createTime;

    private Long tenantId;

    @Override
    public UserTypeEnum getUserType() {
        return UserTypeEnum.PLATFORM;
    }


    public Long getSysUserId() {
        return getUserId();
    }

    public void setSysUserId(Long sysUserId) {
        setUserId(sysUserId);
    }
}
