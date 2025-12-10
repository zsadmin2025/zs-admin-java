package com.zs.gen.mapper;


import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.gen.domain.entity.GenTableColumn;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 业务字段 数据层
 * 
 * @author ruoyi
 */

@Mapper
public interface GenTableColumnMapper extends DataPermissionMapper<GenTableColumn>
{
    @InterceptorIgnore(tenantLine = "true")
    List<GenTableColumn> selectTableColumnsByName(String tableName);
}
