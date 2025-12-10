package com.zs.gen.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "生成表参数")
@Getter
@Setter
public class GenTableParams {

    @Schema(description = "表ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long tableId;
}
