package com.zs.lawyer.cases.info.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "案件信息-状态参数")
public class CaseInfoStatusParams {

    @Schema(description = "案件ID")
    private Long caseInfoId;
}
