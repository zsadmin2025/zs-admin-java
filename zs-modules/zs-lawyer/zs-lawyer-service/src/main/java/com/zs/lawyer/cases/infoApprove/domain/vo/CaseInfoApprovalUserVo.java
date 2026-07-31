package com.zs.lawyer.cases.infoApprove.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaseInfoApprovalUserVo {

    @Schema(description = "用户id")
    private Long sysUserId;

    @Schema(description = "用户名称")
    private String realName;
}
