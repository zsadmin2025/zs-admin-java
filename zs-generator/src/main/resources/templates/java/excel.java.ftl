package ${packageName}.${moduleName}.${businessName}.domain.excel;

import lombok.Getter;
import lombok.Setter;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
<#-- 动态判断是否需要导入 Date、BigDecimal、DateTimeFormat、JsonFormat -->
<#assign hasDate = false>
<#assign hasBigDecimal = false>
<#list columnList as column>
    <#if column.javaType == "Date">
        <#assign hasDate = true>
    </#if>
    <#if column.javaType == "BigDecimal">
        <#assign hasBigDecimal = true>
    </#if>
</#list>

<#if hasDate>
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
</#if>
<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>
/**
 * <p>
 * ${table.comment!}
 * </p>
 *
 * @author ${author}
 * {@code @date} ${date}
 */
@Getter
@Setter
@Schema(description = "${functionName}Excel对象")
@ExcelIgnoreUnannotated
public class ${ClassName}Excel {

<#list columnList as column>
<#if column.isExport == '1'>
    @ExcelProperty("${column.columnComment!}")
     <#if column.javaType == "Date" && column.columnType == "date">
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    </#if>
    <#if column.javaType == "Date" && column.columnType == "datetime">
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    </#if>
    <#if column.javaType == "Date" && column.columnType == "time">
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    </#if>
    private ${column.javaType!} ${column.javaField!};

</#if>
</#list>
}
