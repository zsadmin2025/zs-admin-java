package com.zs.bpm.model.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程定义信息查询参数
 *
 * @author zsadmin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "流程定义信息查询参数")
public class BpmProcessDefinitionInfoPageQueryParams extends BasePageParams {

    @Schema(description = "流程名称")
    private String processName;

    @Schema(description = "流程Key")
    private String processKey;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "状态")
    private Integer status;

}
