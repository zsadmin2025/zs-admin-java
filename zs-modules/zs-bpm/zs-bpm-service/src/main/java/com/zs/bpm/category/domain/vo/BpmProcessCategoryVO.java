package com.zs.bpm.category.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "流程分类VO")
public class BpmProcessCategoryVO implements Serializable {
    @Schema(description = "分类ID")
    private Long id;
    @Schema(description = "分类名称")
    private String name;
    @Schema(description = "分类编码")
    private String code;
    @Schema(description = "图标")
    private String icon;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "状态")
    private Integer status;
    @Schema(description = "创建时间")
    private String createTime;
    @Schema(description = "子分类列表")
    private List<BpmProcessCategoryVO> children;
}
