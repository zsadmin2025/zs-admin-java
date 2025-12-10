package com.zs.common.core.model.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 本地上传
 */
@Schema(description = "本地")
@Data
public class Local {

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "前缀")
    private String prefix;

    @Schema(description = "路径")
    private String path;
}
