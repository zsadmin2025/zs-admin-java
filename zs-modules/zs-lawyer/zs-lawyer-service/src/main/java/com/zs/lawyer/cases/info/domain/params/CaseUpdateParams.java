package com.zs.lawyer.cases.info.domain.params;

import com.zs.lawyer.cases.contract.domain.params.CaseContractUpdateParams;
import com.zs.lawyer.cases.customer.domain.params.CaseCustomerUpdateParams;
import com.zs.lawyer.cases.hearing.domain.params.CaseHearingUpdateParams;
import com.zs.lawyer.cases.team.domain.params.CaseTeamUpdateParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Schema(description = "案件信息UpdateParams对象")
public class CaseUpdateParams implements Serializable {

    @Schema(description = "客户信息")
    private CaseCustomerUpdateParams caseCustomer;

    @Schema(description = "案件信息")
    private CaseInfoUpdateParams  caseInfo;

    @Schema(description = "案件开庭信息")
    private List<CaseHearingUpdateParams> caseHearingList;

    @Schema(description = "案件团队信息")
    private CaseTeamUpdateParams caseTeam;

    @Schema(description = "案件合同信息")
    private CaseContractUpdateParams caseContract;

    @Schema(description = "案件审批人")
    private List<Long> approvalLawyerList = List.of();
}
