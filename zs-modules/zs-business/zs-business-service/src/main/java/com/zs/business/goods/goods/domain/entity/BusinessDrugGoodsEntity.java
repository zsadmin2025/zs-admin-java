package com.zs.business.goods.goods.domain.entity;


import cn.idev.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

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
@TableName("business_drug_goods")
@Schema(description = "药品商品主信息表Entity对象")
public class BusinessDrugGoodsEntity extends BaseEntity {

    /**  主键ID */
    @TableId
    private Long drugGoodsId;

    /**  商品类别id */
    private Long goodsCategoryId;

    /**  特殊商品管理：0-否 1-是 */
    private Integer specialGoodsType;

    /**  商品货号 */
    private String goodsSn;

    /**  剂型（对应数据字典） */
    private Long dosageFormId;

    /**  通用名 */
    private String commonName;

    /**  商品名称 */
    private String goodsName;

    /**  单位（对应数据字典） */
    private Long unit;

    /**  追溯码 */
    private String traceCode;

    /**  生产厂家(编码/名称) */
    private Long manufacturerId;

    /**  产地 */
    private String originPlace;

    /**  货区/货位 */
    private Long storeLocationId;

    /**  规格 */
    private String spec;

    /**  商品拼音码 */
    private String goodsPinyin;

    /**  条形码 */
    private String barcode;

    /**  通用名拼音码 */
    private String commonNamePinyin;

    /**  上市许可人 */
    private String marketingAuthorizationHolder;

    /**  是否医保用药：0否 1是 */
    private Integer isMedicalInsurance;

    /**  是否中药：0否 1是 */
    private Integer isChineseMedicine;

    /**  商品分类id */
    private Long goodsClassifyId;

    /**  处方标志：1-处方 2-非处方 */
    private Integer prescriptionFlag;

    /**  批准文号 */
    private String approvalNo;

    /**  收费等级(对应数据字典) */
    private Integer chargeLevel;

    /**  近效期预警天数 */
    private Integer expireWarnDays;

    /**  最近进价 */
    private BigDecimal latestPurchasePrice;

    /**  药品有效期 */
    private Date drugValidDate;

    /**  档案号 */
    private String fileNo;

    /**  最近供应商id */
    private Long latestSupplierId;

    /**  批准文号有效期 */
    private Date approvalValidEnd;

    /**  商品出售包装形式,1-整合出售 2-散装出售 */
    private Integer salePackageType;

    /**  大包装数量 */
    private Integer bigPackageNum;

    /**  大包装单位(对应数据字典) */
    private Long bigPackageUnit;

    /**  中包装数量 */
    private BigDecimal midPackageNum;

    /**  中包装单位(对应数据字典) */
    private Long midPackageUnit;

    /**  小包装数量 */
    private BigDecimal smallPackageNum;

    /**  小包装单位(对应数据字典) */
    private Long smallPackageUnit;

    /**  国家编码 */
    private String nationalCode;

    /**  省级编码 */
    private String provinceCode;

    /**  标准价 */
    private BigDecimal standardPrice;

    /**  供货价 */
    private BigDecimal supplyPrice;

    /**  销售单位（对应数据字典） */
    private Long saleUnit;

    /**  拆零标志：0否 1是 */
    private Integer splitFlag;

    /**  拆零价格 */
    private BigDecimal splitPrice;

    /**  拆零单位 */
    private Long splitUnit;

    /**  拆零比例 */
    private BigDecimal splitRatio;

    /**  税率(%) */
    private BigDecimal taxRate;

    /**  是否特价：0否 1是 */
    private Integer isSpecialPrice;

    /**  建议零售价 */
    private BigDecimal suggestRetailPrice;

    /**  单价加价系数 */
    private BigDecimal priceCoefficient;

    /**  首营供应商 */
    private String firstSupplierId;

    /**  最高零售价 */
    private BigDecimal maxRetailPrice;

    /**  批发价 */
    private BigDecimal wholesalePrice;

    /**  出厂价 */
    private BigDecimal factoryPrice;

    /**  集采价 */
    private BigDecimal collectPrice;

    /**  大分类属性（对应数据字典） */
    private Long mainCategoryAttr;

    /**  功能属性分类（对应数据字典） */
    private Long funcAttr;

    /**  给药途径属性 1-内服 2-外服 */
    private Integer adminRouteAttr;

    /**  中西药属性 1-中药 2-西药 */
    private Integer chineseWesternAttr;

    /**  基药属性 1-基药 2-非基药 */
    private Integer baseDrugAttr;

    /**  ABC属性（对应数据字典） */
    private Long abcAttr;

    /**  经营关注属性（对应数据字典） */
    private Long businessAttr;

    /**  商品详细分类（对应数据字典） */
    private Long goodsDetailClassify;

    /**  药品其他属性3（对应数据字典） */
    private Long drugAttr3;

    /**  药品其他属性4（对应数据字典） */
    private Long drugAttr4;

    /**  药品其他属性5（对应数据字典） */
    private Long drugAttr5;

    /**  药品其他属性6（对应数据字典） */
    private Long drugAttr6;

    /**  药品其他属性7（对应数据字典） */
    private Long drugAttr7;

    /**  药品其他属性8（对应数据字典） */
    private Long drugAttr8;

    /**  药品其他属性9（对应数据字典） */
    private Long drugAttr9;

    /**  药品其他属性10（对应数据字典） */
    private Long drugAttr10;

    /**  兴奋剂成分 0-不包含 1-包含  */
    private Integer stimulantAttr;

    /**  英文名 */
    private String englishName;

    /**  化学名 */
    private String chemicalName;

    /**  拉丁名 */
    private String latinName;

    /**  剂量 */
    private String dosage;

    /**  用量单位（对应数据字典） */
    private Long doseUnit;

    /**  频次 */
    private String frequency;

    /**  单周期天数 */
    private Integer cycleDays;

    /**  单周期用药次数 */
    private Integer cycleTimes;

    /**  生效开始时间 */
    private Date effectStartDate;

    /**  生效结束时间 */
    private Date effectEndDate;

    /**  扩展类型（对应数据字典） */
    private Long extendType;

    /**  用法用量 */
    private String usageDosage;

    /**  炮制方法 */
    private String processingMethod;

    /**  功效 */
    private String efficacy;

    /**  药材科 (族) 来源 */
    private String herbFamilySource;

    /**  药材种来源 */
    private String herbSpeciesSource;

    /**  药用部位 */
    private String medicinalPart;

    /**  性状 */
    private String propertyDescription;

    /**  主要成分 */
    private String mainIngredients;

    /**  主治功能 */
    private String mainFunction;

    /**  适应症 */
    private String indications;

    /**  状态 1-正常 0 -停用 */
    private Integer status;


}
