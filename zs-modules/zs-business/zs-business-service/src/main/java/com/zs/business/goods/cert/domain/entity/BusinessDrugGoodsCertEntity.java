package com.zs.business.goods.cert.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;


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
@TableName("business_drug_goods_cert")
@Schema(description = "商品证照附件Entity对象")
public class BusinessDrugGoodsCertEntity extends BaseEntity {

    /**  主键ID */
    @TableId
    private Long drugGoodsCertId;

    /**  关联药品商品ID，关联business_drug_goods.drug_goods_id */
    private Long drugGoodsId;

    /**  证件名称 */
    private String certName;

    /**  证件编号 */
    private String certNo;

    /**  有效期 */
    private Date validEndDate;

    /**  附件文件地址 */
    private String fileUrl;


}
