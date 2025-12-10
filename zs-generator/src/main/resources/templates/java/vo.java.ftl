package ${packageName}.${moduleName}.${businessName}.domain.vo;

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
@Schema(description = "${functionName}VO对象")
public class ${ClassName}VO implements Serializable {

<#list columnList as column>
    @Schema(description = "${column.columnComment!}")
    private ${column.javaType!} ${column.javaField!};

</#list>
}
