package com.zs.sys.role.domain.vo;

import com.zs.common.core.model.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author zsadmin
 */
@Schema(name = "角色信息")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleVO extends BaseVO implements Serializable {

    @Schema(name = "角色ID")
    private Long sysRoleId;

    @Schema(name = "角色名称")
    private String roleName;

    @Schema(name = "角色编码")
    private String roleCode;

    @Schema(name = "数据权限,")
    private Integer dataScope;

    @Schema(name = "排序")
    private Integer sort;

    @Schema(name = "状态")
    private Integer status;

    @Schema(name = "备注")
    private String remark;

    @Schema(name = "菜单ID集合")
    private List<Long> menuList;

    @Schema(name = "部门ID集合")
    private List<Long> deptList;
}
