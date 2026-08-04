package com.zs.business.warehouse.warehouse.info.domain.params;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
@Schema(description = "库房表AddParams对象")
public class BusinessWarehouseInfoAddParams implements Serializable {


    @Schema(description = "库房编号")
    @Size(max = 100, message = "库房编号长度不能超过100")
    private String warehouseCode;

    @Schema(description = "库房名称")
    @Size(max = 100, message = "库房名称长度不能超过100")
    private String warehouseName;

    @Schema(description = "库房地址")
    @Size(max = 255, message = "库房地址长度不能超过255")
    private String warehouseAddress;

    @Schema(description = "机构id")
    private Long institutionId;

    @Schema(description = "库房面积")
    private BigDecimal warehouseArea;

    @Schema(description = "库房类型")
    private Long warehouseType;

    @Schema(description = "货位数量")
    private Integer locationCount;

    @Schema(description = "货架数量")
    private Integer shelfCount;

    @Schema(description = "管理员")
    private Long managerUserId;

    @Schema(description = "联系方式")
    @Size(max = 20, message = "联系方式长度不能超过20")
    private String contactInfo;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "备注")
    @Size(max = 255, message = "备注长度不能超过255")
    private String remark;








}
