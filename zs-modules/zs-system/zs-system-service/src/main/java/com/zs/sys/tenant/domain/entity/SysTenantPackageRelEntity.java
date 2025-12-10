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
 * 租户套餐关联表
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:43
 */
@Getter
@Setter
@TableName("sys_tenant_package_rel")
@Schema(description = "租户-套餐管理Entity对象")
public class SysTenantPackageRelEntity extends BaseEntity {

    /**  租户套餐关联ID */
    @TableId
    private Long sysTenantPackageRelId;

    /**  租户ID */
    private Long sysTenantId;

    /**  套餐ID */
    private Long packageId;

    /**  套餐生效时间 */
    private Date startTime;

    /**  套餐到期时间 */
    private Date endTime;

    /**  状态（0-已过期，1-生效中） */
    private Integer status;




}
