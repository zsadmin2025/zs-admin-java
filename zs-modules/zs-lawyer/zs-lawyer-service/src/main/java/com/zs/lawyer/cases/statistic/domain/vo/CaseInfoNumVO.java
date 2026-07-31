package com.zs.lawyer.cases.statistic.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 案件数量统计VO对象
 */
@Setter
@Getter
public class CaseInfoNumVO {

    @Schema(description = "总立项")
    private Integer total = 0;

    @Schema(description = "进行中数量")
    private Integer doingTotal = 0;

    @Schema(description = "已结案数量")
    private Integer closedTotal = 0;

    @Schema(description = "已归档数量")
    private Integer filingTotal = 0;
}
