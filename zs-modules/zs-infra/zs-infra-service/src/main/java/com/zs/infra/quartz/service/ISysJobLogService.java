package com.zs.infra.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.infra.quartz.domain.entity.SysJobLogEntity;
import com.zs.infra.quartz.domain.params.SysJobLogQueryParams;
import com.zs.infra.quartz.domain.vo.SysJobLogVO;

import java.util.List;

/**
 * 定时任务日志service
 */
public interface ISysJobLogService extends IService<SysJobLogEntity> {

    void addJobLog(SysJobLogEntity sysJobLogEntity);

    PageResult<SysJobLogVO> page(SysJobLogQueryParams sysJobLogQueryParams);

    List<SysJobLogVO> list(SysJobLogQueryParams sysJobLogQueryParams);
}
