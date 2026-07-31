package com.zs.lawyer.cases.infoApprovalForm.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 案件审批表
 * </p>
 *
 * @author zs
 * @since 2025-07-10 07:07:27
 */
@Getter
@Setter
@Schema(description = "案件审批表AParams对象")
public class CaseInfoApprovalFormParams implements Serializable {


    @Schema(description = "")
    private Long caseInfoApprovalFormId;

    @Schema(description = "审批意见")
    private String approvalOpinion;


}
