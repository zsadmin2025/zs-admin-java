package com.zs.sys.role.domain.excel;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
@ExcelIgnoreUnannotated
public class SysRoleExcel {

    @ExcelProperty("角色名称")
    private String roleName;
    @ExcelProperty("角色编码")
    private String roleCode;

}
