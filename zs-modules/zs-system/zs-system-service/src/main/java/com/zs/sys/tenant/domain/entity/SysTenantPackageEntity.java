package com.zs.sys.tenant.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * <p>
 * 租户套餐表
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:40
 */
@Getter
@Setter
@TableName("sys_tenant_package")
@Schema(description = "租户套餐Entity对象")
public class SysTenantPackageEntity extends BaseEntity {

    /**  租户套餐ID */
    @TableId
    private Long sysTenantPackageId;

    /**  套餐编码 */
    private String packageCode;

    /**  套餐名称 */
    private String packageName;

    /**  套餐价格 */
    private BigDecimal price;

    /**  最大用户数（-1表示无限制） */
    private Integer maxUser;

    /**  最大存储空间(GB，-1表示无限制) */
    private Integer maxStorage;

    /**  状态（0-下架，1-正常） */
    private Integer status;




}
