package config.dto;

import lombok.Data;

@Data
public class SysConfigEmailDTO {

    /**
     * 邮件服务器SMTP地址
     */
    private String host;

    /**
     * 邮件服务器SMTP端口
     */
    private Integer port;

    /**
     * 发件人
     */
    private String username;

    /**
     * 密码(授权码)
     */
    private String password;

}
