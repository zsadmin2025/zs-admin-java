package com.zs.lawyer.cases.info.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
@Schema(description = "案件相关方VO对象")
public class CaseRelatedPartiesVO implements Serializable {

    @Schema(description = "表ID")
    private Long caseRelatedPartiesId;

    @Schema(description = "our_side我方 other_side对方 third_party三方")
    private String role;

    @Schema(description = "关联方类型")
    private String relationType;

    @Schema(description = "关联方名称")
    private String relatedName;

}
