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

    List<GenTableColumn> selectTableColumnsByName(String tableName);

    void updateGenTableColumn(List<GenTableColumn> columns);
}
