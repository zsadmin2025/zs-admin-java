package com.zs.gen.mapper;


import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.gen.domain.entity.GenTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务 数据层
 * 
 * @author ruoyi
 */
@Mapper
public interface GenTableMapper extends DataPermissionMapper<GenTable> {

    @InterceptorIgnore(tenantLine = "true")
    IPage<GenTable> dbPage(Page<GenTable> page, @Param("ew") Wrapper<GenTable> queryWrapper);

    @InterceptorIgnore(tenantLine = "true")
    List<GenTable> selectTableListByNames(@Param("tables") List<String> tables);

}
