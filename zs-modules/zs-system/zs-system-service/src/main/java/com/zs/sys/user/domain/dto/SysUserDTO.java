package com.zs.sys.user.domain.dto;

import lombok.Data;

/**
 * @author zsadmin
 */
@Data
public class SysUserDTO {

    private Long sysUserId;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
    private Integer age;
    private Integer sex;
    private String employeeNumber;
    private Integer isAdmin;

}
