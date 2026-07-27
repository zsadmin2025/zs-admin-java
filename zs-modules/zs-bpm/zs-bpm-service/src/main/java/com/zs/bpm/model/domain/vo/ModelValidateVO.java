package com.zs.bpm.model.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "模型校验返回VO")
public class ModelValidateVO {
    @Schema(description = "是否合法")
    private Boolean valid;
    @Schema(description = "错误信息列表")
    private List<String> errors;
}
