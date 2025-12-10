package ${packageName}.${moduleName}.${businessName}.domain.excel;

import lombok.Getter;
import lombok.Setter;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
/**
 * <p>
 * $!{table.comment}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Getter
@Setter
@Schema(description = "${functionName}Excel对象")
@ExcelIgnoreUnannotated
public class ${ClassName}Excel {

		<#list columnList as column>
    @ExcelProperty("${column.columnComment!}")
    private ${column.javaType!} ${column.javaField!};

    </#list>
}
