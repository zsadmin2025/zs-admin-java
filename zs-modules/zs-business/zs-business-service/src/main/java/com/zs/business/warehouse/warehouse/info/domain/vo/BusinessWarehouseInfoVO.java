package com.zs.business.warehouse.warehouse.info.domain.vo;

import com.zs.common.core.annotation.DictBind;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Schema(description = "库房表VO对象")
public class BusinessWarehouseInfoVO implements Serializable {

    @Schema(description = "主键")
    private Long warehouseInfoId;

    @Schema(description = "库房编号")
    private String warehouseCode;

    @Schema(description = "库房名称")
    private String warehouseName;

    @Schema(description = "库房地址")
    private String warehouseAddress;

    @Schema(description = "机构id")
    private Long institutionId;

    @Schema(description = "机构名称")
    private String institutionName;

    @Schema(description = "库房面积")
    private BigDecimal warehouseArea;

    @Schema(description = "库房类型")
    private Long warehouseType;

    @Schema(description = "库房类型名称")
    @DictBind(dictCode = "warehouseType", sourceField = "warehouseType", defaultValue = "未知")
    private String warehouseTypeLabel;

    @Schema(description = "货位数量")
    private Integer locationCount;

    @Schema(description = "货架数量")
    private Integer shelfCount;

    @Schema(description = "管理员")
    private Long managerUserId;

    @Schema(description = "管理员名称")
    private String managerUserName;

    @Schema(description = "联系方式")
    private String contactInfo;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

}
