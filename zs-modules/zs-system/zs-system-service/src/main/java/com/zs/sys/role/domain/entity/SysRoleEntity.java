package com.zs.sys.role.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zsadmin
 */
@Data
@TableName("sys_role")
@EqualsAndHashCode(callSuper = true)
public class SysRoleEntity extends BaseEntity {

    @TableId
    private Long sysRoleId;
    private String roleName;
    private String roleCode;
    private Integer dataScope;
    private Integer type;
    private Integer sort;
    private Integer status;
    private String remark;
}
