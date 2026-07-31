package com.zs.lawyer.cases.info.domain.vo;

import com.zs.lawyer.cases.contract.domain.vo.CaseContractVO;
import com.zs.lawyer.cases.customer.domain.vo.CaseCustomerVO;
import com.zs.lawyer.cases.hearing.domain.vo.CaseHearingVO;
import com.zs.lawyer.cases.infoApprovalForm.domain.vo.CaseInfoApprovalFormVO;
import com.zs.lawyer.cases.infoFiles.domain.vo.CaseInfoFilesVO;
import com.zs.lawyer.cases.infoList.domain.vo.CaseInfoListVO;
import com.zs.lawyer.cases.team.domain.vo.CaseTeamVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CaseVO {

    @Schema(description = "客户信息")
    private CaseCustomerVO caseCustomer;

    @Schema(description = "案件信息")
    private CaseInfoVO caseInfo;

    @Schema(description = "案件开庭信息")
    private List<CaseHearingVO> caseHearingList = List.of();;

    @Schema(description = "案件团队信息")
    private CaseTeamVO caseTeam;

    @Schema(description = "案件合同信息")
    private CaseContractVO caseContract;

    @Schema(description = "案件附件信息")
    private List<CaseInfoListVO> caseInfoList = List.of();


    @Schema(description = "案件附件信息")
    private List<CaseInfoFilesVO> caseInfoFilesList = List.of();


    @Schema(description = "案件审批人")
    private List<Long> approvalLawyerList = List.of();

    @Schema(description = "案件审批表")
    private CaseInfoApprovalFormVO caseInfoApprovalForm;
}
