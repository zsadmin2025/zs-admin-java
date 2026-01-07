package ${packageName}.${moduleName}.${businessName}.domain.params;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Schema(description = "${functionName}updateParams对象")
public class ${ClassName}UpdateParams implements Serializable {

<#list columnList as column>
    <#if column.isEdit == '1'>
    @Schema(description = "${column.columnComment!}")
    <#if column.isRequired == '1'>
        <#if column.javaType == 'String'>
    @NotBlank(message = "${column.columnComment!}不能为空")
        <#else>
    @NotNull(message = "${column.columnComment!}不能为空")
        </#if>
    </#if>
    <#if column.javaType == 'String' && column.columnLength??>
    @Size(max = ${column.columnLength}, message = "${column.columnComment!}长度不能超过${column.columnLength}")
    </#if>
    <#-- 可以根据需要添加更多校验注解，如@Size、@Min、@Max等 -->
    <#if column.javaType == "Date">
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    </#if>
    private ${column.javaType!} ${column.javaField!};
    </#if>

</#list>
}
