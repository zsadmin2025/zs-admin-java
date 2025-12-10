package com.zs.sms.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p>
 * 短信记录表
 * </p>
 *
 * @author zs
 * @since 2025-11-25 22:20:34
 */
@Getter
@Setter
@TableName("sys_sms_record")
@Schema(description = "短信记录Entity对象")
public class SysSmsRecordEntity extends BaseEntity {

    /**  表ID */
    @TableId
    private Long sysSmsRecordId;

    /**  模板编号 */
    private String templateNumber;

    /**  请求ID */
    private String requestId;

    /**  回执Id */
    private String bizId;

    /**  接收短信手机号 */
    private String phoneNumbers;

    /**  短信内容 */
    private String content;

    /**  短信模板ID */
    private String templateCode;

    /**  模板变量，如 {"code": "123456"} */
    private String templateParams;

    /**  短信通道/服务商 */
    private Long channel;

    /**  发送状态：0-待发送，1-发送中，2-成功，3-失败，4-取消 */
    private Long status;

    /**  发送时间 */
    private Date sendTime;






}
