package com.zs.sys.post.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author zsadmin
 */
@Schema(description = "岗位查询参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class SysPostQueryParams extends BasePageParams implements Serializable {

    @Schema(description = "岗位名称")
    private String postName;

    @Schema(description = "部门ID")
    private Long sysDeptId;

    @Schema(description = "状态:1启用，0禁用")
    private Integer status;
}
