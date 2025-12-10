package com.zs.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "验证码返回参数")
@Data
public class CodeVO {

    @Schema(description = "图片base64", example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...")
    private String img;

    @Schema(description = "uuid", example = "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8")
    private String uuid;
}
