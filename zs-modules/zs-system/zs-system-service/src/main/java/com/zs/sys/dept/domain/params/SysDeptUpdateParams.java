package com.zs.sys.dept.domain.params;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author zsadmin
 */
@Data
public class SysDeptUpdateParams {

    @TableId
    private Long sysDeptId;

    @NotNull(message = "上级部门id不能为空")
    @Schema(description = "上级部门id")
    private Long pid;

    @NotBlank(message = "部门名称不能为空")
    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "部门负责人id")
    private Long deptHeadId;

    @Schema(description = "部门负责人名称")
    private String deptHeadName;

    @Schema(description = "部门描述")
    private String describe;

    @Schema(description = "部门状态")
    private Integer status;

    @Schema(description = "排序")
    private Integer sort;
}
