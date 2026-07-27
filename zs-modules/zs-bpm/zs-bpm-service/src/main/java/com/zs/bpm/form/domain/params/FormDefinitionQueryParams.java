package com.zs.bpm.form.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 表单定义查询参数
 *
 * @author zsadmin
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "表单定义查询参数")
public class FormDefinitionQueryParams extends BasePageParams {

    @Schema(description = "表单名称")
    private String formName;

    @Schema(description = "表单key")
    private String formKey;

    @Schema(description = "状态(0-停用 1-启用)")
    private Integer status;
}
