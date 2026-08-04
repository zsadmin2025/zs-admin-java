package com.zs.business.partner.category.domain.params;

import com.zs.common.core.page.BasePageParams;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "单位分类ageQueryParams对象")
public class BusinessPartnerCategoryPageQueryParams  extends BasePageParams implements Serializable {

    @Schema(description = "")
    private Long businessPartnerCategoryId;

    @Schema(description = "类别名称")
    private String partnerCategoryName;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

}
