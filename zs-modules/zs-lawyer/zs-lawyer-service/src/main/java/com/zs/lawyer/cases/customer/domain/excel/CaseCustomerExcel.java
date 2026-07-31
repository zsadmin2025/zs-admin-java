package com.zs.lawyer.cases.customer.domain.excel;

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
 * @since 2025-06-08 17:55:28
 */
@Getter
@Setter
@Schema(description = "案件客户信息Excel对象")
@ExcelIgnoreUnannotated
public class CaseCustomerExcel {

    @ExcelProperty("表id")
    private Long caseCustomerId;

    @ExcelProperty("案件id")
    private Long caseInfoId;

    @ExcelProperty("客户状态")
    private String customerType;

    @ExcelProperty("创建者")
    private Long creator;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("更新者")
    private Long updater;

    @ExcelProperty("更新时间")
    private Date updateTime;

}
