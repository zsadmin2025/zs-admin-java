package com.zs.sys.tenant.domain.excel;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
/**
 * <p>
 * $!{table.comment}
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:40
 */
@Getter
@Setter
@Schema(description = "租户套餐Excel对象")
@ExcelIgnoreUnannotated
public class SysTenantPackageExcel {

    @ExcelProperty("租户套餐ID")
    private Long sysTenantPackageId;

    @ExcelProperty("套餐编码")
    private String packageCode;

    @ExcelProperty("套餐名称")
    private String packageName;

    @ExcelProperty("套餐价格")
    private BigDecimal price;

    @ExcelProperty("最大用户数（-1表示无限制）")
    private Integer maxUser;

    @ExcelProperty("最大存储空间(GB，-1表示无限制)")
    private Integer maxStorage;

    @ExcelProperty("包含功能（JSON格式）")
    private String features;

    @ExcelProperty("状态（0-下架，1-正常）")
    private Long status;

    @ExcelProperty("创建者")
    private Long creator;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("更新者")
    private Long updater;

    @ExcelProperty("更新时间")
    private Date updateTime;

}
