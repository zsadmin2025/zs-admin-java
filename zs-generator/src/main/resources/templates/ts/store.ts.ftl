import { ${className}Api } from '@/api/${moduleName}/${businessName}';
import { ref } from 'vue';
import { defineStore } from 'pinia';
import { ${BusinessName}State, ${BusinessName} } from '@/types/${moduleName}/${businessName}/${businessName}Types';
import { Modal } from '@arco-design/web-vue';
import download from '@/utils/fileDownload';

export const use${BusinessName}Store = defineStore('${businessName}', {
  state: (): ${BusinessName}State => ({
    addEditRef: ref(null),
    ruleFormRef: ref(null),
    list: [],
    loading: false,
    total: 0,
    form: {
    <#list columnList as column>
      <#if column.isQuery == '1'>
      <#if column.javaType == 'String'>
      ${column.javaField!}: '',
      </#if>
      <#if column.javaType == 'Integer'>
      ${column.javaField!}: 1,
      </#if>
      <#if column.javaType == 'Long'>
      ${column.javaField!}: '',
      </#if>
      <#if column.javaType == 'Date'>
      ${column.javaField!}: new Date(),
      </#if>
      <#if column.javaType == 'Double'>
      ${column.javaField!}: 0.0,
      </#if>
      <#if column.javaType == 'Boolean'>
      ${column.javaField!}: false,
      </#if>
      </#if>
    </#list>
      current: 1,
      pageSize: 30,
      order: 'asc',
      orderField: 'createTime',
    },
    selectedKeys: [],
  }),
  actions: {
    async fetchData() {
      this.loading = true;
      const { data } = await ${className}Api.page(this.form);
      this.list = data?.list ?? [];
      this.total = data.total ?? 0;
      this.loading = false;
    },
    handleNodeClick(data: any) {
      this.form.${className}Id = data.${className}Id;
      this.fetchData();
    },
    handleSizeChange(val: number) {
      this.form.pageSize = val;
      this.fetchData();
    },
    handleCurrentChange(val: number) {
      this.form.current = val;
      this.fetchData();
    },
    reset() {
      this.form = {
        current: 1,
        pageSize: 30,
      };
      this.fetchData();
    },
    setSelectedKeys(keys: number[]) {
      this.selectedKeys = keys;
    },
    handleAddOrEdit(row: any) {
      if (this.addEditRef) {
        this.addEditRef.form.${className}Id = row?.${className}Id;
        this.addEditRef.init();
      }
    },
    async handleDeleteSelected() {
      Modal.confirm({
        title: '温馨提示',
        titleAlign: 'start',
        content: '您将进行批量删除操作,是否继续?',
        onOk: async () => {
          const ids = this.selectedKeys.map((item: any) => item);
          await ${className}Api.batchDel(ids);
          await this.fetchData();
        },
      });
    },
    async handleDelete(record: ${BusinessName}) {
      Modal.confirm({
        title: '确认删除',
        titleAlign: 'start',
        <#noparse>
        content: `确定要删除吗？`,
        </#noparse>
        onOk: async () => {
          await ${className}Api.delete(record.${className}Id);
          await this.fetchData();
        },
      });
    },

    // 导出
    async handleExport() {
      const excelName = '${functionName!}';
      const response = await ${className}Api.exportExcel({ excelName });
      const data = response.data as Blob;
      <#noparse>
      download.excel(data, `${excelName}.xlsx`);
      </#noparse>
    },
  },
});
