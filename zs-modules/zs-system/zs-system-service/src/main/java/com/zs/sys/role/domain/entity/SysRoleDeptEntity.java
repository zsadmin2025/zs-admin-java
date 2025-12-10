package com.zs.sys.role.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author zsadmin
 */
@Data
@TableName("sys_role_dept")
public class SysRoleDeptEntity{

    @TableId
    private Long sysRoleDeptId;
    private Long sysRoleId;
    private Long sysDeptId;

}
