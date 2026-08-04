package com.zs.business.goods.goods.domain.vo;

import com.zs.business.goods.cert.domain.params.BusinessDrugGoodsCertAddParams;
import com.zs.business.goods.cert.domain.vo.BusinessDrugGoodsCertVO;
import com.zs.common.core.annotation.DictBind;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;

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
@Schema(description = "药品商品主信息表VO对象")
public class BusinessDrugGoodsVO implements Serializable {

    @Schema(description = "主键ID")
    private Long drugGoodsId;

    @Schema(description = "商品类别id")
    private Long goodsCategoryId;

    @Schema(description = "商品类别名称")
    private String goodsCategoryIdName;

    @Schema(description = "特殊商品管理：0-否 1-是")
    private Integer specialGoodsType;

    @Schema(description = "商品货号")
    private String goodsSn;

    @Schema(description = "剂型")
    private Long dosageFormId;

    @Schema(description = "剂型名称")
    @DictBind(dictCode = "dosageType", sourceField = "dosageFormId", defaultValue = "未知")
    private String dosageFormLabel;

    @Schema(description = "通用名")
    private String commonName;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "单位")
    private Long unit;

    @Schema(description = "单位名称")
    @DictBind(dictCode = "unit", sourceField = "unit", defaultValue = "未知")
    private String unitLabel;

    @Schema(description = "追溯码")
    private String traceCode;

    @Schema(description = "生产厂家")
    private Long manufacturerId;

    @Schema(description = "生产厂家名称")
    private String manufacturerIdName;

    @Schema(description = "产地")
    private String originPlace;

    @Schema(description = "货区/货位")
    private Long storeLocationId;

    @Schema(description = "货区/货位名称")
    private String storeLocationIdName;

    @Schema(description = "规格")
    private String spec;

    @Schema(description = "商品拼音码")
    private String goodsPinyin;

    @Schema(description = "条形码")
    private String barcode;

    @Schema(description = "通用名拼音码")
    private String commonNamePinyin;

    @Schema(description = "上市许可人")
    private String marketingAuthorizationHolder;

    @Schema(description = "是否医保用药")
    private Integer isMedicalInsurance;

    @Schema(description = "是否中药")
    private Integer isChineseMedicine;

    @Schema(description = "商品分类id")
    private Long goodsClassifyId;

    @Schema(description = "商品分类名称")
    @DictBind(dictCode = "productCategory", sourceField = "goodsClassifyId", defaultValue = "未知")
    private String goodsClassifyIdName;

    @Schema(description = "处方标志")
    private Integer prescriptionFlag;

    @Schema(description = "批准文号")
    private String approvalNo;

    @Schema(description = "收费等级(对应数据字典)")
    private Integer chargeLevel;

    @Schema(description = "收费等级名称")
    @DictBind(dictCode = "chargeLevel", sourceField = "pricingTier", defaultValue = "未知")
    private String chargeLevelLabel;

    @Schema(description = "近效期预警天数")
    private Integer expireWarnDays;

    @Schema(description = "最近进价")
    private BigDecimal latestPurchasePrice;

    @Schema(description = "药品有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date drugValidDate;

    @Schema(description = "档案号")
    private String fileNo;

    @Schema(description = "最近供应商")
    private Long latestSupplierId;

    @Schema(description = "最近供应商名称")
    private String latestSupplierIdName;

    @Schema(description = "批准文号有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date approvalValidEnd;

    @Schema(description = "商品出售包装形式,1-整合出售 2-散装出售")
    private Integer salePackageType;

    @Schema(description = "大包装数量")
    private Integer bigPackageNum;

    @Schema(description = "大包装单位")
    private Long bigPackageUnit;

    @Schema(description = "大包装单位名称")
    @DictBind(dictCode = "unit", sourceField = "bigPackageUnit", defaultValue = "未知")
    private String bigPackageUnitLabel;

    @Schema(description = "中包装数量")
    private BigDecimal midPackageNum;

    @Schema(description = "中包装单位(对应数据字典)")
    private Long midPackageUnit;

    @Schema(description = "中包装单位名称")
    @DictBind(dictCode = "unit", sourceField = "midPackageUnit", defaultValue = "未知")
    private String midPackageUnitLabel;

    @Schema(description = "小包装数量")
    private BigDecimal smallPackageNum;

    @Schema(description = "小包装单位(对应数据字典)")
    private Long smallPackageUnit;

    @Schema(description = "小包装单位名称")
    @DictBind(dictCode = "unit", sourceField = "smallPackageUnit", defaultValue = "未知")
    private String smallPackageUnitLabel;

    @Schema(description = "国家编码")
    private String nationalCode;

    @Schema(description = "省级编码")
    private String provinceCode;

    @Schema(description = "标准价")
    private BigDecimal standardPrice;

    @Schema(description = "供货价")
    private BigDecimal supplyPrice;

    @Schema(description = "销售单位（对应数据字典）")
    private Long saleUnit;

    @Schema(description = "销售单位名称")
    @DictBind(dictCode = "unit", sourceField = "saleUnit", defaultValue = "未知")
    private String saleUnitLabel;

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

    @Schema(description = "大分类属性名称")
    @DictBind(dictCode = "majorCategoryAttributes", sourceField = "mainCategoryAttr", defaultValue = "未知")
    private String mainCategoryAttrLabel;

    @Schema(description = "功能属性分类（对应数据字典）")
    private Long funcAttr;

    @Schema(description = "功能属性名称")
    @DictBind(dictCode = "funcAttributeCategory", sourceField = "funcAttr", defaultValue = "未知")
    private String funcAttrLabel;

    @Schema(description = "给药途径属性 1-内服 2-外服")
    private Integer adminRouteAttr;

    @Schema(description = "中西药属性 1-中药 2-西药")
    private Integer chineseWesternAttr;

    @Schema(description = "基药属性 1-基药 2-非基药")
    private Integer baseDrugAttr;

    @Schema(description = "ABC属性（对应数据字典）")
    private Long abcAttr;

    @Schema(description = "ABC属性名称")
    @DictBind(dictCode = "ABCattributes", sourceField = "abcAttr", defaultValue = "未知")
    private String abcAttrLabel;

    @Schema(description = "经营关注属性（对应数据字典）")
    private Long businessAttr;

    @Schema(description = "经营关注属性名称")
    @DictBind(dictCode = "businessFocusAttribute", sourceField = "businessAttr", defaultValue = "未知")
    private String businessAttrLabel;

    @Schema(description = "商品详细分类（对应数据字典）")
    private Long goodsDetailClassify;

    @Schema(description = "商品详细分类名称")
    @DictBind(dictCode = "productDetailedClass", sourceField = "goodsDetailClassify", defaultValue = "未知")
    private String goodsDetailClassifyLabel;

    @Schema(description = "药品其他属性3（对应数据字典）")
    private Long drugAttr3;

    @Schema(description = "药品其他属性3名称")
    @DictBind(dictCode = "productDetailedClass", sourceField = "drugAttr3", defaultValue = "未知")
    private String drugAttr3Label;

    @Schema(description = "药品其他属性4（对应数据字典）")
    private Long drugAttr4;

    @Schema(description = "药品其他属性4名称")
    @DictBind(dictCode = "productDetailedClass", sourceField = "drugAttr4", defaultValue = "未知")
    private String drugAttr4Label;

    @Schema(description = "药品其他属性5（对应数据字典）")
    private Long drugAttr5;

    @Schema(description = "药品其他属性5名称")
    @DictBind(dictCode = "productDetailedClass", sourceField = "drugAttr5", defaultValue = "未知")
    private String drugAttr5Label;

    @Schema(description = "药品其他属性6（对应数据字典）")
    private Long drugAttr6;

    @Schema(description = "药品其他属性6名称")
    @DictBind(dictCode = "productDetailedClass", sourceField = "drugAttr6", defaultValue = "未知")
    private String drugAttr6Label;

    @Schema(description = "药品其他属性7（对应数据字典）")
    private Long drugAttr7;

    @Schema(description = "药品其他属性7名称")
    @DictBind(dictCode = "productDetailedClass", sourceField = "drugAttr7", defaultValue = "未知")
    private String drugAttr7Label;

    @Schema(description = "药品其他属性8（对应数据字典）")
    private Long drugAttr8;

    @Schema(description = "药品其他属性9名称")
    @DictBind(dictCode = "productDetailedClass", sourceField = "drugAttr8", defaultValue = "未知")
    private String drugAttr8Label;

    @Schema(description = "药品其他属性9（对应数据字典）")
    private Long drugAttr9;

    @Schema(description = "药品其他属性10名称")
    @DictBind(dictCode = "productDetailedClass", sourceField = "drugAttr9", defaultValue = "未知")
    private String drugAttr9Label;

    @Schema(description = "药品其他属性10（对应数据字典）")
    private Long drugAttr10;

    @Schema(description = "药品其他属性10名称")
    @DictBind(dictCode = "productDetailedClass", sourceField = "drugAttr10", defaultValue = "未知")
    private String drugAttr10Label;

    @Schema(description = "兴奋剂成分 0-不包含 1-包含 ")
    private Integer stimulantAttr;

    @Schema(description = "英文名")
    private String englishName;

    @Schema(description = "化学名")
    private String chemicalName;

    @Schema(description = "拉丁名")
    private String latinName;

    @Schema(description = "剂量")
    private String dosage;

    @Schema(description = "用量单位（对应数据字典）")
    private Long doseUnit;

    @Schema(description = "剂量单位名称")
    @DictBind(dictCode = "usageUnit", sourceField = "doseUnit", defaultValue = "未知")
    private String doseUnitLabel;

    @Schema(description = "频次")
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

    @Schema(description = "扩展类型名称")
    @DictBind(dictCode = "extensionType", sourceField = "extendType", defaultValue = "未知")
    private String extendTypeLabel;

    @Schema(description = "用法用量")
    private String usageDosage;

    @Schema(description = "炮制方法")
    private String processingMethod;

    @Schema(description = "功效")
    private String efficacy;

    @Schema(description = "药材科 (族) 来源")
    private String herbFamilySource;

    @Schema(description = "药材种来源")
    private String herbSpeciesSource;

    @Schema(description = "药用部位")
    private String medicinalPart;

    @Schema(description = "性状")
    private String propertyDescription;

    @Schema(description = "主要成分")
    private String mainIngredients;

    @Schema(description = "主治功能")
    private String mainFunction;

    @Schema(description = "适应症")
    private String indications;

    @Schema(description = "状态")
    private Integer status;



    @Schema(description = "商品证照附件")
    private List<BusinessDrugGoodsCertVO> certs = List.of();;

}
