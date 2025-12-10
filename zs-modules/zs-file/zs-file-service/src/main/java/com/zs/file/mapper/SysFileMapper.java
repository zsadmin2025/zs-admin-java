package com.zs.file.mapper;

import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.file.domain.entity.SysFileEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysFileMapper extends DataPermissionMapper<SysFileEntity> {
}
