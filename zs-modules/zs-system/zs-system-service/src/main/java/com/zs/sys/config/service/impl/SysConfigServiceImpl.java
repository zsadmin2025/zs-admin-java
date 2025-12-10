package com.zs.sys.config.service.impl;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.constant.Constants;
import com.zs.common.core.constant.RedisConstants;
import com.zs.common.core.constant.SysConfigConstants;
import com.zs.common.core.model.file.SysConfigFileVO;
import com.zs.common.redis.config.RedisUtil;
import com.zs.sys.config.domain.entity.SysConfigEntity;
import com.zs.sys.config.domain.params.SysConfigParams;
import com.zs.sys.config.domain.vo.*;
import com.zs.sys.config.mapper.SysConfigMapper;
import com.zs.sys.config.service.ISysConfigService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfigEntity> implements ISysConfigService {

    @Resource
    private RedisUtil redisUtil;

    private SysConfigEntity fetchConfigEntity(String configKey) {
        return this.baseMapper.selectOne(new LambdaQueryWrapper<SysConfigEntity>()
                .eq(SysConfigEntity::getConfigKey, configKey));
    }

    private <T> T convertToVO(String configKey, Class<T> voClass) {
        SysConfigEntity sysConfigEntity = fetchConfigEntity(configKey);
        if (sysConfigEntity == null || StringUtils.isEmpty(sysConfigEntity.getConfigValue())) {
            return null;
        }
        return JSONUtil.toBean(sysConfigEntity.getConfigValue(), voClass);
    }

    @Override
    public void update(SysConfigParams sysConfigParams) {
        this.baseMapper.update(
                new LambdaUpdateWrapper<SysConfigEntity>()
                        .set(SysConfigEntity::getConfigValue, sysConfigParams.getConfigValue())
                        .eq(SysConfigEntity::getConfigKey, sysConfigParams.getConfigKey()));

        SysConfigFileVO sysConfigFileVO = convertToVO(SysConfigConstants.SYS_CONFIG_FILE, SysConfigFileVO.class);
        redisUtil.setObject(RedisConstants.SYS_DICT_CONFIG_KEY + Constants.FILE_UPLOAD, sysConfigFileVO);
    }

    @Override
    public SysConfigWebsiteVO websiteInfo() {
        return convertToVO(SysConfigConstants.SYS_CONFIG_WEBSITE, SysConfigWebsiteVO.class);
    }

    @Override
    public SysConfigFileVO fileUploadInfo() {
        return convertToVO(SysConfigConstants.SYS_CONFIG_FILE, SysConfigFileVO.class);
    }

    @Override
    public SysConfigSmsVO smsInfo() {
        return convertToVO(SysConfigConstants.SYS_CONFIG_SMS, SysConfigSmsVO.class);
    }

    @Override
    public SysConfigEmailVO emailInfo() {
        return convertToVO(SysConfigConstants.SYS_CONFIG_EMAIL, SysConfigEmailVO.class);
    }

    @Override
    public SysConfigPayVO payInfo() {
        return convertToVO(SysConfigConstants.SYS_CONFIG_PAY, SysConfigPayVO.class);
    }

    @Override
    public SysConfigOtherVO otherInfo() {
        return convertToVO(SysConfigConstants.SYS_CONFIG_OTHER, SysConfigOtherVO.class);
    }
}
