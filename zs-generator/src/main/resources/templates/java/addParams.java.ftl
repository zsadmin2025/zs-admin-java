package ${packageName}.${moduleName}.${businessName}.domain.params;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

/**
 * <p>
 * ${table.comment!}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Getter
@Setter
@Schema(description = "${functionName}AddParams对象")
public class ${ClassName}AddParams implements Serializable {

<#-- 定义需要忽略的字段集合 -->
<#assign ignoredFields = ["creator", "createTime", "updater", "updateTime"]>

<#list columnList as column>
    <#if !ignoredFields?seq_contains(column.javaField)>
    @Schema(description = "${column.columnComment!}")
    private ${column.javaType!} ${column.javaField!};
    </#if>

</#list>
}
