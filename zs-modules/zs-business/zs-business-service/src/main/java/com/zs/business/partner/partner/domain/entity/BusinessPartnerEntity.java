package com.zs.business.partner.partner.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;


/**
 * <p>
 * 往来单位
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-02 11:23:09
 */
@Getter
@Setter
@TableName("business_partner")
@Schema(description = "往来单位Entity对象")
public class BusinessPartnerEntity extends BaseEntity {

    /**  主键ID */
    @TableId
    private Long partnerId;

    /**  合作方分类ID，关联business_partner_category.id */
    private Long partnerCategoryId;

    /**  企业名称 */
    private String companyName;

    /**  单位类型(字段) */
    private Long partnerType;

    /**  企业地址 */
    private String companyAddress;

    /**  仓库地址 */
    private String warehouseAddress;

    /**  简称 */
    private String shortName;

    /**  名称首拼（用于快速检索） */
    private String namePinyin;

    /**  联系人 */
    private String contactPerson;

    /**  企业电话 */
    private String companyPhone;

    /**  手机号 */
    private String mobile;

    /**  传真 */
    private String fax;

    /**  电子邮箱 */
    private String email;

    /**  档案号 */
    private String fileNo;

    /**  结算状态 0-未结算 1-已结算 */
    private Integer settlementStatus;

    /**  结算账期(天) */
    private Integer settlementPeriod;

    /**  单位状态 1启用 0停用 */
    private Integer status;

    /**  统一社会信用代码 */
    private String socialCreditCode;

    /**  有效期至 */
    private Date validUntil;

    /**  开户银行 */
    private String bankName;

    /**  银行账号 */
    private String bankAccount;

    /**  企业负责人 */
    private String companyPrincipal;

    /**  企业负责人-联系电话 */
    private String principalPhone;

    /**  企业管理员 */
    private String companyManager;

    /**  企业管理员-联系电话 */
    private String managerPhone;

    /**  质量负责人 */
    private String qualityPrincipal;

    /**  质量负责人-联系电话 */
    private String qualityPhone;

    /**  质量管理员 */
    private String qualityManager;

    /**  质量管理员联系电话 */
    private String qualityManagerPhone;

    /**  财务负责人 */
    private String financePrincipal;

    /**  财务负责人联系电话 */
    private String financePhone;

    /**  质量机构负责人 */
    private String qualityOrgPrincipal;

    /**  质量机构负责人联系电话 */
    private String qualityOrgPhone;

}
