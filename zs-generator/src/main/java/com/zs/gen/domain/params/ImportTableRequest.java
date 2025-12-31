package com.zs.gen.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(description = "导入表参数")
@Setter
@Getter
public class ImportTableRequest {

    @Schema(description = "表名称列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "表名称列表不能为空")
    private List<String> tables;

}
