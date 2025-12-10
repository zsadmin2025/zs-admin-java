package com.zs.sys.tenant.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p>
 * 租户用户关联表
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:38
 */
@Getter
@Setter
@TableName("sys_tenant_user")
@Schema(description = "租户用户Entity对象")
public class SysTenantUserEntity extends BaseEntity {

    /**  租户用户ID */
    @TableId
    private Long sysTenantUserId;

    /**  租户ID */
    private Long sysTenantId;

    /**  用户ID */
    private Long userId;

    /**  用户类型（0-普通用户，1-租户管理员） */
    private Long userType;

    /**  加入租户时间 */
    private Date joinTime;

    /**  状态（0-禁用，1-正常） */
    private Integer status;




}
