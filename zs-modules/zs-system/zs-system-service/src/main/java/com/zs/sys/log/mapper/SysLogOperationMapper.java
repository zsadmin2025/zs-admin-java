package com.zs.sys.log.mapper;

import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.sys.log.domain.entity.SysLogOperationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zsadmin
 */
@Mapper
public interface SysLogOperationMapper extends DataPermissionMapper<SysLogOperationEntity> {
}
