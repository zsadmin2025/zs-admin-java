package com.zs.lawyer.cases.infoApprove.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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
@TableName("case_info_approve")
@Schema(description = "案件审批Entity对象")
public class CaseInfoApproveEntity extends BaseEntity {

    /**  案件审批表ID */
    @TableId
    private Long caseInfoApproveId;

    /**  案件表ID */
    private Long caseInfoId;

    /**  待审批律师 */
    private Long approvalLawyer;

    /**  审批状态 0-拒绝 1-审批中 2-通过 */
    private Integer approveStatus;




}
