package com.zs.bpm.definition.mapper;

import com.zs.bpm.definition.domain.entity.BpmProcessDefinitionInfoEntity;
import com.zs.common.mp.base.DataPermissionMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程定义信息 Mapper
 *
 * @author zsadmin
 */
@Mapper
public interface BpmProcessDefinitionInfoMapper extends DataPermissionMapper<BpmProcessDefinitionInfoEntity> {
}
