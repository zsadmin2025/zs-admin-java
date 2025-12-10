package com.zs.sys.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zsadmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user_role")
@Schema(description = "用户角色关联表")
public class SysUserRoleEntity extends BaseEntity {

    @TableId
    private Long sysUserRoleId;
    private Long sysUserId;
    private Long sysRoleId;
}
