package com.zs.sys.config.domain.params;

import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysConfigParams extends BaseEntity {

    @Schema(name = "配置项key")
    private String configKey;

    @Schema(name = "配置项vale,json串格式")
    private String configValue;
}
