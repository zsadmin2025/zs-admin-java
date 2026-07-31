package com.zs.lawyer.cases.hearing.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
@TableName("case_hearing")
@Schema(description = "案件开庭信息Entity对象")
public class CaseHearingEntity extends BaseEntity {

    /**  表ID */
    @TableId
    private Long caseHearingId;

    /**  管理的案件信息表 */
    private Long caseInfoId;

    /**  法院受理日期 */
    private Date courtAcceptDate;

    /**  开庭时间*/
    private Date courtTime;

    /**  审理程序 */
    private String hearingProcedure;

    /**  开庭律师 */
    private String courtLawyer;

    /**  法院/仲裁委员会 */
    private String court;

    /**  法院案号 */
    private String courtCaseNumber;

    /**  法官 */
    private String judge;

    /**  法官电话 */
    private String judgePhone;

    /**  书记员 */
    private String courtClerk;

    /**  书记员电话 */
    private String courtClerkPhone;

    /**  判决结果 */
    private String judgmentResult;

    /**  公告送达日期 */
    private Date serviceByPublicationDate;

    /**  判决签发日期 */
    private Date judgmentIssuedDate;

    /**  判决签收日期 */
    private Date judgmentSignDate;

    /**  判决生效日期 */
    private Date judgmentTakesEffect;

    /**  判决内容 */
    private String judgmentContent;

    /**  跟进情况 */
    private String followUpSituation;


}
