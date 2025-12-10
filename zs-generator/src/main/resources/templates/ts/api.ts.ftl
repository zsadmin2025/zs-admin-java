import axios from 'axios';

export const ${className}Api = {
  // 获取分页数据
  page(params: any) {
    return axios.get('/${moduleName}/${businessName}/page', { params });
  },

  // 获取列表
  getList(params: any) {
    return axios.get('/${moduleName}/${businessName}/list', { params });
  },

  // 根据ID获取
  getById(id: string) {
    return axios.get(`/${moduleName}/${businessName}/<#noparse>${id}</#noparse>`);
  },

  // 新增
  save(data: any) {
    return axios.post('/${moduleName}/${businessName}/save', data);
  },

  // 编辑
  edit(data: any) {
    return axios.put('/${moduleName}/${businessName}/update', data);
  },

  // 删除单个
  delete(id: string) {
    return axios.delete(`/${moduleName}/${businessName}/<#noparse>${id}</#noparse>`);
  },

  // 批量删除
  batchDel(data: any) {
    return axios.delete('/${moduleName}/${businessName}', { data });
  },

  // 导出数据为Excel
  exportExcel(params: any) {
    return axios.get('/${moduleName}/${businessName}/export', {
      params,
      responseType: 'blob',
    });
  },
};
