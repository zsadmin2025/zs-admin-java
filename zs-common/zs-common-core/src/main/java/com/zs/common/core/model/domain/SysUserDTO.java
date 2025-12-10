package com.zs.common.core.model.domain;

import lombok.Data;

/**
 * @author zsadmin
 */
@Data
public class SysUserDTO {

    private Long sysUserId;
    private String username;
    private String realName;
    private String phone;
    private String email;
    private Integer age;
    private Integer sex;
    private Integer isAdmin;
    private Long sysDeptId;
    private Long sysPostId;
    private Integer status;
    private String deptName;
    private String postName;
}
