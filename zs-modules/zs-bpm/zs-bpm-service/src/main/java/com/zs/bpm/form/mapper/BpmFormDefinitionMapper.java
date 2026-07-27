package com.zs.bpm.form.mapper;

import com.zs.bpm.form.domain.entity.BpmFormDefinitionEntity;
import com.zs.common.mp.base.DataPermissionMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 动态表单定义 Mapper
 *
 * @author zsadmin
 */
@Mapper
public interface BpmFormDefinitionMapper extends DataPermissionMapper<BpmFormDefinitionEntity> {
}
