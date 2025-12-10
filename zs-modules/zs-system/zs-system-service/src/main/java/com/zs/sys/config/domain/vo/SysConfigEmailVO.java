package com.zs.sys.config.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *  邮件配置VO
 */
@Schema(description = "邮件配置VO")
@Data
public class SysConfigEmailVO {


    @Schema(description = "邮件服务器SMTP地址")
    private String host;

    @Schema(description = "邮件服务器SMTP端口")
    private Integer port;

    @Schema(description = "发件人")
    private String username;

    @Schema(description = "密码(授权码)")
    private String password;

}
