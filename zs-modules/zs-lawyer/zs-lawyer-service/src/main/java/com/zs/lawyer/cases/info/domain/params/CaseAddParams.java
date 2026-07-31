package com.zs.lawyer.cases.info.domain.params;

import com.zs.lawyer.cases.contract.domain.params.CaseContractAddParams;
import com.zs.lawyer.cases.customer.domain.params.CaseCustomerAddParams;
import com.zs.lawyer.cases.hearing.domain.params.CaseHearingAddParams;
import com.zs.lawyer.cases.team.domain.params.CaseTeamAddParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Schema(description = "案件信息AddParams对象")
public class CaseAddParams implements Serializable {

    @Schema(description = "客户信息")
    private CaseCustomerAddParams  caseCustomer;

    @Schema(description = "案件信息")
    private CaseInfoAddParams  caseInfo;

    @Schema(description = "案件开庭信息")
    private List<CaseHearingAddParams> caseHearingList;

    @Schema(description = "案件团队信息")
    private CaseTeamAddParams caseTeam;

    @Schema(description = "案件合同信息")
    private CaseContractAddParams caseContract;

    @Schema(description = "案件审批人")
    private List<Long> approvalLawyerList = List.of();
}
