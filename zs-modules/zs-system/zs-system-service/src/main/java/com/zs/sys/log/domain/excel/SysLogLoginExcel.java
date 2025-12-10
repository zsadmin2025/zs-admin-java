package com.zs.sys.log.domain.excel;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author zsadmin
 */
@Data
@ExcelIgnoreUnannotated
public class SysLogLoginExcel {

    @ExcelProperty("登录用户名")
    private String username;

    @ExcelProperty("登录时间")
    private String loginTime;

    @ExcelProperty("登录IP")
    private String ipAddress;

    @ExcelProperty("登录地点")
    private String city;

    @ExcelProperty("代理")
    private String userAgent;

    @ExcelProperty("登录状态")
    private Integer loginStatus;

    @ExcelProperty("失败原因")
    private String failureReason;

    @ExcelProperty("登录方式")
    private Integer loginMethod;

    @ExcelProperty("登录来源")
    private String loginSource;

    @ExcelProperty("登录设备")
    private String browser;

    @ExcelProperty("操作系统")
    private String os;
}
