package com.zs.sys.config.api;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zs.common.core.constant.SysConfigConstants;
import com.zs.sys.config.domain.entity.SysConfigEntity;
import com.zs.sys.config.service.ISysConfigService;
import config.SysConfigApi;
import config.dto.SysConfigEmailDTO;
import config.dto.SysConfigFileDTO;
import config.dto.SysConfigSmsDTO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SysConfigApiImpl implements SysConfigApi {

    @Resource
    private ISysConfigService iSysConfigService;


    @Override
    public SysConfigEmailDTO emailInfo() {
        return convertToVO(SysConfigConstants.SYS_CONFIG_EMAIL, SysConfigEmailDTO.class);
    }

    @Override
    public SysConfigFileDTO fileUploadInfo() {
        return convertToVO(SysConfigConstants.SYS_CONFIG_FILE, SysConfigFileDTO.class);
    }

    @Override
    public SysConfigSmsDTO smsInfo() {
        return convertToVO(SysConfigConstants.SYS_CONFIG_SMS, SysConfigSmsDTO.class);
    }

    private SysConfigEntity fetchConfigEntity(String configKey) {
        return iSysConfigService.getOne(new LambdaQueryWrapper<SysConfigEntity>().eq(SysConfigEntity::getConfigKey, configKey));
    }

    private <T> T convertToVO(String configKey, Class<T> voClass) {
        SysConfigEntity sysConfigEntity = fetchConfigEntity(configKey);
        if (sysConfigEntity == null || StringUtils.isEmpty(sysConfigEntity.getConfigValue())) {
            return null;
        }
        return JSONUtil.toBean(sysConfigEntity.getConfigValue(), voClass);
    }
}
