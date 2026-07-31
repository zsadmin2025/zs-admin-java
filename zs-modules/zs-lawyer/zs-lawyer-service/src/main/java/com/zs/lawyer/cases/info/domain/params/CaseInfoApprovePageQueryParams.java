package com.zs.lawyer.cases.info.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 案件信息表
 * </p>
 *
 * @author zs
 * @since 2025-06-08 16:43:20
 */
@Getter
@Setter
@Schema(description = "案件信息ApprovePageQueryParams对象")
public class CaseInfoApprovePageQueryParams extends BasePageParams implements Serializable {

    @Schema(description = "审批律师")
    private Long approvalLawyer;

    @Schema(description = "案件名称")
    private String caseName;

    @Schema(description = "案件编号")
    private String caseNo;

    @Schema(description = "案件类型")
    private String caseType;

    @Schema(description = "案件状态")
    private Integer approveStatus;

}
