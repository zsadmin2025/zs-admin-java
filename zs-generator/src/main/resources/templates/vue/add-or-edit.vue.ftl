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
        <#if column.htmlType == 'input' || column.htmlType == ''>
          <a-input v-model="form.${column.javaField!}" placeholder="请输入${column.columnComment!}" :max-length="${column.columnLength!}"/>
        <#elseif column.htmlType == 'textarea'>
          <a-textarea v-model="form.${column.javaField!}" placeholder="请输入${column.columnComment!}" :rows="3" />
        <#elseif column.htmlType == 'number'>
          <a-input-number v-model="form.${column.javaField!}" placeholder="请输入${column.columnComment!}" />
        <#elseif column.htmlType == 'select'>
          <a-select v-model="form.${column.javaField!}" placeholder="请选择${column.columnComment!}" allow-clear>
            <a-option v-for="item in ${column.dictType!}Options" :key="item.dictValue" :value="item.dictValue">
              {{ item.dictLabel }}
            </a-option>
          </a-select>
        <#elseif column.htmlType == 'radio'>
          <a-radio-group v-model="form.${column.javaField!}" placeholder="请选择${column.columnComment!}">
            <a-radio v-for="item in ${column.dictType!}Options" :key="item.dictValue" :value="item.dictValue">
              {{ item.dictLabel }}
            </a-radio>
          </a-radio-group>
        <#elseif column.htmlType == 'checkbox'>
          <a-checkbox-group v-model="form.${column.javaField!}" placeholder="请选择${column.columnComment!}">
            <a-checkbox v-for="item in ${column.dictType!}Options" :key="item.dictValue" :value="item.dictValue">
              {{ item.dictLabel }}
            </a-checkbox>
          </a-checkbox-group>
        <#elseif column.htmlType == 'date'>
          <a-date-picker v-model="form.${column.javaField!}" placeholder="请选择${column.columnComment!}" value-format="YYYY-MM-DD" />
        <#elseif column.htmlType == 'datetime'>
          <a-date-picker v-model="form.${column.javaField!}" placeholder="请选择${column.columnComment!}" value-format="YYYY-MM-DD HH:mm:ss" show-time />
        <#elseif column.htmlType == 'time'>
          <a-time-picker v-model="form.${column.javaField!}" placeholder="请选择${column.columnComment!}" format="HH:mm:ss" />
        <#elseif column.htmlType == 'image' || column.htmlType == 'upload'>
          <a-input v-model="form.${column.javaField!}" placeholder="请输入${column.columnComment!}" />
        <#elseif column.htmlType == 'editor'>
          <ZsEditor v-model="form.${column.javaField!}" @change="(val) => form.${column.javaField!} = val" />
        <#else>
          <a-input v-model="form.${column.javaField!}" placeholder="请输入${column.columnComment!}" />
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
  import { ref, onMounted } from 'vue';
  import { use${BusinessName}AddOrEditStore } from '@/store/modules/${moduleName}/${businessName}/${businessName}AddOrEditStore';
  <#assign dictTypes = []>
  <#list columnList as column>
  <#if column.dictType?? && column.dictType != "" && !dictTypes?seq_contains(column.dictType)>
    <#assign dictTypes = dictTypes + [column.dictType]>
  </#if>
  </#list>
  <#if dictTypes?has_content>
  import useDict from '@/hooks/dict';
  import { DictData } from '@/types/sys/dict/DictData';
  
  <#list dictTypes as dictType>
  const ${dictType}Options = ref<DictData[]>([]);
  </#list>
  
  async function loadDicts() {
    <#list dictTypes as dictType>
    ${dictType}Options.value = await useDict('${dictType}');
    </#list>
  }
  
  onMounted(() => {
    loadDicts();
  });
  </#if>

  const ${businessName}AddOrEditStore = use${BusinessName}AddOrEditStore();
  const { form, dialogFormVisible, loading, formRef, rules } = storeToRefs(${businessName}AddOrEditStore);

  const emits = defineEmits(['refresh']);

  defineExpose({
    init: ${businessName}AddOrEditStore.init,
    form,
  });
</script>
