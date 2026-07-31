package com.zs.lawyer.customer.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 客户表
 * </p>
 *
 * @author zs
 * @since 2025-05-29 21:55:59
 */
@Getter
@Setter
@Schema(description = "客户管理VO对象")
public class CustomerVO implements Serializable {

    @Schema(description = "表ID")
    private Long customerId;
    @Schema(description = "客户类别")
    private String customerCategory;
    @Schema(description = "客户性质")
    private String customerNature;
    @Schema(description = "客户名称")
    private String customerName;
    @Schema(description = "客户编号")
    private String customerCode;
    @Schema(description = "客户简称")
    private String customerAbbreviation;
    @Schema(description = "维系人")
    private List<String> maintainingPeople;
    @Schema(description = "维系人名字")
    private String maintainingPeopleName;
    @Schema(description = "共享人")
    private List<String> sharer;
    @Schema(description = "共享人名字")
    private String sharerName;
    @Schema(description = "法人代表")
    private String legalPerson;
    @Schema(description = "统一社会信用代码")
    private String credit;
    @Schema(description = "客户来源")
    private String customerSource;
    @Schema(description = "客户等级")
    private String customerGrade;
    @Schema(description = "联系电话")
    private String contactNumber;
    @Schema(description = "微信号")
    private String wechatNumber;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "是否顾问，0-否，1-是")
    private Integer isConsultant;
    @Schema(description = "开户行")
    private String bankDeposit;
    @Schema(description = "银行账户")
    private String bankAccount;
    @Schema(description = "客户地址")
    private String customerAddress;
    @Schema(description = "录入人")
    private Long inputPerson;
    @Schema(description = "录入人名称")
    private String inputPersonName;
    @Schema(description = "录入时间")
    private Date inputTime;
    @Schema(description = "行业类别")
    private String industryCategory;
    @Schema(description = "行业类别名称")
    private String industryCategoryName;
    @Schema(description = "公司成立时间")
    private Date establishedTime;
    @Schema(description = "邮政编码")
    private String postalCode;
    @Schema(description = "传真号码")
    private String faxNumber;
    @Schema(description = "注册资本")
    private String registeredCapital;
    @Schema(description = "股东与持股情况")
    private String shareholding;
    @Schema(description = "管理层与分管情况")
    private String companyManagement;
    @Schema(description = "营业范围")
    private String businessScope;
    @Schema(description = "创建者")
    private Long creator;
    @Schema(description = "创建时间")
    private Date createTime;
    @Schema(description = "更新者")
    private Long updater;
    @Schema(description = "更新时间")
    private Date updateTime;
}
