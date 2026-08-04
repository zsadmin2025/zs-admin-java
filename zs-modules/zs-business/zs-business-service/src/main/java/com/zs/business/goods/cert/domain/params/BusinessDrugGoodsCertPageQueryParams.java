package com.zs.business.goods.cert.domain.params;

import com.zs.common.core.page.BasePageParams;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <p>
 * 商品证照附件
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-03 11:09:18
 */
@Getter
@Setter
@Schema(description = "商品证照附件ageQueryParams对象")
public class BusinessDrugGoodsCertPageQueryParams  extends BasePageParams implements Serializable {

    @Schema(description = "主键ID")
    private Long drugGoodsCertId;

    @Schema(description = "关联药品商品ID，关联business_drug_goods.drug_goods_id")
    private Long drugGoodsId;

    @Schema(description = "证件名称")
    private String certName;

    @Schema(description = "证件编号")
    private String certNo;

    @Schema(description = "有效期")
    private Date validEndDate;

    @Schema(description = "附件文件地址")
    private String fileUrl;

}
