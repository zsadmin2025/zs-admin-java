package com.zs.lawyer.contact.contactCategory.domain.excel;

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
 * @since 2025-08-26 10:34:29
 */
@Getter
@Setter
@Schema(description = "通讯录分类Excel对象")
@ExcelIgnoreUnannotated
public class ContactCategoryExcel {

    @ExcelProperty("")
    private Long contactCategoryId;

    @ExcelProperty("父ID")
    private Long pid;

    @ExcelProperty("所有上级ID，用逗号分开")
    private String pids;

    @ExcelProperty("部门名称")
    private String categoryName;

    @ExcelProperty("是否公共分类 0-否 1-是")
    private Long isPublic;

    @ExcelProperty("部门描述")
    private String remark;

    @ExcelProperty("创建者")
    private Long creator;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("更新者")
    private Long updater;

    @ExcelProperty("更新时间")
    private Date updateTime;

    @ExcelProperty("创建部门")
    private String creatorDept;

    @ExcelProperty("租户id")
    private String tenantId;

}
