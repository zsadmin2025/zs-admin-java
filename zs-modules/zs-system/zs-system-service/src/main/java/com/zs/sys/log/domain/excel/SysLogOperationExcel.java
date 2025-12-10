package com.zs.sys.log.domain.excel;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author zsadmin
 */
@Data
@ExcelIgnoreUnannotated
public class SysLogOperationExcel {

    @ExcelProperty("登录用户名")
    private String username;

    @ExcelProperty("所属模块")
    private String module;

    @ExcelProperty("请求IP地址")
    private String ipAddress;

    @ExcelProperty("操作类型")
    private String operationType;

    @ExcelProperty("操作描述")
    private String operationDescription;

    @ExcelProperty("请求方法")
    private String requestMethod;

    @ExcelProperty("请求路径")
    private String requestPath;

    @ExcelProperty("请求参数")
    private String requestParams;

    @ExcelProperty("响应状态码")
    private Integer responseStatusCode;

    @ExcelProperty("响应消息")
    private String responseMessage;

    @ExcelProperty("操作耗时(ms)")
    private Integer operationDuration;

    @ExcelProperty("创建时间")
    private String createTime;
}
