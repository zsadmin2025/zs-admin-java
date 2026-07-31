package com.zs.lawyer.cases.infoApprove.domain.params;

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
 * @since 2025-06-30 09:04:42
 */
@Getter
@Setter
@Schema(description = "案件审批AddParams对象")
public class CaseInfoApproveAddParams implements Serializable {


    @Schema(description = "案件审批表ID")
    private Long caseInfoApproveId;

    @Schema(description = "案件表ID")
    private Long caseInfoId;

    @Schema(description = "待审批律师")
    private Long approvalLawyer;

    @Schema(description = "审批状态 0-拒绝 1-审批中 2-通过")
    private Integer approveStatus;





}
