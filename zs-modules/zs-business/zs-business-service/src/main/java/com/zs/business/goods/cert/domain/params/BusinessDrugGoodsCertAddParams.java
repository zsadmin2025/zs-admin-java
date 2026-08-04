package com.zs.business.goods.cert.domain.params;

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
 * 商品证照附件
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-03 11:09:18
 */
@Getter
@Setter
@Schema(description = "商品证照附件AddParams对象")
public class BusinessDrugGoodsCertAddParams implements Serializable {


    @Schema(description = "关联药品商品ID，关联business_drug_goods.drug_goods_id")
    @NotNull(message = "关联药品商品ID，关联business_drug_goods.drug_goods_id不能为空")
    private Long drugGoodsId;

    @Schema(description = "证件名称")
    @NotBlank(message = "证件名称不能为空")
    @Size(max = 128, message = "证件名称长度不能超过128")
    private String certName;

    @Schema(description = "证件编号")
    @Size(max = 128, message = "证件编号长度不能超过128")
    private String certNo;

    @Schema(description = "有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date validEndDate;

    @Schema(description = "附件文件地址")
    @Size(max = 512, message = "附件文件地址长度不能超过512")
    private String fileUrl;








}
