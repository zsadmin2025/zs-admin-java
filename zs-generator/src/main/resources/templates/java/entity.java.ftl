package ${packageName}.${moduleName}.${businessName}.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

<#-- 定义需要忽略的字段集合 -->
<#assign ignoredFields = ["creator", "createTime", "updater", "updateTime", "creatorDept", "tenantId"]>

<#assign hasBigDecimalField = false>
<#list columnList as column>
  <#if !ignoredFields?seq_contains(column.javaField)>
    <#if column.javaType == 'Date'>
      <#assign hasDateField = true>
    </#if>
    <#if column.javaType == 'BigDecimal'>
      <#assign hasBigDecimalField = true>
    </#if>
  </#if>
</#list>
<#if hasBigDecimalField>
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
@TableName("${tableName!}")
@Schema(description = "${functionName}Entity对象")
public class ${ClassName}Entity extends BaseEntity {

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list columnList as column>
  <#if !ignoredFields?seq_contains(column.javaField)>
    /**  ${column.columnComment!} */
    <#if column.isPk == '1'>
    @TableId
    </#if>
    private ${column.javaType!} ${column.javaField!};

  </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
}
