package com.zs.sys.menu.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author zsadmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "菜单查询参数")
public class SysMenuQueryParams extends BasePageParams implements Serializable {

    @Schema(description = "菜单标题")
    private String title;

}
