package com.zs.business.partner.category.domain.params;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
@Schema(description = "单位分类updateParams对象")
public class BusinessPartnerCategoryUpdateParams implements Serializable {

    @Schema(description = "")
    @NotNull(message = "不能为空")
    private Long businessPartnerCategoryId;

    @Schema(description = "类别名称")
    @Size(max = 255, message = "类别名称长度不能超过255")
    private String partnerCategoryName;

    @Schema(description = "状态")
    @NotNull(message = "不能为空")
    private Integer status;

    @Schema(description = "备注")
    @Size(max = 255, message = "备注长度不能超过255")
    private String remark;








}
