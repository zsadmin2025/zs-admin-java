package com.zs.lawyer.customer.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
@TableName("biz_customer")
@Schema(description = "客户管理Entity对象")
public class CustomerEntity extends BaseEntity {

    /**  表ID */
    @TableId
    private Long customerId;
    /**  客户类别 */
    private String customerCategory;
    /**  客户性质 */
    private String customerNature;
    /**  客户名称 */
    private String customerName;
    /**  客户编号 */
    private String customerCode;
    /**  客户简称 */
    private String customerAbbreviation;
    /**  维系人 */
    private String maintainingPeople;
    /**  维系人名字 */
    private String maintainingPeopleName;
    /**  共享人 */
    private String sharer;
    /**  共享人名字 */
    private String sharerName;
    /**  法人代表 */
    private String legalPerson;
    /**  统一社会信用代码 */
    private String credit;
    /**  客户来源 */
    private String customerSource;
    /**  客户等级 */
    private String customerGrade;
    /**  联系电话 */
    private String contactNumber;
    /**  微信号 */
    private String wechatNumber;
    /**  邮箱 */
    private String email;
    /**  是否顾问，0-否，1-是 */
    private Integer isConsultant;
    /**  开户行 */
    private String bankDeposit;
    /**  银行账户 */
    private String bankAccount;
    /**  客户地址 */
    private String customerAddress;
    /**  录入人 */
    private Long inputPerson;
    /**  录入人名称 */
    private String inputPersonName;
    /**  录入时间 */
    private Date inputTime;
    /**  行业类别 */
    private String industryCategory;
    /**  行业类别名称 */
    private String industryCategoryName;
    /**  公司成立时间 */
    private Date establishedTime;
    /**  邮政编码 */
    private String postalCode;
    /**  传真号码 */
    private String faxNumber;
    /**  注册资本 */
    private String registeredCapital;
    /**  股东与持股情况 */
    private String shareholding;
    /**  管理层与分管情况 */
    private String companyManagement;
    /**  营业范围 */
    private String businessScope;
    /**  客户状态 */
    private Integer status;


}
