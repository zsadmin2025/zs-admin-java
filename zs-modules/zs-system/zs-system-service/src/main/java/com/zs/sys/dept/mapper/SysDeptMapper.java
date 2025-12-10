package com.zs.sys.dept.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.sys.dept.domain.entity.SysDeptEntity;
import com.zs.sys.dept.domain.params.SysDeptQueryParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zsadmin
 */
@Mapper
public interface SysDeptMapper extends DataPermissionMapper<SysDeptEntity> {

    IPage<SysDeptEntity> page(Page<SysDeptEntity> page, @Param("ew") Wrapper<SysDeptEntity> queryWrapper);
    /**
     * 根据部门ID查询子级部门ID集合
     **/
    List<Long> getSubDeptIdList(Long sysDeptId);

    List<SysDeptEntity> getList(SysDeptQueryParams sysDeptQueryParams);

    SysDeptEntity getBySysDeptId(Long sysDeptId);
}
