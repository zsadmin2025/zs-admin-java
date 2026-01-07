package com.zs.sys.post.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author zsadmin
 */
@Schema(description = "岗位信息")
@Data
public class SysPostAddParams {

    @Schema(description = "岗位ID")
    @NotBlank(message = "岗位ID不能为空")
    private Long sysPostId;

    @Schema(description = "岗位名称")
    private String postName;

    @Schema(description = "部门ID")
    private Long sysDeptId;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态:0禁用，1启用")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}
