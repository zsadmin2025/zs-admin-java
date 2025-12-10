package com.zs.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * 登录参数
 */
@Data
@Schema(description = "登录参数")
public class LoginParams  {

    @Schema(description = "租户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "租户ID不能为空")
    private String tenantId;


    @Schema(description = "登录账号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户名不能为空")
    private String username;


    @Schema(description = "登录密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不能为空")
    private String password;


    @Schema(description = "验证码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "验证码不能为空")
    private String code;


    @Schema(description = "uuid", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "uuid不能为空")
    private String uuid;
}
