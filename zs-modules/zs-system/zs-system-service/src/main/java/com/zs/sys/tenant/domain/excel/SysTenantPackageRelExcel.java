package com.zs.sys.tenant.domain.excel;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
/**
 * <p>
 * $!{table.comment}
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:43
 */
@Getter
@Setter
@Schema(description = "租户-套餐管理Excel对象")
@ExcelIgnoreUnannotated
public class SysTenantPackageRelExcel {

    @ExcelProperty("租户套餐关联ID")
    private Long sysTenantPackageRelId;

    @ExcelProperty("租户ID")
    private Long tenantId;

    @ExcelProperty("套餐ID")
    private Long packageId;

    @ExcelProperty("套餐生效时间")
    private Date startTime;

    @ExcelProperty("套餐到期时间")
    private Date endTime;

    @ExcelProperty("状态（0-已过期，1-生效中）")
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
