package com.zs.lawyer.cases.hearing.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zs.lawyer.cases.infoFiles.domain.vo.CaseInfoFilesVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 案件开庭表
 * </p>
 *
 * @author zs
 * @since 2025-06-08 17:58:57
 */
@Getter
@Setter
@Schema(description = "案件开庭信息VO对象")
public class CaseHearingVO implements Serializable {

    @Schema(description = "表ID")
    private Long caseHearingId;

    @Schema(description = "管理的案件信息表")
    private Long caseInfoId;

    @Schema(description = "法院受理日期")
    private Date courtAcceptDate;

    @Schema(description = "审理程序")
    private String hearingProcedure;

    @Schema(description = "开庭时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date courtTime;

    @Schema(description = "开庭律师")
    private String courtLawyer;

    @Schema(description = "法院/仲裁委员会")
    private String court;

    @Schema(description = "法院案号")
    private String courtCaseNumber;

    @Schema(description = "法官")
    private String judge;

    @Schema(description = "法官电话")
    private String judgePhone;

    @Schema(description = "书记员")
    private String courtClerk;

    @Schema(description = "书记员电话")
    private String courtClerkPhone;

    @Schema(description = "判决结果")
    private String judgmentResult;

    @Schema(description = "公告送达日期")
    private Date serviceByPublicationDate;

    @Schema(description = "判决签发日期")
    private Date judgmentIssuedDate;

    @Schema(description = "判决签收日期")
    private Date judgmentSignDate;

    @Schema(description = "判决生效日期")
    private Date judgmentTakesEffect;

    @Schema(description = "判决内容")
    private String judgmentContent;

    @Schema(description = "跟进情况")
    private String followUpSituation;

    @Schema(description = "创建者")
    private Long creator;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新者")
    private Long updater;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "案件开庭附件")
    private List<CaseInfoFilesVO> caseHearingFilesList = List.of();
}
