package com.zs.bpm.form.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 表单定义新增参数
 *
 * @author zsadmin
 */
@Data
@Schema(description = "表单定义新增参数")
public class FormDefinitionAddParams {

    @Schema(description = "表单ID")
    private Long id;

    @Schema(description = "表单名称")
    private String formName;

    @Schema(description = "表单描述")
    private String description;

    @Schema(description = "状态(0-停用 1-启用)")
    private Integer status;

    @Schema(description = "表单唯一标识")
    private String formKey;

    @Schema(description = "表单字段配置")
    private String formRule;

    @Schema(description = "表单全局配置")
    private String formOption;
}
