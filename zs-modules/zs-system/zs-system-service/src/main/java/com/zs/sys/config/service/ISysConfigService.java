package com.zs.sys.config.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.model.file.SysConfigFileVO;
import com.zs.sys.config.domain.entity.SysConfigEntity;
import com.zs.sys.config.domain.params.SysConfigParams;
import com.zs.sys.config.domain.vo.*;

public interface ISysConfigService extends IService<SysConfigEntity> {

    void update(SysConfigParams sysConfigParams);

    /** 网站配置信息 */
    SysConfigWebsiteVO websiteInfo();

    /** 文件上传配置信息 */
    SysConfigFileVO fileUploadInfo();

    /** 短信配置信息 */
    SysConfigSmsVO smsInfo();

    /** 邮箱配置信息 */
    SysConfigEmailVO emailInfo();

    /** 支付配置信息 */
    SysConfigPayVO payInfo();

    /** 其他配置信息 */
    SysConfigOtherVO otherInfo();
}
