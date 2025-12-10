package com.zs.sms.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 短信记录表
 * </p>
 *
 * @author zs
 * @since 2025-11-25 22:20:34
 */
@Getter
@Setter
@Schema(description = "短信记录AddParams对象")
public class SysSmsParams implements Serializable {


    @Schema(description = "接收短信手机号")
    @NotNull(message = "接收短信手机号不能为空")
    private List<String> phoneNumbers;

    @Schema(description = "模板编号")
    @NotBlank(message = "模板编号不能为空")
    private String templateNumber;

    @Schema(description = "模板参数")
    @NotBlank(message = "模板参数不能为空")
    private String templateParam;

}
