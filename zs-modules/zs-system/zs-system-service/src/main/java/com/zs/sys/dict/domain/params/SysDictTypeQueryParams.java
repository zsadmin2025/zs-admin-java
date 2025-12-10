package com.zs.sys.dict.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zsadmin
 */
@Schema(description = "字典类型查询参数")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictTypeQueryParams extends BasePageParams {

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "状态")
    private Integer status;
}
