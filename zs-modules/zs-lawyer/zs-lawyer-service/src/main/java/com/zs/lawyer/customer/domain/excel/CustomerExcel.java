package com.zs.lawyer.customer.domain.excel;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
/**
 * <p>
 * $!{table.comment}
 * </p>
 *
 * @author zs
 * @since 2025-05-29 21:55:59
 */
@Getter
@Setter
@Schema(description = "客户管理Excel对象")
@ExcelIgnoreUnannotated
public class CustomerExcel {

    @ExcelProperty("表ID")
    private Long customerId;
    @ExcelProperty("客户类别")
    private String customerCategory;
    @ExcelProperty("客户性质")
    private String customerNature;
    @ExcelProperty("客户名称")
    private String customerName;
    @ExcelProperty("客户简称")
    private String customerAbbreviation;
    @ExcelProperty("维系人")
    private Long maintainingPeople;
    @ExcelProperty("维系人名字")
    private String maintainingPeopleName;
    @ExcelProperty("共享人")
    private Long sharer;
    @ExcelProperty("共享人名字")
    private String sharerName;
    @ExcelProperty("法人代表")
    private String legalPerson;
    @ExcelProperty("统一社会信用代码")
    private String credit;
    @ExcelProperty("客户来源")
    private String customerSource;
    @ExcelProperty("客户等级")
    private String customerGrade;
    @ExcelProperty("联系电话")
    private String contactNumber;
    @ExcelProperty("微信号")
    private String wechatNumber;
    @ExcelProperty("邮箱")
    private String email;
    @ExcelProperty("是否顾问，0-否，1-是")
    private Integer isConsultant;
    @ExcelProperty("开户行")
    private String bankDeposit;
    @ExcelProperty("银行账户")
    private String bankAccount;
    @ExcelProperty("客户地址")
    private String customerAddress;
    @ExcelProperty("录入人")
    private Integer inputPerson;
    @ExcelProperty("录入人名称")
    private String inputPersonName;
    @ExcelProperty("录入时间")
    private Date inputTime;
    @ExcelProperty("行业类别")
    private String industryCategory;
    @ExcelProperty("行业类别名称")
    private String industryCategoryName;
    @ExcelProperty("公司成立时间")
    private Date establishedTime;
    @ExcelProperty("邮政编码")
    private String postalCode;
    @ExcelProperty("传真号码")
    private String faxNumber;
    @ExcelProperty("注册资本")
    private String registeredCapital;
    @ExcelProperty("股东与持股情况")
    private String shareholding;
    @ExcelProperty("管理层与分管情况")
    private String companyManagement;
    @ExcelProperty("营业范围")
    private String businessScope;
    @ExcelProperty("创建者")
    private Long creator;
    @ExcelProperty("创建时间")
    private Date createTime;
    @ExcelProperty("更新者")
    private Long updater;
    @ExcelProperty("更新时间")
    private Date updateTime;
}
