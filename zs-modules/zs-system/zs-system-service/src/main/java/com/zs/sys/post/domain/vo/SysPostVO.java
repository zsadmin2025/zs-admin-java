package com.zs.sys.post.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zsadmin
 */
@Schema(description = "岗位信息")
@Data
public class SysPostVO implements Serializable {


    @Schema(description = "岗位ID")
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

    @Schema(description = "部门名称")
    private String deptName;
}
