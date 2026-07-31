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
@Schema(description = "案件审批表electQueryParams对象")
public class CaseInfoApprovalFormSelectQueryParams implements Serializable {

    @Schema(description = "")
    private Long caseInfoApprovalFormId;

    @Schema(description = "案件表id")
    private Long caseInfoId;

    @Schema(description = "案件编号")
    private String caseNo;

    @Schema(description = "案件类型")
    private String caseType;
}
