package com.zs.business.goods.category.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;


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
@TableName("business_drug_goods_category")
@Schema(description = "商品档案Entity对象")
public class BusinessDrugGoodsCategoryEntity extends BaseEntity {

    /**   */
    @TableId
    private Long categoryId;

    /**  类别名称 */
    private String categoryName;

    /**  状态 1-正常 0 -停用 */
    private Integer status;

    /**  备注 */
    private String remark;


}
