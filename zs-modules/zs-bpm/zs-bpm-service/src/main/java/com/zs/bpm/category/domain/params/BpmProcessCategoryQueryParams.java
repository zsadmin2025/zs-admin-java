package com.zs.bpm.category.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "流程分类查询参数")
public class BpmProcessCategoryQueryParams extends BasePageParams {
    @Schema(description = "分类名称")
    private String name;
    @Schema(description = "分类编码")
    private String code;
    @Schema(description = "状态")
    private Integer status;
}
