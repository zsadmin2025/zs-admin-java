package com.zs.sms.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 短信模板表
 * </p>
 *
 * @author zs
 * @since 2025-11-26 09:40:35
 */
@Getter
@Setter
@TableName("sys_sms_template")
@Schema(description = "短信模板Entity对象")
public class SysSmsTemplateEntity extends BaseEntity {

    /**   */
    @TableId
    private Long sysSmsTemplateId;

    /**  模板编号 */
    private String templateNumber;

    /**  短信通道/服务商 */
    private Long channel;

    /**  短信签名 */
    private String signName;

    /**  短信模板code */
    private String templateCode;

    /**  短信模板内容 */
    private String templateContent;

}
