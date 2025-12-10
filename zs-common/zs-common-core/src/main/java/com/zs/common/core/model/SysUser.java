package com.zs.common.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;


/**
 * @author zsadmin
 */
@Data
public class SysUser {

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
    private String ip;
    private String ipAddress;
    private String browser;
    private String os;
    private Date loginTime;
    private Integer status;

    private Long sysDeptId;
    private String deptName;

    private Long sysPostId;
    private String postName;

    private String createTime;

    private Long tenantId;
}
