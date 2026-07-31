package com.zs.lawyer.cases.contract.domain.excel;

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
 * @since 2025-06-08 18:02:46
 */
@Getter
@Setter
@Schema(description = "案件合同Excel对象")
@ExcelIgnoreUnannotated
public class CaseContractExcel {

    @ExcelProperty("案件合同表id")
    private Long caseContractId;

    @ExcelProperty("案件表id")
    private Long caseInfoId;

    @ExcelProperty("开始日期")
    private Date startDate;

    @ExcelProperty("结束日期")
    private Date endDate;

    @ExcelProperty("合同金额")
    private BigDecimal contractAmount;

    @ExcelProperty("付款方式")
    private String paymentMethod;

    @ExcelProperty("付款方式明细")
    private String paymentMethodDetails;

    @ExcelProperty("创建者")
    private Long creator;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("更新者")
    private Long updater;

    @ExcelProperty("更新时间")
    private Date updateTime;

}
