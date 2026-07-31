package com.zs.lawyer.cases.infoList.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 案件结案目录表
 * </p>
 *
 * @author zs
 * @since 2025-06-21 12:20:27
 */
@Getter
@Setter
@Schema(description = "案件结案目录electQueryParams对象")
public class CaseInfoListSelectQueryParams implements Serializable {

    @Schema(description = "案件结案目录表ID")
    private Long caseInfoListId;

    @Schema(description = "案件信息表ID")
    private Long caseInfoId;

    @Schema(description = "结案目录基础表ID")
    private Long caseListId;

}
