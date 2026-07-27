package com.zs.bpm.model.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "流程定义信息查询参数")
@Data
public class BpmProcessDefinitionInfoQueryParams {

    @Schema(description = "流程名称")
    private String processName;

    @Schema(description = "流程Key")
    private String processKey;

    @Schema(description = "分类ID")
    private Long categoryId;

}
