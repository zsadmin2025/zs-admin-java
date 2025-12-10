<template>
  <a-modal
    v-model:visible="dialogFormVisible"
    width="50%"
    title-align="start"
    :draggable="true"
    @cancel="${businessName}AddOrEditStore.close"
  >
    <template #title>
      <h4>
        {{ !form.${className}Id ? '新增${functionName}' : '修改${functionName}' }}
      </h4>
    </template>
    <a-form ref="formRef" :model="form" :rules="rules" auto-label-width>
    <#list columnList as column>
    <#if column.isInsert == '1'>
      <a-form-item label="${column.columnComment!}" field="${column.javaField!}">
        <#if column.htmlType == 'input'>
        <a-input v-model="form.${column.javaField!}" placeholder="请输入${column.columnComment!}" />
        <#elseif column.htmlType == 'textarea'>
        <a-textarea v-model="form.${column.javaField!}" placeholder="请输入${column.columnComment!}" rows="2" />
        <#elseif column.htmlType == 'select'>
        <a-select v-model="form.${column.javaField!}" placeholder="请选择${column.columnComment!}">
          <a-option :value="0">0</a-option>
          <a-option :value="1">1</a-option>
        </a-select>
        <#elseif column.htmlType == 'checkbox'>
        <a-checkbox-group v-model="form.${column.javaField!}">
          <a-checkbox value="0">0</a-checkbox>
          <a-checkbox value="1">1</a-checkbox>
        </a-checkbox-group>
        <#elseif column.htmlType == 'radio'>
        <a-radio-group v-model="form.${column.javaField!}">
          <a-radio value="0">0</a-radio>
          <a-radio value="1">1</a-radio>
        </a-radio-group>
        <#elseif column.htmlType == 'datetime'>
        <a-date-picker v-model="form.${column.javaField!}" />
        </#if>
      </a-form-item>
    </#if>
    </#list>
    </a-form>
    <template #footer>
      <a-space>
        <a-button @click="${businessName}AddOrEditStore.close">取消</a-button>
        <a-button
          type="primary"
          :loading="loading"
          @click="${businessName}AddOrEditStore.submit(emits)"
        >
          确定
        </a-button>
      </a-space>
    </template>
  </a-modal>
</template>

<script lang="ts" setup>
    import {storeToRefs} from 'pinia';

    const ${businessName}AddOrEditStore = use${BusinessName}AddOrEditStore();
  const { form, dialogFormVisible, loading, formRef, rules } =
    storeToRefs(${businessName}AddOrEditStore);

  const emits = defineEmits(['refresh']);

  defineExpose({
    init: ${businessName}AddOrEditStore.init,
    form,
  });
</script>
