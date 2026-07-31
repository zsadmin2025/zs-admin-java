package com.zs.lawyer.cases.team.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaseKeyValueVO {

    @Schema(description = "键")
    private String value;

    @Schema(description = "值")
    private String label;
}
