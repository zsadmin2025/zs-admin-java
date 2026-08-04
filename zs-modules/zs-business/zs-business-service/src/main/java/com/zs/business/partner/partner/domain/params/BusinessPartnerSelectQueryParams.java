package com.zs.business.partner.partner.domain.params;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

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
@Schema(description = "往来单位electQueryParams对象")
public class BusinessPartnerSelectQueryParams implements Serializable {

    @Schema(description = "主键ID")
    private Long partnerId;

    @Schema(description = "合作方分类ID，关联business_partner_category.id")
    private Long partnerCategoryId;

    @Schema(description = "企业名称")
    private String companyName;

    @Schema(description = "单位类型(字段)")
    private Long partnerType;

    @Schema(description = "企业地址")
    private String companyAddress;

    @Schema(description = "仓库地址")
    private String warehouseAddress;

    @Schema(description = "简称")
    private String shortName;

    @Schema(description = "名称首拼（用于快速检索）")
    private String namePinyin;

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "企业电话")
    private String companyPhone;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "传真")
    private String fax;

    @Schema(description = "电子邮箱")
    private String email;

    @Schema(description = "档案号")
    private String fileNo;

    @Schema(description = "结算状态 0-未结算 1-已结算")
    private Integer settlementStatus;

    @Schema(description = "结算账期(天)")
    private Integer settlementPeriod;

    @Schema(description = "单位状态 1启用 0停用")
    private Integer status;

    @Schema(description = "统一社会信用代码")
    private String socialCreditCode;

    @Schema(description = "有效期至")
    private Date validUntil;

    @Schema(description = "开户银行")
    private String bankName;

    @Schema(description = "银行账号")
    private String bankAccount;

    @Schema(description = "企业负责人")
    private String companyPrincipal;

    @Schema(description = "企业负责人-联系电话")
    private String principalPhone;

    @Schema(description = "企业管理员")
    private String companyManager;

    @Schema(description = "企业管理员-联系电话")
    private String managerPhone;

    @Schema(description = "质量负责人")
    private String qualityPrincipal;

    @Schema(description = "质量负责人-联系电话")
    private String qualityPhone;

    @Schema(description = "质量管理员")
    private String qualityManager;

    @Schema(description = "质量管理员联系电话")
    private String qualityManagerPhone;

    @Schema(description = "财务负责人")
    private String financePrincipal;

    @Schema(description = "财务负责人联系电话")
    private String financePhone;

    @Schema(description = "质量机构负责人")
    private String qualityOrgPrincipal;

    @Schema(description = "质量机构负责人联系电话")
    private String qualityOrgPhone;

}
