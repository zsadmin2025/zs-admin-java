package com.zs.business.goods.goods.domain.params;

import com.zs.business.goods.cert.domain.params.BusinessDrugGoodsCertAddParams;
import com.zs.business.goods.cert.domain.params.BusinessDrugGoodsCertUpdateParams;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 药品商品主信息表
 * </p>
 *
 * @author zs
 * {@code @date} 2026-07-31 19:22:08
 */
@Getter
@Setter
@Schema(description = "药品商品主信息表updateParams对象")
public class BusinessDrugGoodsUpdateParams implements Serializable {

    @Schema(description = "主键ID")
    @NotNull(message = "主键ID不能为空")
    private Long drugGoodsId;

    @Schema(description = "商品类别id")
    @NotNull(message = "商品类别id不能为空")
    private Long goodsCategoryId;

    @Schema(description = "特殊商品管理：0-否 1-是")
    @NotNull(message = "特殊商品管理：0-否 1-是不能为空")
    private Integer specialGoodsType;

    @Schema(description = "商品货号")
    @NotBlank(message = "商品货号不能为空")
    @Size(max = 32, message = "商品货号长度不能超过32")
    private String goodsSn;

    @Schema(description = "剂型（对应数据字典）")
    @NotNull(message = "剂型（对应数据字典）不能为空")
    private Long dosageFormId;

    @Schema(description = "通用名")
    @NotBlank(message = "通用名不能为空")
    @Size(max = 100, message = "通用名长度不能超过100")
    private String commonName;

    @Schema(description = "商品名称")
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 100, message = "商品名称长度不能超过100")
    private String goodsName;

    @Schema(description = "单位（对应数据字典）")
    @NotNull(message = "单位（对应数据字典）不能为空")
    private Long unit;

    @Schema(description = "追溯码")
    @NotBlank(message = "追溯码不能为空")
    @Size(max = 200, message = "追溯码长度不能超过200")
    private String traceCode;

    @Schema(description = "生产厂家(编码/名称)")
    @NotNull(message = "生产厂家(编码/名称)不能为空")
    private Long manufacturerId;

    @Schema(description = "产地")
    @Size(max = 200, message = "产地长度不能超过200")
    private String originPlace;

    @Schema(description = "货区/货位")
    private Long storeLocationId;

    @Schema(description = "规格")
    @Size(max = 100, message = "规格长度不能超过100")
    private String spec;

    @Schema(description = "商品拼音码")
    @Size(max = 100, message = "商品拼音码长度不能超过100")
    private String goodsPinyin;

    @Schema(description = "条形码")
    @Size(max = 100, message = "条形码长度不能超过100")
    private String barcode;

    @Schema(description = "通用名拼音码")
    @Size(max = 100, message = "通用名拼音码长度不能超过100")
    private String commonNamePinyin;

    @Schema(description = "上市许可人")
    @Size(max = 256, message = "上市许可人长度不能超过256")
    private String marketingAuthorizationHolder;

    @Schema(description = "是否医保用药：0否 1是")
    private Integer isMedicalInsurance;

    @Schema(description = "是否中药：0否 1是")
    @NotNull(message = "是否中药：0否 1是不能为空")
    private Integer isChineseMedicine;

    @Schema(description = "商品分类id")
    @NotNull(message = "商品分类id不能为空")
    private Long goodsClassifyId;

    @Schema(description = "处方标志：1-处方 2-非处方")
    @NotNull(message = "处方标志：1-处方 2-非处方不能为空")
    private Integer prescriptionFlag;

    @Schema(description = "批准文号")
    @Size(max = 128, message = "批准文号长度不能超过128")
    private String approvalNo;

    @Schema(description = "收费等级(对应数据字典)")
    private Integer chargeLevel;

    @Schema(description = "近效期预警天数")
    private Integer expireWarnDays;

    @Schema(description = "最近进价")
    private BigDecimal latestPurchasePrice;

    @Schema(description = "药品有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date drugValidDate;

    @Schema(description = "档案号")
    @Size(max = 64, message = "档案号长度不能超过64")
    private String fileNo;

    @Schema(description = "最近供应商id")
    private Long latestSupplierId;

    @Schema(description = "批准文号有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date approvalValidEnd;

    @Schema(description = "商品出售包装形式,1-整合出售 2-散装出售")
    private Integer salePackageType;

    @Schema(description = "大包装数量")
    private Integer bigPackageNum;

    @Schema(description = "大包装单位(对应数据字典)")
    private Long bigPackageUnit;

    @Schema(description = "中包装数量")
    private BigDecimal midPackageNum;

    @Schema(description = "中包装单位(对应数据字典)")
    @Size(max = 32, message = "中包装单位(对应数据字典)长度不能超过32")
    private String midPackageUnit;

    @Schema(description = "小包装数量")
    private BigDecimal smallPackageNum;

    @Schema(description = "小包装单位(对应数据字典)")
    private Long smallPackageUnit;

    @Schema(description = "国家编码")
    @Size(max = 64, message = "国家编码长度不能超过64")
    private String nationalCode;

    @Schema(description = "省级编码")
    @Size(max = 64, message = "省级编码长度不能超过64")
    private String provinceCode;

    @Schema(description = "标准价")
    @NotNull(message = "标准价不能为空")
    private BigDecimal standardPrice;

    @Schema(description = "供货价")
    @NotNull(message = "供货价不能为空")
    private BigDecimal supplyPrice;

    @Schema(description = "销售单位（对应数据字典）")
    private Long saleUnit;

    @Schema(description = "拆零标志：0否 1是")
    private Integer splitFlag;

    @Schema(description = "拆零价格")
    private BigDecimal splitPrice;

    @Schema(description = "拆零单位")
    private Long splitUnit;

    @Schema(description = "拆零比例")
    private BigDecimal splitRatio;

    @Schema(description = "税率(%)")
    private BigDecimal taxRate;

    @Schema(description = "是否特价：0否 1是")
    private Integer isSpecialPrice;

    @Schema(description = "建议零售价")
    private BigDecimal suggestRetailPrice;

    @Schema(description = "单价加价系数")
    private BigDecimal priceCoefficient;

    @Schema(description = "首营供应商")
    private String firstSupplierId;

    @Schema(description = "最高零售价")
    private BigDecimal maxRetailPrice;

    @Schema(description = "批发价")
    private BigDecimal wholesalePrice;

    @Schema(description = "出厂价")
    private BigDecimal factoryPrice;

    @Schema(description = "集采价")
    private BigDecimal collectPrice;

    @Schema(description = "大分类属性（对应数据字典）")
    private Long mainCategoryAttr;

    @Schema(description = "功能属性分类（对应数据字典）")
    private Long funcAttr;

    @Schema(description = "给药途径属性 1-内服 2-外服")
    private Integer adminRouteAttr;

    @Schema(description = "中西药属性 1-中药 2-西药")
    private Integer chineseWesternAttr;

    @Schema(description = "基药属性 1-基药 2-非基药")
    private Integer baseDrugAttr;

    @Schema(description = "ABC属性（对应数据字典）")
    private Long abcAttr;

    @Schema(description = "经营关注属性（对应数据字典）")
    private Long businessAttr;

    @Schema(description = "商品详细分类（对应数据字典）")
    private Long goodsDetailClassify;

    @Schema(description = "药品其他属性3（对应数据字典）")
    private Long drugAttr3;

    @Schema(description = "药品其他属性4（对应数据字典）")
    private Long drugAttr4;

    @Schema(description = "药品其他属性5（对应数据字典）")
    private Long drugAttr5;

    @Schema(description = "药品其他属性6（对应数据字典）")
    private Long drugAttr6;

    @Schema(description = "药品其他属性7（对应数据字典）")
    private Long drugAttr7;

    @Schema(description = "药品其他属性8（对应数据字典）")
    private Long drugAttr8;

    @Schema(description = "药品其他属性9（对应数据字典）")
    private Long drugAttr9;

    @Schema(description = "药品其他属性10（对应数据字典）")
    private Long drugAttr10;

    @Schema(description = "兴奋剂成分 0-不包含 1-包含 ")
    private Integer stimulantAttr;

    @Schema(description = "英文名")
    @Size(max = 256, message = "英文名长度不能超过256")
    private String englishName;

    @Schema(description = "化学名")
    @Size(max = 256, message = "化学名长度不能超过256")
    private String chemicalName;

    @Schema(description = "拉丁名")
    @Size(max = 256, message = "拉丁名长度不能超过256")
    private String latinName;

    @Schema(description = "剂量")
    @Size(max = 128, message = "剂量长度不能超过128")
    private String dosage;

    @Schema(description = "用量单位（对应数据字典）")
    private Long doseUnit;

    @Schema(description = "频次")
    @Size(max = 64, message = "频次长度不能超过64")
    private String frequency;

    @Schema(description = "单周期天数")
    private Integer cycleDays;

    @Schema(description = "单周期用药次数")
    private Integer cycleTimes;

    @Schema(description = "生效开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date effectStartDate;

    @Schema(description = "生效结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date effectEndDate;

    @Schema(description = "扩展类型（对应数据字典）")
    private Long extendType;

    @Schema(description = "用法用量")
    @Size(max = 65535, message = "用法用量长度不能超过65,535")
    private String usageDosage;

    @Schema(description = "炮制方法")
    @Size(max = 65535, message = "炮制方法长度不能超过65,535")
    private String processingMethod;

    @Schema(description = "功效")
    @Size(max = 65535, message = "功效长度不能超过65,535")
    private String efficacy;

    @Schema(description = "药材科 (族) 来源")
    @Size(max = 65535, message = "药材科 (族) 来源长度不能超过65,535")
    private String herbFamilySource;

    @Schema(description = "药材种来源")
    @Size(max = 65535, message = "药材种来源长度不能超过65,535")
    private String herbSpeciesSource;

    @Schema(description = "药用部位")
    @Size(max = 65535, message = "药用部位长度不能超过65,535")
    private String medicinalPart;

    @Schema(description = "性状")
    @Size(max = 65535, message = "性状长度不能超过65,535")
    private String propertyDescription;

    @Schema(description = "主要成分")
    @Size(max = 65535, message = "主要成分长度不能超过65,535")
    private String mainIngredients;

    @Schema(description = "主治功能")
    @Size(max = 65535, message = "主治功能长度不能超过65,535")
    private String mainFunction;

    @Schema(description = "适应症")
    @Size(max = 65535, message = "适应症长度不能超过65,535")
    private String indications;

    @Schema(description = "状态 1-正常 0 -停用")
    private Integer status;

    @Schema(description = "商品证照附件")
    private List<BusinessDrugGoodsCertUpdateParams> certs = List.of();





}
