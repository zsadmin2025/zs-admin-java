package com.zs.sys.role.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author zsadmin
 */
@Schema(description = "角色添加参数")
@Data
public class SysRoleAddParams {

    @Schema(description = "角色ID")
    private Long sysRoleId;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "数据权限")
    private Integer dataScope;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "角色对应的菜单id集合")
    private List<Long> menuList;

    @Schema(description = "角色对应的自定义数据权限部门id集合")
     private List<Long> deptList;
}
