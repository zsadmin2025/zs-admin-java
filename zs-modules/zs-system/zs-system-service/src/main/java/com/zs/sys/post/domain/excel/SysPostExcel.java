package com.zs.sys.post.domain.excel;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
@ExcelIgnoreUnannotated
public class SysPostExcel {

    @ExcelProperty("所属部门")
    private String deptName;
    @ExcelProperty("岗位名称")
    private String postName;
}
