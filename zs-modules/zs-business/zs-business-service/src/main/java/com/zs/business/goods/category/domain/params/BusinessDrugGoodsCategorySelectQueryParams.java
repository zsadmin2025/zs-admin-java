package com.zs.business.goods.category.domain.params;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;

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
@Schema(description = "商品档案electQueryParams对象")
public class BusinessDrugGoodsCategorySelectQueryParams implements Serializable {

    @Schema(description = "")
    private Long categoryId;

    @Schema(description = "类别名称")
    private String categoryName;

    @Schema(description = "状态 1-正常 0 -停用")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

}
