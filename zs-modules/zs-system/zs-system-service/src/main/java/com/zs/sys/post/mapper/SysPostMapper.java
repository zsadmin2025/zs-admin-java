package com.zs.sys.post.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.sys.post.domain.entity.SysPostEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zsadmin
 */
@Mapper
public interface SysPostMapper extends DataPermissionMapper<SysPostEntity> {

    IPage<SysPostEntity> page(Page<SysPostEntity> page, @Param("params") Map<String, Object> params);

    List<SysPostEntity> getList(@Param("params") Map<String, Object> params);
}
