package com.zs.sms.domain.excel;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import com.zs.common.core.model.BaseEntity;
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
 * @since 2025-11-25 22:20:34
 */
@Getter
@Setter
@Schema(description = "短信记录Excel对象")
@ExcelIgnoreUnannotated
public class SysSmsRecordExcel extends BaseEntity {

    @ExcelProperty("表ID")
    private Long sysSmsRecordId;

    @ExcelProperty("请求ID")
    private String requestId;

    @ExcelProperty("回执Id")
    private String bizId;

    @ExcelProperty("接收短信手机号")
    private String phoneNumbers;

    @ExcelProperty("短信内容")
    private String content;

    @ExcelProperty("短信模板ID")
    private String templateCode;

    @ExcelProperty("模板变量")
    private String templateParams;

    @ExcelProperty("短信通道/服务商")
    private Long channel;

    private Long status;

    @ExcelProperty("发送时间")
    private Date sendTime;


}
