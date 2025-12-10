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
@Schema(description = "${functionName}electQueryParams对象")
public class ${ClassName}SelectQueryParams implements Serializable {

<#list columnList as column>
<#if column.isQuery == '1'>
    @Schema(description = "${column.columnComment!}")
    private ${column.javaType!} ${column.javaField!};

</#if>
</#list>
}
