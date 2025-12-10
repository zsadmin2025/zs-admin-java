import { ResponseData } from '@/types/global';

export interface ${BusinessName}PageParams {
  current: number;
  pageSize: number;
  [key: string]: any; // 支持额外参数
}
export interface ${BusinessName} {
<#list columnList as column>
<#if column.isList == '1'>
  <#if column.javaType == 'String'>
  ${column.javaField!}: string;
  <#elseif column.javaType == 'Integer'>
  ${column.javaField!}: number;
  <#elseif column.javaType == 'Long'>
  ${column.javaField!}: string;
  <#elseif column.javaType == 'Boolean'>
  ${column.javaField!}: boolean;
  <#else>
  ${column.javaField!}: any;
  </#if>
</#if>
</#list>
}
export interface ${BusinessName}State {
  addEditRef: any;
  ruleFormRef: any;
  list: ${BusinessName}[];
  total: number;
  loading: boolean;
  selectedKeys: number[];
  form: ${BusinessName}PageParams;
}

export interface ${BusinessName}ListRes extends ResponseData {
  list: ${BusinessName}[];
  total: number;
}

export interface ${BusinessName}AddOrEditForm {
<#list columnList as column>
<#if column.isInsert == '1'>
  <#if column.javaType == 'String'>
  ${column.javaField!}: string;
  <#elseif column.javaType == 'Integer'>
  ${column.javaField!}: number;
  <#elseif column.javaType == 'Long'>
  ${column.javaField!}: string;
  <#elseif column.javaType == 'Boolean'>
  ${column.javaField!}: boolean;
  <#else>
  ${column.javaField!}: any;
  </#if>
</#if>
</#list>
}

// 定义状态类型
export interface ${BusinessName}AddOrEditState {
  dialogFormVisible: boolean;
  loading: boolean;
  formRef: any;
  form: ${BusinessName}AddOrEditForm;
}
