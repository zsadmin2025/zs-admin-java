package com.zs.gen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.gen.domain.entity.GenTableColumn;
import com.zs.gen.mapper.GenTableColumnMapper;
import com.zs.gen.service.IGenTableColumnService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务字段 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class GenTableColumnServiceImpl extends ServiceImpl<GenTableColumnMapper, GenTableColumn> implements IGenTableColumnService {


    @Override
    public List<GenTableColumn> selectTableColumnsByName(String tableName) {
        return this.baseMapper.selectTableColumnsByName(tableName);
    }

    @Override
    public void updateGenTableColumn(List<GenTableColumn> columns) {
        for (GenTableColumn column : columns){
            this.baseMapper.updateById(column);
        }
    }

}
