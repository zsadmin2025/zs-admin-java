package com.zs.sys.dict.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author zsadmin
 */
@Schema(description = "字典数据新增参数")
@Data
public class SysDictDataAddParams {


    @Schema(description = "字典类型")
    @NotNull(message = "字典类型不能为空")
    private String dictType;

    @Schema(description = "字典类型id")
    @NotNull(message = "字典类型id不能为空")
    private Long sysDictTypeId;

    @Schema(description = "上级字典数据id")
    @NotNull(message = "上级字典数据id不能为空")
    private Long pid;

    @Schema(description = "字典数据名称")
    @NotNull(message = "字典数据名称不能为空")
    private String dictLabel;

    @Schema(description = "字典数据值")
    @NotNull(message = "字典数据值不能为空")
    private String dictValue;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "字典数据状态")
    private Integer status;
}
