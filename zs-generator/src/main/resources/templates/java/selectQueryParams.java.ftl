package ${packageName}.${moduleName}.${businessName}.domain.params;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
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
@Schema(description = "${functionName}electQueryParams对象")
public class ${ClassName}SelectQueryParams implements Serializable {

<#list columnList as column>
<#if column.isQuery == '1'>
    <#if column.queryType == 'BETWEEN'>
    @Schema(description = "${column.columnComment!}开始值")
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
    private ${column.javaType!} ${column.javaField!}Start;

    @Schema(description = "${column.columnComment!}结束值")
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
    private ${column.javaType!} ${column.javaField!}End;

    <#else>
    @Schema(description = "${column.columnComment!}")
    private ${column.javaType!} ${column.javaField!};

    </#if>
</#if>
</#list>
}
