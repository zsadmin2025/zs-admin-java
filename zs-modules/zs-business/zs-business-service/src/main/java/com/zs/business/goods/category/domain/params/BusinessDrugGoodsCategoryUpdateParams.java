package com.zs.business.goods.category.domain.params;

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
 * 商品档案
 * </p>
 *
 * @author zs
 * {@code @date} 2026-07-31 19:22:03
 */
@Getter
@Setter
@Schema(description = "商品档案updateParams对象")
public class BusinessDrugGoodsCategoryUpdateParams implements Serializable {

    @Schema(description = "")
    @NotNull(message = "不能为空")
    private Long categoryId;

    @Schema(description = "类别名称")
    @NotBlank(message = "类别名称不能为空")
    @Size(max = 50, message = "类别名称长度不能超过50")
    private String categoryName;

    @Schema(description = "状态 1-正常 0 -停用")
    @NotNull(message = "状态 1-正常 0 -停用不能为空")
    private Integer status;

    @Schema(description = "备注")
    @Size(max = 255, message = "备注长度不能超过255")
    private String remark;








}
