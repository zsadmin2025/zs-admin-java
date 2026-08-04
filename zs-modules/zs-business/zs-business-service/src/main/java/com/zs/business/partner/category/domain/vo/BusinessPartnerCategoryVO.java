package com.zs.business.partner.category.domain.vo;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <p>
 * 单位分类
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-02 09:15:47
 */
@Getter
@Setter
@Schema(description = "单位分类VO对象")
public class BusinessPartnerCategoryVO implements Serializable {

    @Schema(description = "")
    private Long businessPartnerCategoryId;

    @Schema(description = "类别名称")
    private String partnerCategoryName;

    @Schema(description = "状态 1-正常 0 -停用")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建人姓名")
    private String creatorName;

    @Schema(description = "创建时间")
    private String createTime;

}
