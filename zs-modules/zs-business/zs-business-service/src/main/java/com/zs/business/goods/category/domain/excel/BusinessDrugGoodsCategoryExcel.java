package com.zs.business.goods.category.domain.excel;

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
 * 商品档案
 * </p>
 *
 * @author zs
 * {@code @date} 2026-07-31 19:22:03
 */
@Getter
@Setter
@Schema(description = "商品档案Excel对象")
@ExcelIgnoreUnannotated
public class BusinessDrugGoodsCategoryExcel {

    @ExcelProperty("")
    private Long categoryId;

    @ExcelProperty("类别名称")
    private String categoryName;

    @ExcelProperty("状态 1-正常 0 -停用")
    private Integer status;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建者")
    private Long creator;

    @ExcelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
