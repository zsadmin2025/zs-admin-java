package com.zs.business.warehouse.warehouse.info.domain.params;

import com.zs.common.core.page.BasePageParams;
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
@Schema(description = "库房表ageQueryParams对象")
public class BusinessWarehouseInfoPageQueryParams  extends BasePageParams implements Serializable {

    @Schema(description = "库房编号")
    private String warehouseCode;

    @Schema(description = "库房名称")
    private String warehouseName;

    @Schema(description = "库房地址")
    private String warehouseAddress;

    @Schema(description = "库房类型")
    private Long warehouseType;

    @Schema(description = "状态")
    private Integer status;

}
