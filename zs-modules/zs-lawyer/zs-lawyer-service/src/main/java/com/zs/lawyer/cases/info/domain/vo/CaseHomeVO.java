package com.zs.lawyer.cases.info.domain.vo;

import com.zs.lawyer.cases.customer.domain.vo.CaseCustomerVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CaseHomeVO {

    @Schema(description = "客户信息")
    private CaseCustomerVO caseCustomer;

    @Schema(description = "案件信息")
    private CaseInfoVO caseInfo;

}
