package ${packageName}.${moduleName}.${businessName}.domain.params;

import com.zs.common.core.page.BasePageParams;
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
@Schema(description = "${functionName}ageQueryParams对象")
public class ${ClassName}PageQueryParams  extends BasePageParams implements Serializable {

<#list columnList as column>
<#if column.isQuery == '1'>
    @Schema(description = "${column.columnComment!}")
    private ${column.javaType!} ${column.javaField!};

</#if>
</#list>
}
