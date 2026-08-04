package com.zs.business.goods.goods.domain.params;

import com.zs.common.core.page.BasePageParams;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;

/**
 * <p>
 * 药品商品主信息表
 * </p>
 *
 * @author zs
 * {@code @date} 2026-07-31 19:22:08
 */
@Getter
@Setter
@Schema(description = "药品商品主信息表ageQueryParams对象")
public class BusinessDrugGoodsPageQueryParams  extends BasePageParams implements Serializable {

    @Schema(description = "商品分类id")
    private  Long goodsCategoryId;

    @Schema(description = "商品货号")
    private String goodsSn;

    @Schema(description = "通用名")
    private String commonName;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "通用名拼音码")
    private String commonNamePinyin;

    @Schema(description = "档案号")
    private String fileNo;

    @Schema(description = "国家编码")
    private String nationalCode;

    @Schema(description = "生产厂家id")
    private Long manufacturerId;

    @Schema(description = "状态 1-正常 0 -停用")
    private Integer status;

}
