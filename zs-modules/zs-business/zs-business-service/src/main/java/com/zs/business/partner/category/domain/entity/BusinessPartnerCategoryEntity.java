package com.zs.business.partner.category.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;


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
@TableName("business_partner_category")
@Schema(description = "单位分类Entity对象")
public class BusinessPartnerCategoryEntity extends BaseEntity {

    /**   */
    @TableId
    private Long businessPartnerCategoryId;

    /**  类别名称 */
    private String partnerCategoryName;

    /**  状态 1-正常 0 -停用 */
    private Integer status;

    /**  备注 */
    private String remark;


}
