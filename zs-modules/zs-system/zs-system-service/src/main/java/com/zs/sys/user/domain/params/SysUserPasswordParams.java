package com.zs.sys.user.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author zsadmin
 */
@Schema(description = "用户密码修改参数")
@Data
public class SysUserPasswordParams {

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long sysUserId;

    @Schema(description = "旧密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    @Schema(description = "新密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不能为空")
    private String confirmPassword;
}
