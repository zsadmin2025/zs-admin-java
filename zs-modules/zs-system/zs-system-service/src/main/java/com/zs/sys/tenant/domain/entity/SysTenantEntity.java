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
 * 租户信息表
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:45
 */
@Getter
@Setter
@TableName("sys_tenant")
@Schema(description = "租户管理Entity对象")
public class SysTenantEntity extends BaseEntity {

    /**  租户ID */
    @TableId
    private Long sysTenantId;

    /**  租户名称 */
    private String tenantName;

    /**  联系人 */
    private String contactPerson;

    /**  联系电话 */
    private String contactPhone;

    /**  联系邮箱 */
    private String contactEmail;

    /**  用户ID */
    private Long sysUserId;

    /** 租户类型  */
    private Integer type;

    /**  状态（0-禁用，1-正常） */
    private Integer status;

    /**  过期时间 */
    private Date expireTime;

    /**  备注 */
    private String remark;

    /**  租户套餐ID */
    private Long sysTenantPackageId;



}
