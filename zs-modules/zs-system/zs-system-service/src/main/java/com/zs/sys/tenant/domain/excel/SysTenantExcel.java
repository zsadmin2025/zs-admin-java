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
 * @since 2025-08-07 23:42:45
 */
@Getter
@Setter
@Schema(description = "租户管理Excel对象")
@ExcelIgnoreUnannotated
public class SysTenantExcel {

    @ExcelProperty("租户ID")
    private Long sysTenantId;

    @ExcelProperty("租户名称")
    private String tenantName;

    @ExcelProperty("联系人")
    private String contactPerson;

    @ExcelProperty("联系电话")
    private String contactPhone;

    @ExcelProperty("联系邮箱")
    private String contactEmail;

    @ExcelProperty("状态（0-禁用，1-正常）")
    private Long status;

    @ExcelProperty("过期时间")
    private Date expireTime;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建者")
    private Long creator;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("更新者")
    private Long updater;

    @ExcelProperty("更新时间")
    private Date updateTime;

}
