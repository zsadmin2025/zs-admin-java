package com.zs.business.partner.category.domain.excel;

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
 * 单位分类
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-02 09:15:47
 */
@Getter
@Setter
@Schema(description = "单位分类Excel对象")
@ExcelIgnoreUnannotated
public class BusinessPartnerCategoryExcel {

    @ExcelProperty("")
    private Long businessPartnerCategoryId;

    @ExcelProperty("类别名称")
    private String partnerCategoryName;

    @ExcelProperty("状态")
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
