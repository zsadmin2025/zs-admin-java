<template>
  <div>
    <zs-container layout="header-main-footer">
      <template #header>
        <a-row :gutter="16">
          <a-col :flex="1">
            <a-form :model="form" :auto-label-width="true" label-align="left">
              <a-row :gutter="16">
              <#list columnList as column>
                <#if column.isQuery == '1'>
                <a-col :span="6">
                  <a-form-item field="${column.javaField!}" label="${column.columnComment!}">
                    <#if column.htmlType == 'input' || column.htmlType == ''>
                      <a-input v-model="form.${column.javaField!}" placeholder="${column.columnComment!}" />
                    <#elseif column.htmlType == 'textarea'>
                      <a-textarea v-model="form.${column.javaField!}" placeholder="${column.columnComment!}" :rows="3" />
                    <#elseif column.htmlType == 'number'>
                      <a-input-number v-model="form.${column.javaField!}" placeholder="${column.columnComment!}" />
                    <#elseif column.htmlType == 'select'>
                      <a-select v-model="form.${column.javaField!}" placeholder="${column.columnComment!}">
                        <a-option v-for="item in ${column.dictType!}Options" :key="item.dictValue" :value="item.dictValue">
                          {{ item.dictLabel }}
                        </a-option>
                      </a-select>
                    <#elseif column.htmlType == 'radio'>
                      <a-radio-group v-model="form.${column.javaField!}" placeholder="${column.columnComment!}">
                        <a-radio v-for="item in ${column.dictType!}Options" :key="item.dictValue" :value="item.dictValue">
                          {{ item.dictLabel }}
                        </a-radio>
                      </a-radio-group>
                    <#elseif column.htmlType == 'checkbox'>
                      <a-checkbox-group v-model="form.${column.javaField!}" placeholder="${column.columnComment!}">
                        <a-checkbox v-for="item in ${column.dictType!}Options" :key="item.dictValue" :value="item.dictValue">
                          {{ item.dictLabel }}
                        </a-checkbox>
                      </a-checkbox-group>
                    <#elseif column.htmlType == 'date'>
                      <a-date-picker v-model="form.${column.javaField!}" placeholder="${column.columnComment!}" value-format="YYYY-MM-DD" />
                    <#elseif column.htmlType == 'datetime'>
                      <a-date-picker v-model="form.${column.javaField!}" placeholder="${column.columnComment!}" value-format="YYYY-MM-DD HH:mm:ss" show-time />
                    <#elseif column.htmlType == 'time'>
                      <a-time-picker v-model="form.${column.javaField!}" placeholder="${column.columnComment!}" format="HH:mm:ss" />
                    <#elseif column.htmlType == 'image' || column.htmlType == 'upload'>
                      <a-input v-model="form.${column.javaField!}" placeholder="${column.columnComment!}" />
                    <#else>
                      <a-input v-model="form.${column.javaField!}" placeholder="${column.columnComment!}" />
                    </#if>
                  </a-form-item>
                </a-col>
                </#if>
              </#list>
              </a-row>
            </a-form>
          </a-col>
          <a-col :flex="'86px'" style="text-align: right">
            <a-space :size="18">
              <a-button type="primary" @click="${businessName}Store.fetchData">
                <template #icon>
                  <icon-search />
                </template>
                {{ $t('searchTable.form.search') }}
              </a-button>
              <a-button @click="${businessName}Store.reset">
                <template #icon>
                  <icon-refresh />
                </template>
                {{ $t('searchTable.form.reset') }}
              </a-button>
            </a-space>
          </a-col>
        </a-row>
      </template>
      <template #main-header>
        <a-row justify="space-between" align="center">
          <a-col :span="12">
            <a-space>
              <a-button
                      v-permission="'${moduleName}:${businessName}:save'"
                      type="primary"
                      @click="${businessName}Store.handleAddOrEdit(null)"
              >
                <template #icon>
                  <icon-plus />
                </template>
                {{ $t('searchTable.operation.create') }}
              </a-button>
              <a-button
                      v-permission="'${moduleName}:${businessName}:batchDelete'"
                      type="primary"
                      status="danger"
                      :disabled="selectedKeys.length === 0"
                      @click="${businessName}Store.handleDeleteSelected()"
              >
                <template #icon>
                  <icon-delete />
                </template>
                <template #default>删除</template>
              </a-button>
              <a-button
                      v-permission="'${moduleName}:${businessName}:export'"
                      @click="${businessName}Store.handleExport()"
              >
                <template #icon>
                  <icon-download />
                </template>
                {{ $t('searchTable.operation.download') }}
              </a-button>
            </a-space>
          </a-col>
          <a-col
                  :span="12"
                  style="display: flex; align-items: center; justify-content: end"
          >
            <a-space>
              <a-tooltip :content="$t('searchTable.actions.refresh')">
                <div class="action-icon" @click="() => ${businessName}Store.reset()"
                ><icon-refresh size="18"
                  /></div>
              </a-tooltip>
              <DensityDropdown @size-change="handleSizeChange" />
            </a-space>
          </a-col>
        </a-row>
      </template>
      <template #main-body>
        <a-table
          v-model:selected-keys="selectedKeys"
          :row-selection="rowSelection"
          row-key="${className}Id"
          :loading="loading"
          :pagination="false"
          :columns="(columns as TableColumnData[])"
          :data="list"
          :bordered="false"
          :size="currentSize"
          :scroll="{ x: '100%', y: '100%' }"
        >
          <template #status="{ record }">
            <ZsStatus :value="record.status" />
          </template>
          <template #operations="{ record }">
            <a-space>
              <a-link
                v-permission="'${moduleName}:${businessName}:update'"
                @click="${businessName}Store.handleAddOrEdit(record)"
              >
                <template #icon>
                  <icon-edit />
                </template>
                <template #default>编辑</template>
              </a-link>
              <a-link
                v-permission="'${moduleName}:${businessName}:delete'"
                status="danger"
                @click="${businessName}Store.handleDelete(record)"
              >
                <template #icon>
                  <icon-delete />
                </template>
                <template #default>删除</template>
              </a-link>
            </a-space>
          </template>
        </a-table>
      </template>
      <template #footer>
        <a-pagination
          v-model:current="form.current"
          v-model:page-size="form.pageSize"
          :total="total"
          show-total
          show-jumper
          show-page-size
          @change="${businessName}Store.handleCurrentChange"
          @page-size-change="${businessName}Store.handleSizeChange"
        />
      </template>
    </zs-container>
    <${BusinessName}AddOrEdit ref="addEditRef" @refresh="${businessName}Store.fetchData" />
  </div>
</template>

<script lang="ts" setup>
  import {storeToRefs} from 'pinia';
  import {computed, onMounted, reactive, ref} from 'vue';
  import type { TableColumnData } from '@arco-design/web-vue/es/table/interface';
  <#assign dictTypes = []>
  <#list columnList as column>
  <#if column.dictType?? && column.dictType != "" && !dictTypes?seq_contains(column.dictType)>
    <#assign dictTypes = dictTypes + [column.dictType]>
  </#if>
  </#list>
  <#if dictTypes?has_content>
  import useDict from '@/hooks/dict';
  import { DictData } from '@/types/sys/dict/DictData';
  </#if>
  import { use${businessName}Store } from '@/store/modules/${moduleName}/${businessName}/${businessName}Store';
  import ${BusinessName}AddOrEdit from './${businessName}-add-or-edit.vue';

  const ${businessName}Store = use${BusinessName}Store();
  const { addEditRef, loading, list, total, form, selectedKeys } = 
  storeToRefs(${businessName}Store);

  const rowSelection = reactive({
    type: 'checkbox',
    showCheckedAll: true,
  });

  <#if dictTypes?has_content>
  <#list dictTypes as dictType>
  const ${dictType}Options = ref<DictData[]>([]);
  </#list>
  </#if>

  const columns = computed<TableColumnData[]>(() => [
    <#noparse>
    {
      title: '#',
      dataIndex: 'index',
      render: ({ rowIndex }) => `${rowIndex + 1 + (form.value.current - 1) * form.value.pageSize}`,
      width: 80,
      align: 'center',
    },
    </#noparse>
    <#list columnList as column>
    <#if column.isList == '1'>
    {
      title: '${column.columnComment!}',
      dataIndex: '${column.javaField!}',
      slotName: '${column.javaField!}',
      ellipsis: true,
      tooltip: true,
    },
    </#if>
    </#list>
    {
      title: '操作',
      dataIndex: 'operations',
      slotName: 'operations',
      width: 160,
      align: 'center',
      fixed: 'right',
    },
  ]);

  const currentSize = ref('medium');
  const handleSizeChange = (size: string) => {
    currentSize.value = size;
  };

  <#if dictTypes?has_content>
  async function loadDicts() {
    <#list dictTypes as dictType>
    ${dictType}Options.value = await useDict('${dictType}');
    </#list>
  }
  </#if>

  onMounted(() => {
    ${businessName}Store.fetchData();
    <#if dictTypes?has_content>
    loadDicts();
    </#if>
  });
</script>
