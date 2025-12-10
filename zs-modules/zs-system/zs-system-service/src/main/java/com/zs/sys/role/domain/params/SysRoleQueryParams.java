package com.zs.sys.role.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zsadmin
 */
@Schema(description = "角色查询参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleQueryParams extends BasePageParams {

    @Schema(description = "角色ID")
    private Long sysRoleId;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "角色名称")
    private String roleName;
}
