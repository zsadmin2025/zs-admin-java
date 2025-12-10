package config;

import config.dto.SysConfigEmailDTO;
import config.dto.SysConfigFileDTO;
import config.dto.SysConfigSmsDTO;

public interface SysConfigApi {

    /**
     * 获取邮件配置信息
     *
     * @return 邮件配置信息
     */
    SysConfigEmailDTO emailInfo();

    /**
     * 获取文件上传配置信息
     *
     * @return 文件上传配置信息
     */
    SysConfigFileDTO fileUploadInfo();

    /**
     * 获取短信配置信息
     *
     * @return 短信配置信息
     */
    SysConfigSmsDTO smsInfo();

}
