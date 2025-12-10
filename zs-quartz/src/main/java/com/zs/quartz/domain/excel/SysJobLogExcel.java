package com.zs.quartz.domain.excel;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ExcelIgnoreUnannotated
public class SysJobLogExcel {

    @ExcelProperty("任务类")
    private String jobClass;
    @ExcelProperty("任务名称")
    private String jobName;
    @ExcelProperty("任务组")
    private String jobGroup;
    @ExcelProperty("任务描述")
    private int status;
    @ExcelProperty("异常信息")
    private String exceptionInfo;
    @ExcelProperty("开始时间")
    private Date startTime;
    @ExcelProperty("结束时间")
    private Date endTime;
    @ExcelProperty("耗时")
    private Long duration;
}
