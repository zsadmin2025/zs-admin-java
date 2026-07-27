package com.zs.bpm.category.mapper;

import com.zs.bpm.category.domain.entity.BpmProcessCategoryEntity;
import com.zs.common.mp.base.DataPermissionMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程分类 Mapper
 *
 * @author zsadmin
 */
@Mapper
public interface BpmProcessCategoryMapper extends DataPermissionMapper<BpmProcessCategoryEntity> {
}
