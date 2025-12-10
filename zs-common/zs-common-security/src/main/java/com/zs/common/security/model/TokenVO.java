package com.zs.common.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * token信息
 */
@Schema(description = "登录返回信息")
@Data
public class TokenVO {

    @Schema(description = "访问令牌")
    private String accessToken;

    @Schema(description = "刷新令牌")
    private String refreshToken;

}
