package com.zs.gen.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.gen.domain.entity.GenTableColumn;

import java.util.List;

/**
 * 业务字段 服务层
 * 
 * @author ruoyi
 */
public interface IGenTableColumnService extends IService<GenTableColumn>{

    /**
     * 查询业务字段列表
     * 
     * @param tableId 业务字段编号
     * @return 业务字段集合
     */
    List<GenTableColumn> selectTableColumnsByName(String tableName);

    /**
     * 更新业务字段
     * 
     * @param columns 业务字段列表
     */
    void updateGenTableColumn(List<GenTableColumn> columns);
}
