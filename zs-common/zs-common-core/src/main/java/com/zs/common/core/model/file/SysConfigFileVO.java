package com.zs.common.core.model.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文件配置VO
 */
@Schema(description = "文件配置VO")
@Data
public class SysConfigFileVO {

    @Schema(description = "文件上传类型")
    private Integer type;

    @Schema(description = "本地")
    private Local local;

    @Schema(description = "阿里云")
    private Aliyun aliyun;

    @Schema(description = "腾讯云")
    private Tencent tencent;

    @Schema(description = "华为云")
    private Huawei huawei;


}
