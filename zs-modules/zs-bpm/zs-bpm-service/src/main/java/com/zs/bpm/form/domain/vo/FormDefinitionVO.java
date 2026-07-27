package com.zs.bpm.form.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 表单定义视图对象
 *
 * @author zsadmin
 */
@Data
@Schema(description = "表单定义视图对象")
public class FormDefinitionVO implements Serializable {

    @Schema(description = "表单ID")
    private Long id;

    @Schema(description = "表单名称")
    private String formName;

    @Schema(description = "表单key")
    private String formKey;

    @Schema(description = "表单描述")
    private String description;

    @Schema(description = "状态(0-停用 1-启用)")
    private Integer status;

    @Schema(description = "表单字段配置")
    private String formRule;

    @Schema(description = "表单全局配置")
    private String formOption;

    @Schema(description = "创建时间")
    private String createTime;
}
