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
 * @since 2025-08-07 23:42:38
 */
@Getter
@Setter
@Schema(description = "租户用户Excel对象")
@ExcelIgnoreUnannotated
public class SysTenantUserExcel {

    @ExcelProperty("租户用户ID")
    private Long sysTenantUserId;

    @ExcelProperty("租户ID")
    private Long tenantId;

    @ExcelProperty("用户ID")
    private Long userId;

    @ExcelProperty("用户类型（0-普通用户，1-租户管理员）")
    private Long userType;

    @ExcelProperty("加入租户时间")
    private Date joinTime;

    @ExcelProperty("状态（0-禁用，1-正常）")
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
