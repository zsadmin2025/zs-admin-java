package com.zs.business.partner.partner.domain.excel;

import com.zs.common.core.annotation.DictBind;
import lombok.Getter;
import lombok.Setter;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
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
@Schema(description = "往来单位Excel对象")
@ExcelIgnoreUnannotated
public class BusinessPartnerExcel {

    @ExcelProperty("主键ID")
    private Long partnerId;

    @ExcelProperty("分类大类")
    private String partnerCategoryName;

    @ExcelProperty("企业名称")
    private String companyName;

    private Long partnerType;

    @DictBind(dictCode = "unitType", sourceField = "partnerType", defaultValue = "未知")
    @Schema(description = "单位类型(标签)")
    private String partnerTypeLabel;


    @ExcelProperty("企业地址")
    private String companyAddress;

    @ExcelProperty("仓库地址")
    private String warehouseAddress;

    @ExcelProperty("简称")
    private String shortName;

    @ExcelProperty("名称首拼（用于快速检索）")
    private String namePinyin;

    @ExcelProperty("联系人")
    private String contactPerson;

    @ExcelProperty("企业电话")
    private String companyPhone;

    @ExcelProperty("手机号")
    private String mobile;

    @ExcelProperty("传真")
    private String fax;

    @ExcelProperty("电子邮箱")
    private String email;

    @ExcelProperty("档案号")
    private String fileNo;

    @ExcelProperty("结算状态 0-未结算 1-已结算")
    private Integer settlementStatus;

    @ExcelProperty("结算账期(天)")
    private Integer settlementPeriod;

    @ExcelProperty("单位状态 1启用 0停用")
    private Integer status;

    @ExcelProperty("统一社会信用代码")
    private String socialCreditCode;

    @ExcelProperty("有效期至")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date validUntil;

    @ExcelProperty("开户银行")
    private String bankName;

    @ExcelProperty("银行账号")
    private String bankAccount;

    @ExcelProperty("企业负责人")
    private String companyPrincipal;

    @ExcelProperty("企业负责人-联系电话")
    private String principalPhone;

    @ExcelProperty("企业管理员")
    private String companyManager;

    @ExcelProperty("企业管理员-联系电话")
    private String managerPhone;

    @ExcelProperty("质量负责人")
    private String qualityPrincipal;

    @ExcelProperty("质量负责人-联系电话")
    private String qualityPhone;

    @ExcelProperty("质量管理员")
    private String qualityManager;

    @ExcelProperty("质量管理员联系电话")
    private String qualityManagerPhone;

    @ExcelProperty("财务负责人")
    private String financePrincipal;

    @ExcelProperty("财务负责人联系电话")
    private String financePhone;

    @ExcelProperty("质量机构负责人")
    private String qualityOrgPrincipal;

    @ExcelProperty("质量机构负责人联系电话")
    private String qualityOrgPhone;

}
