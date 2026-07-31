package com.zs.infra.quartz.mapper;

import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.infra.quartz.domain.entity.SysJobLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志mapper
 */
@Mapper
public interface SysJobLogMapper extends DataPermissionMapper<SysJobLogEntity> {
}
