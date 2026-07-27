package com.zs.bpm.category.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "流程分类新增/修改参数")
public class BpmProcessCategoryUpdateParams {

    @Schema(description = "分类ID(修改时必传)")
    private Long id;
    @Schema(description = "分类名称")
    private String name;
    @Schema(description = "分类编码")
    private String code;
    @Schema(description = "图标")
    private String icon;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "状态(0=禁用,1=启用)")
    private Integer status;
}
