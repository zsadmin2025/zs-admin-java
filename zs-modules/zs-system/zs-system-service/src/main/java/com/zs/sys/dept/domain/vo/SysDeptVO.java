package com.zs.sys.dept.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zsadmin
 */
@Schema(description = "部门信息")
@Data
public class SysDeptVO implements Serializable {

    @Schema(description = "部门ID")
    private Long sysDeptId;

    @Schema(description = "上级部门ID")
    private Long pid;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "部门描述")
    private String remark;

    @Schema(description = "部门负责人ID")
    private Long deptHeadId;

    @Schema(description = "部门负责人名称")
    private String deptHeadName;

    @Schema(description = "部门状态")
    private Integer status;

    @Schema(description = "排序")
    private Integer sort;

}
