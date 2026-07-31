package com.zs.lawyer.cases.team.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.zs.sys.user.domain.vo.SysUserVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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
@Schema(description = "案件团队VO对象")
public class CaseTeamVO implements Serializable {

    @Schema(description = "")
    private Long caseTeamId;

    @Schema(description = "关联案件信息表id")
    private Long caseInfoId;

    @Schema(description = "承接律师")
    private Long undertakeLawyer;

    @Schema(description = "承接律师名称")
    private SysUserVO undertakeLawyerVo;

    @Schema(description = "协接律师")
    private List<String> coordinatingLawyer;

    @Schema(description = "协接律师对象")
    private List<SysUserVO> coordinatingLawyerVos;

    @Schema(description = "主办律师")
    private List<String> leadLawyer;

    @Schema(description = "主办律师")
    private List<SysUserVO> leadLawyerVos;

    @Schema(description = "协办人员")
    private List<String> coOrganizer;

    @Schema(description = "协办人员")
    private List<SysUserVO> coOrganizerVos;

    @Schema(description = "助理")
    private List<String> assistant;

    @Schema(description = "助理")
    private List<SysUserVO> assistantVos;

    @Schema(description = "秘书")
    private List<String> secretary;

    @Schema(description = "秘书")
    private List<SysUserVO> secretaryVos;

    @Schema(description = "创建者")
    private Long creator;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新者")
    private Long updater;

    @Schema(description = "更新时间")
    private Date updateTime;

}
