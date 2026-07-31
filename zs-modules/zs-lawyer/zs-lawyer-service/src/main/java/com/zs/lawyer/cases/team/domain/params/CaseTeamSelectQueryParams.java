package com.zs.lawyer.cases.team.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 案件团队
 * </p>
 *
 * @author zs
 * @since 2025-06-08 18:01:20
 */
@Getter
@Setter
@Schema(description = "案件团队electQueryParams对象")
public class CaseTeamSelectQueryParams implements Serializable {

    @Schema(description = "")
    private Long caseTeamId;

    @Schema(description = "关联案件信息表id")
    private Long caseInfoId;

    @Schema(description = "承接律师")
    private Long undertakeLawyer;

    @Schema(description = "协接律师")
    private Long coordinatingLawyer;

    @Schema(description = "主办律师")
    private Long leadLawyer;

    @Schema(description = "协办人员")
    private Long coOrganizer;

    @Schema(description = "助理")
    private Long assistant;

    @Schema(description = "秘书")
    private Long secretary;

}
