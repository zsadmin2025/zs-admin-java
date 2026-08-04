package com.zs.business.warehouse.warehouse.info.domain.excel;

import lombok.Getter;
import lombok.Setter;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
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
@Schema(description = "库房表Excel对象")
@ExcelIgnoreUnannotated
public class BusinessWarehouseInfoExcel {

    @ExcelProperty("主键")
    private Long warehouseInfoId;

    @ExcelProperty("库房编号")
    private String warehouseCode;

    @ExcelProperty("库房名称")
    private String warehouseName;

    @ExcelProperty("库房地址")
    private String warehouseAddress;

    @ExcelProperty("机构id")
    private Long institutionId;

    @ExcelProperty("库房面积")
    private BigDecimal warehouseArea;

    @ExcelProperty("库房类型")
    private Long warehouseType;

    @ExcelProperty("货位数量")
    private Integer locationCount;

    @ExcelProperty("货架数量")
    private Integer shelfCount;

    @ExcelProperty("管理员")
    private Long managerUserId;

    @ExcelProperty("联系方式")
    private String contactInfo;

    @ExcelProperty("状态")
    private Integer status;

    @ExcelProperty("备注")
    private String remark;

}
