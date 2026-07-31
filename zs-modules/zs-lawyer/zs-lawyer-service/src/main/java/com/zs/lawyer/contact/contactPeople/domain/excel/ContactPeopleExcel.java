package com.zs.lawyer.contact.contactPeople.domain.excel;

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
 * @since 2025-08-26 10:42:27
 */
@Getter
@Setter
@Schema(description = "通讯录联系人Excel对象")
@ExcelIgnoreUnannotated
public class ContactPeopleExcel {

    @ExcelProperty("")
    private Long contactPeopleId;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("联系电话")
    private String phone;

    @ExcelProperty("性别")
    private String sex;

    @ExcelProperty("关联分类表ID")
    private Long contactCategoryId;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("工作单位")
    private String placeWork;

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
