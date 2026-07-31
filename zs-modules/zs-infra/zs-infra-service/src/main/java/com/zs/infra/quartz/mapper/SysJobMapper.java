package com.zs.infra.quartz.mapper;

import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.infra.quartz.domain.entity.SysJobEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务mapper
 */
@Mapper
public interface SysJobMapper extends DataPermissionMapper<SysJobEntity> {
}
