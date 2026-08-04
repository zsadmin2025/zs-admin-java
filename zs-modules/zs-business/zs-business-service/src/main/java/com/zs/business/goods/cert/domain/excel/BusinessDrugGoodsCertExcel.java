package com.zs.business.goods.cert.domain.excel;

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
 * 商品证照附件
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-03 11:09:18
 */
@Getter
@Setter
@Schema(description = "商品证照附件Excel对象")
@ExcelIgnoreUnannotated
public class BusinessDrugGoodsCertExcel {

    @ExcelProperty("主键ID")
    private Long drugGoodsCertId;

    @ExcelProperty("关联药品商品ID，关联business_drug_goods.drug_goods_id")
    private Long drugGoodsId;

    @ExcelProperty("证件名称")
    private String certName;

    @ExcelProperty("证件编号")
    private String certNo;

    @ExcelProperty("有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date validEndDate;

    @ExcelProperty("附件文件地址")
    private String fileUrl;

}
