package com.zs.common.core.model.email;

import lombok.Data;

/**
 * 邮件配置VO
 */
@Data
public class SysConfigEmailVO {

    private String host;

    private Integer port;

    private String username;

    private String password;

    private String from;
}
