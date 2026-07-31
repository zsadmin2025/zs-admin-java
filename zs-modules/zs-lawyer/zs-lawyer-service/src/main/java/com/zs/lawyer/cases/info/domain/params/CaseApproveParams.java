package com.zs.lawyer.cases.info.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "案件信息审批对象")
public class CaseApproveParams {

    private Long caseInfoId;

    private String approvalOpinion;
}
