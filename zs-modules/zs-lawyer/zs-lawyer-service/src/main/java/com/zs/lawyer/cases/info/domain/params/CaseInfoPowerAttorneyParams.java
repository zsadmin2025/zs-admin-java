package com.zs.lawyer.cases.info.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaseInfoPowerAttorneyParams {

    @Schema(description = "案件ID")
    @NotBlank(message = "案件ID不能为空")
    private Long caseInfoId;

    @Schema(description = "案件委托书")
    @NotBlank(message = "案件委托书不能为空")
    private String powerAttorney;
}
