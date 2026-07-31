package com.zs.lawyer.cases.infoApprove.domain.params;

import com.zs.common.core.page.BasePageParams;
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
@Schema(description = "案件审批ageQueryParams对象")
public class CaseInfoApprovePageQueryParams  extends BasePageParams implements Serializable {

    @Schema(description = "案件审批表ID")
    private Long caseInfoApproveId;

    @Schema(description = "案件表ID")
    private Long caseInfoId;

    @Schema(description = "案件名称")
    private String caseName;

    @Schema(description = "案件编号")
    private String caseNo;

    @Schema(description = "案件类型")
    private String caseType;

    @Schema(description = "案件申请人")
    private String applicantName;

    @Schema(description = "审批状态 0-拒绝 1-审批中 2-通过")
    private Integer approveStatus;

}
