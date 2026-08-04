package com.zs.business.goods.goods.domain.excel;

import lombok.Getter;
import lombok.Setter;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
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
@Schema(description = "药品商品主信息表Excel对象")
@ExcelIgnoreUnannotated
public class BusinessDrugGoodsExcel {

    @ExcelProperty("主键ID")
    private Long drugGoodsId;

    @ExcelProperty("商品类别id")
    private Long goodsCategoryId;

    @ExcelProperty("特殊商品管理：0-否 1-是")
    private Integer specialGoodsType;

    @ExcelProperty("商品货号")
    private String goodsSn;

    @ExcelProperty("剂型（对应数据字典）")
    private Long dosageFormId;

    @ExcelProperty("通用名")
    private String commonName;

    @ExcelProperty("商品名称")
    private String goodsName;

    @ExcelProperty("单位（对应数据字典）")
    private Long unit;

    @ExcelProperty("追溯码")
    private String traceCode;

    @ExcelProperty("生产厂家(编码/名称)")
    private Long manufacturerId;

    @ExcelProperty("产地")
    private String originPlace;

    @ExcelProperty("货区/货位")
    private Long storeLocationId;

    @ExcelProperty("规格")
    private String spec;

    @ExcelProperty("商品拼音码")
    private String goodsPinyin;

    @ExcelProperty("条形码")
    private String barcode;

    @ExcelProperty("通用名拼音码")
    private String commonNamePinyin;

    @ExcelProperty("上市许可人")
    private String marketingAuthorizationHolder;

    @ExcelProperty("是否医保用药：0否 1是")
    private Integer isMedicalInsurance;

    @ExcelProperty("是否中药：0否 1是")
    private Integer isChineseMedicine;

    @ExcelProperty("商品分类id")
    private Long goodsClassifyId;

    @ExcelProperty("处方标志：1-处方 2-非处方")
    private Integer prescriptionFlag;

    @ExcelProperty("批准文号")
    private String approvalNo;

    @ExcelProperty("收费等级(对应数据字典)")
    private Integer chargeLevel;

    @ExcelProperty("近效期预警天数")
    private Integer expireWarnDays;

    @ExcelProperty("最近进价")
    private BigDecimal latestPurchasePrice;

    @ExcelProperty("药品有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date drugValidDate;

    @ExcelProperty("档案号")
    private String fileNo;

    @ExcelProperty("最近供应商id")
    private Long latestSupplierId;

    @ExcelProperty("批准文号有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date approvalValidEnd;

    @ExcelProperty("商品出售包装形式,1-整合出售 2-散装出售")
    private Integer salePackageType;

    @ExcelProperty("大包装数量")
    private Integer bigPackageNum;

    @ExcelProperty("大包装单位(对应数据字典)")
    private Long bigPackageUnit;

    @ExcelProperty("中包装数量")
    private BigDecimal midPackageNum;

    @ExcelProperty("中包装单位(对应数据字典)")
    private String midPackageUnit;

    @ExcelProperty("小包装数量")
    private BigDecimal smallPackageNum;

    @ExcelProperty("小包装单位(对应数据字典)")
    private Long smallPackageUnit;

    @ExcelProperty("国家编码")
    private String nationalCode;

    @ExcelProperty("省级编码")
    private String provinceCode;

    @ExcelProperty("标准价")
    private BigDecimal standardPrice;

    @ExcelProperty("供货价")
    private BigDecimal supplyPrice;

    @ExcelProperty("销售单位（对应数据字典）")
    private Long saleUnit;

    @ExcelProperty("拆零标志：0否 1是")
    private Integer splitFlag;

    @ExcelProperty("拆零价格")
    private BigDecimal splitPrice;

    @ExcelProperty("拆零单位（对应数据字典）")
    private Long splitUnit;

    @ExcelProperty("拆零比例")
    private BigDecimal splitRatio;


    @ExcelProperty("税率(%)")
    private BigDecimal taxRate;

    @ExcelProperty("是否特价：0否 1是")
    private Integer isSpecialPrice;

    @ExcelProperty("建议零售价")
    private BigDecimal suggestRetailPrice;

    @ExcelProperty("单价加价系数")
    private BigDecimal priceCoefficient;

    @ExcelProperty("首营供应商")
    private String firstSupplierId;

    @ExcelProperty("最高零售价")
    private BigDecimal maxRetailPrice;

    @ExcelProperty("批发价")
    private BigDecimal wholesalePrice;

    @ExcelProperty("出厂价")
    private BigDecimal factoryPrice;

    @ExcelProperty("集采价")
    private BigDecimal collectPrice;

    @ExcelProperty("大分类属性（对应数据字典）")
    private Long mainCategoryAttr;

    @ExcelProperty("功能属性分类（对应数据字典）")
    private Long funcAttr;

    @ExcelProperty("给药途径属性 1-内服 2-外服")
    private Integer adminRouteAttr;

    @ExcelProperty("中西药属性 1-中药 2-西药")
    private Integer chineseWesternAttr;

    @ExcelProperty("基药属性 1-基药 2-非基药")
    private Integer baseDrugAttr;

    @ExcelProperty("ABC属性（对应数据字典）")
    private Long abcAttr;

    @ExcelProperty("经营关注属性（对应数据字典）")
    private Long businessAttr;

    @ExcelProperty("商品详细分类（对应数据字典）")
    private Long goodsDetailClassify;

    @ExcelProperty("药品其他属性3（对应数据字典）")
    private Long drugAttr3;

    @ExcelProperty("药品其他属性4（对应数据字典）")
    private Long drugAttr4;

    @ExcelProperty("药品其他属性5（对应数据字典）")
    private Long drugAttr5;

    @ExcelProperty("药品其他属性6（对应数据字典）")
    private Long drugAttr6;

    @ExcelProperty("药品其他属性7（对应数据字典）")
    private Long drugAttr7;

    @ExcelProperty("药品其他属性8（对应数据字典）")
    private Long drugAttr8;

    @ExcelProperty("药品其他属性9（对应数据字典）")
    private Long drugAttr9;

    @ExcelProperty("药品其他属性10（对应数据字典）")
    private Long drugAttr10;

    @ExcelProperty("兴奋剂成分 0-不包含 1-包含 ")
    private Integer stimulantAttr;

    @ExcelProperty("英文名")
    private String englishName;

    @ExcelProperty("化学名")
    private String chemicalName;

    @ExcelProperty("拉丁名")
    private String latinName;

    @ExcelProperty("剂量")
    private String dosage;

    @ExcelProperty("用量单位（对应数据字典）")
    private Long doseUnit;

    @ExcelProperty("频次")
    private String frequency;

    @ExcelProperty("单周期天数")
    private Integer cycleDays;

    @ExcelProperty("单周期用药次数")
    private Integer cycleTimes;

    @ExcelProperty("生效开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date effectStartDate;

    @ExcelProperty("生效结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date effectEndDate;

    @ExcelProperty("扩展类型（对应数据字典）")
    private Long extendType;

    @ExcelProperty("用法用量")
    private String usageDosage;

    @ExcelProperty("炮制方法")
    private String processingMethod;

    @ExcelProperty("功效")
    private String efficacy;

    @ExcelProperty("药材科 (族) 来源")
    private String herbFamilySource;

    @ExcelProperty("药材种来源")
    private String herbSpeciesSource;

    @ExcelProperty("药用部位")
    private String medicinalPart;

    @ExcelProperty("性状")
    private String propertyDescription;

    @ExcelProperty("主要成分")
    private String mainIngredients;

    @ExcelProperty("主治功能")
    private String mainFunction;

    @ExcelProperty("适应症")
    private String indications;

    @ExcelProperty("状态 1-正常 0 -停用")
    private Integer status;

}
