import { defineStore } from 'pinia';
import { ${className}Api } from '@/api/${moduleName}/${businessName}/${businessName}';
import { ${BusinessName}AddOrEditState } from '@/types/${moduleName}/${businessName}/${businessName}Types';
import { Message } from '@arco-design/web-vue';

export const use${BusinessName}AddOrEditStore = defineStore('${className}AddOrEdit', {
  state: (): ${BusinessName}AddOrEditState => {
    return {
      dialogFormVisible: false,
      loading: false,
      formRef: ref(null),
      form: {
      <#list columnList as column>
      <#if column.isPk == '1'>
        ${column.javaField!}: '',
      </#if>
      <#if column.isInsert == '1'>
        <#if column.javaType == 'String'>
        ${column.javaField!}: '',
        </#if>
        <#if column.javaType == 'BigDecimal'>
        ${column.javaField!}: 0,
        </#if>
        <#if column.javaType == 'Integer'>
        ${column.javaField!}: null,
        </#if>
        <#if column.javaType == 'Long'>
        ${column.javaField!}: '',
        </#if>
        <#if column.javaType == 'Date'>
        ${column.javaField!}: null,
        </#if>
        <#if column.javaType == 'Double'>
        ${column.javaField!}: 0.0,
        </#if>
        <#if column.javaType == 'Boolean'>
        ${column.javaField!}: false,
        </#if>
        </#if>
      </#list>
      },
    };
  },
  getters: {
    rules() {
      return {
      <#list columnList as column>
        <#if column.isRequired == '1'>
        ${column.javaField!}: [
          { required: true, message: '请选择${column.columnComment!}', trigger: <#if column.htmlType == 'select'>'change'<#else>'blur'</#if> },
        ],
        </#if>
      </#list>
      };
    },
  },
  actions: {
    init() {
      this.dialogFormVisible = true;
      if (this.form.${className}Id) {
        nextTick(async () => {
          await this.getInfoById();
        });
      }
    },
    async getInfoById() {
      const data = await ${className}Api.getById(this.form.${className}Id);
      Object.assign(this.form, data?.data);
    },
    close() {
      this.formRef.resetFields();
      this.dialogFormVisible = false;
    },
    async submit(emits: (event: 'refresh') => void) {
      // 防止重复提交
      if (this.loading) return;

      try {
        if (!this.formRef) return;

        if (await this.formRef.validate()) {
          return;
        }
        this.loading = true;
        const action = this.form.${className}Id ? ${className}Api.edit : ${className}Api.save;
        await action(this.form);
        this.close();
        this.dialogFormVisible = false;
        this.loading = false;
        emits('refresh');
      } catch (error) {
        const errorMessage =
          error instanceof Error ? error.message : '未知错误';
        Message.error(errorMessage);
      } finally {
        this.loading = false;
      }
    },
  },
});
