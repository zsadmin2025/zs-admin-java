package com.zs.sms.factory;

import cn.hutool.json.JSONUtil;
import com.zs.common.core.constant.Constants;
import com.zs.common.core.constant.RedisConstants;
import com.zs.common.core.enums.SmsTypeEnum;
import com.zs.common.redis.config.RedisUtil;
import com.zs.sms.strategy.AliyunSendSmsStrategy;
import com.zs.sms.strategy.SendSmsStrategy;
import com.zs.sms.strategy.TencentSendSmsStrategy;
import com.zs.sys.config.domain.vo.SysConfigSmsVO;
import config.SysConfigApi;
import config.dto.SysConfigSmsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 短信发送工厂
 */
@Slf4j
@Component
public class SmsFactory {

    private static RedisUtil redisUtil;
    private static SysConfigApi sysConfigApi;


    public SmsFactory(SysConfigApi sysConfigApi, RedisUtil redisUtil) {
        SmsFactory.sysConfigApi = sysConfigApi;
        SmsFactory.redisUtil = redisUtil;
        initialize();
    }

    public static void initialize() {
        try {
            SysConfigSmsDTO sysConfigSmsDTO = sysConfigApi.smsInfo();
            if (sysConfigSmsDTO != null) {
                redisUtil.setObject(RedisConstants.SYS_DICT_CONFIG_KEY + Constants.SYS_CONFIG_SMS, sysConfigSmsDTO);
            } else {
                log.warn("文件上传配置为空");
            }
        } catch (Exception e) {
            log.error("获取文件上传配置异常：", e);
        }
    }


    public static SendSmsStrategy build() {
        // 获取当前配置文件上传的类型
        Object object = redisUtil.get(RedisConstants.SYS_DICT_CONFIG_KEY + Constants.SYS_CONFIG_SMS);
        SysConfigSmsVO sysConfigSmsVO = JSONUtil.toBean(JSONUtil.toJsonStr(object), SysConfigSmsVO.class);
        SmsTypeEnum smsTypeEnum = SmsTypeEnum.getEnum(sysConfigSmsVO.getType());
        if (smsTypeEnum == null) {
            return new AliyunSendSmsStrategy(sysConfigSmsVO);
        }

        return switch (smsTypeEnum) {
            case ALIYUN -> new AliyunSendSmsStrategy(sysConfigSmsVO);
            case TENCENT -> new TencentSendSmsStrategy(sysConfigSmsVO);
        };
    }
}
