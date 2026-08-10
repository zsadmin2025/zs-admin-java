package com.zs.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * token 刷新参数
 */
@Data
@Schema(description = "token刷新参数")
public class RefreshTokenParams {

    @Schema(description = "刷新令牌", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "refreshToken不能为空")
    private String refreshToken;

}
