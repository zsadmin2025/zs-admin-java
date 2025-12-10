package com.zs.sys.dict.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * @author zsadmin
 */
@Schema(description = "字典类型-新增")
@Data
public class SysDictTypeAddParams {

    @Schema(description = "字典类型id")
    private Long sysDictTypeId;

    @Schema(description = "字典类型")
    @NotNull(message = "字典类型不能为空")
    private String dictType;

    @Schema(description = "字典名称")
    @NotNull(message = "字典名称不能为空")
    private String dictName;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态")
    private Integer status;


}