package com.zs.sys.user.domain.excel;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import com.zs.common.core.excel.converter.GenderConverter;
import lombok.Data;

/**
 * @author zsadmin
 */
@Data
@ExcelIgnoreUnannotated
public class SysUserExcel {

    private Long sysUserId;
    @ExcelProperty("登录用户名")
    private String username;
    @ExcelProperty("真实姓名")
    private String realName;
    @ExcelProperty("手机号")
    private String phone;
    @ExcelProperty("邮箱")
    private String email;
    @ExcelProperty("年龄")
    private Integer age;
    @ExcelProperty(value = "性别", converter = GenderConverter.class)
    private Integer sex;
    @ExcelProperty("状态")
    private Integer status;
    @ExcelProperty("所属部门")
    private String deptName;
    @ExcelProperty("职位")
    private String postName;
}
