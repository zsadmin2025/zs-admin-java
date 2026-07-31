package com.zs.lawyer.cases.team.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
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
@TableName("case_team")
@Schema(description = "案件团队Entity对象")
public class CaseTeamEntity extends BaseEntity {

    /**   */
    @TableId
    private Long caseTeamId;

    /**  关联案件信息表id */
    private Long caseInfoId;

    /**  承接律师 */
    private Long undertakeLawyer;

    /**  协接律师 */
    private String coordinatingLawyer;

    /**  主办律师 */
    private String leadLawyer;

    /**  协办人员 */
    private String coOrganizer;

    /**  助理 */
    private String assistant;

    /**  秘书 */
    private String secretary;




}
