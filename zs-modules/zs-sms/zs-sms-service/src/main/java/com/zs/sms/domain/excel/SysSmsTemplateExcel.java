package com.zs.sms.domain.excel;

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
 * @since 2025-11-26 09:40:35
 */
@Getter
@Setter
@Schema(description = "短信模板Excel对象")
@ExcelIgnoreUnannotated
public class SysSmsTemplateExcel {

    @ExcelProperty("")
    private Long sysSmsTemplateId;

    @ExcelProperty("模板编号")
    private String templateNumber;

    @ExcelProperty("短信通道/服务商")
    private Long channel;

    @ExcelProperty("短信签名")
    private String signName;

    @ExcelProperty("短信模板code")
    private String templateCode;

    @ExcelProperty("短信模板内容")
    private String templateContent;

    @ExcelProperty("创建时间")
    private Date createTime;

}
