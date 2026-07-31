package com.zs.lawyer.application.contact.domain.excel;

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
 * 通讯录联系人表
 * </p>
 *
 * @author zs
 * {@code @date} 2026-07-30 18:37:41
 */
@Getter
@Setter
@Schema(description = "通讯录联系人表Excel对象")
@ExcelIgnoreUnannotated
public class ContactExcel {

    @ExcelProperty("主键ID")
    private Long contactId;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("联系电话（支持固话、手机号）")
    private String phone;

    @ExcelProperty("性别 0-未知 1-男 2-女")
    private Integer gender;

    @ExcelProperty("分组：内部、客户")
    private String groupType;

    @ExcelProperty("归属范围：公共、我的")
    private String scopeType;

    @ExcelProperty("备注信息")
    private String remark;

}
