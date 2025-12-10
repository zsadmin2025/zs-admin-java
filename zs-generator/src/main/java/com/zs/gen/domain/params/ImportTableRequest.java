package com.zs.gen.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(description = "导入表参数")
@Setter
@Getter
public class ImportTableRequest {

    @Schema(description = "表名称列表")
    private List<String> tables;

}
