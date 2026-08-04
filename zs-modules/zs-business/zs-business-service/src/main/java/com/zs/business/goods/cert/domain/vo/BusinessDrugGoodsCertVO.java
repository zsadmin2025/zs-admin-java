package com.zs.business.goods.cert.domain.vo;

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
@Schema(description = "商品证照附件VO对象")
public class BusinessDrugGoodsCertVO implements Serializable {

    @Schema(description = "主键ID")
    private Long drugGoodsCertId;

    @Schema(description = "关联药品商品ID，关联business_drug_goods.drug_goods_id")
    private Long drugGoodsId;

    @Schema(description = "证件名称")
    private String certName;

    @Schema(description = "证件编号")
    private String certNo;

    @Schema(description = "有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date validEndDate;

    @Schema(description = "附件文件地址")
    private String fileUrl;

    @Schema(description = "附件名称")
    private String fileName;

    @Schema(description = "附件原始名称")
    private String fileOriginalName;

    @Schema(description = "文件类型 1-图片 2-视频 3-音频 4-文档 5-其他")
    private String fileType;

    @Schema(description = "文件大小")
    private double fileSize;

}
