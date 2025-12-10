package com.zs.file.factory;

import cn.hutool.json.JSONUtil;
import com.zs.common.core.constant.Constants;
import com.zs.common.core.constant.RedisConstants;
import com.zs.common.core.enums.UploadTypeEnum;
import com.zs.common.core.model.file.SysConfigFileVO;
import com.zs.common.redis.config.RedisUtil;
import com.zs.file.strategy.*;
import config.SysConfigApi;
import config.dto.SysConfigFileDTO;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 文件上传工厂
 */
@Component
public class FileFactory {

    private static final Logger logger = LoggerFactory.getLogger(FileFactory.class);

    private static RedisUtil redisUtil;
    private static SysConfigApi sysConfigApi;

    public FileFactory(SysConfigApi sysConfigApi, RedisUtil redisUtil) {
        FileFactory.sysConfigApi = sysConfigApi;
        FileFactory.redisUtil = redisUtil;
        initialize();
    }



    private void initialize() {
        try {
            SysConfigFileDTO sysConfigFileVO = sysConfigApi.fileUploadInfo();
            if (sysConfigFileVO != null) {
                redisUtil.setObject(RedisConstants.SYS_DICT_CONFIG_KEY + Constants.FILE_UPLOAD, sysConfigFileVO);
            } else {
                logger.warn("文件上传配置为空");
            }
        } catch (Exception e) {
            logger.error("获取文件上传配置异常：", e);
        }
    }


    @NotNull
    public static UploadStrategy build() {
        // 获取当前配置文件上传的类型
        Object object = redisUtil.get(RedisConstants.SYS_DICT_CONFIG_KEY + Constants.FILE_UPLOAD);
        SysConfigFileVO sysConfigFileVO = JSONUtil.toBean(JSONUtil.toJsonStr(object), SysConfigFileVO.class);
        UploadTypeEnum uploadTypeEnum = UploadTypeEnum.getEnum(sysConfigFileVO.getType());
        if (uploadTypeEnum == null) {
            return new LocalFileStrategy(sysConfigFileVO);
        }

        return switch (uploadTypeEnum) {
            case LOCAL -> new LocalFileStrategy(sysConfigFileVO);
            case TENCENT -> new TencentCloudStrategy(sysConfigFileVO);
            case ALIYUN -> new AliyunCloudStrategy(sysConfigFileVO);
            case HUAWEI -> new HuaweiCloudStrategy(sysConfigFileVO);
        };
    }


}
