package com.zs.lawyer.cases.info.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 案件相关方
 * </p>
 *
 * @author zs
 * @since 2025-06-08 18:08:42
 */
@Getter
@Setter
@TableName("case_related_parties")
@Schema(description = "案件相关方Entity对象")
public class CaseRelatedPartiesEntity extends BaseEntity {

    /**  表ID */
    @TableId
    private Long caseRelatedPartiesId;

    /**  关联案件信息表 */
    private Long caseInfoId;

    /**  our_side我方 other_side对方third_party三方 */
    private String role;

    /**  关联方类型 */
    private String relationType;

    /**  关联方名称 */
    private String relatedName;


}
