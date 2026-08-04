package com.zs.business.warehouse.warehouse.info.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

import java.math.BigDecimal;

/**
 * <p>
 * 库房表
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-04 11:38:39
 */
@Getter
@Setter
@TableName("business_warehouse_info")
@Schema(description = "库房表Entity对象")
public class BusinessWarehouseInfoEntity extends BaseEntity {

    /**  主键 */
    @TableId
    private Long warehouseInfoId;

    /**  库房编号 */
    private String warehouseCode;

    /**  库房名称 */
    private String warehouseName;

    /**  库房地址 */
    private String warehouseAddress;

    /**  机构id */
    private Long institutionId;

    /**  库房面积 */
    private BigDecimal warehouseArea;

    /**  库房类型 */
    private Long warehouseType;

    /**  货位数量 */
    private Integer locationCount;

    /**  货架数量 */
    private Integer shelfCount;

    /**  管理员 */
    private Long managerUserId;

    /**  联系方式 */
    private String contactInfo;

    /**  状态 */
    private Integer status;

    /**  备注 */
    private String remark;

}
