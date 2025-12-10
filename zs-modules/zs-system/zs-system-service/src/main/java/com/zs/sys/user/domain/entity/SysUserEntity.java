package com.zs.sys.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zsadmin
 */
@Data
@TableName("sys_user")
@EqualsAndHashCode(callSuper = true)
public class SysUserEntity extends BaseEntity {

    @TableId
    private Long sysUserId;
    private String username;
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
    private Long sysPostId;
    private Integer status;
    private Integer deleted;

    @TableField(exist = false)
    private String deptName;
    @TableField(exist = false)
    private String postName;
}
