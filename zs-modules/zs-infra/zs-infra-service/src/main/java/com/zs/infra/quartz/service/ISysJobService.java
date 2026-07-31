package com.zs.infra.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.page.PageResult;
import com.zs.infra.quartz.domain.entity.SysJobEntity;
import com.zs.infra.quartz.domain.params.SysJobAddParams;
import com.zs.infra.quartz.domain.params.SysJobQueryParams;
import com.zs.infra.quartz.domain.params.SysJobUpdateParams;
import com.zs.infra.quartz.domain.vo.SysJobVO;

import java.util.List;

/**
 * 定时任务接口
 */
public interface ISysJobService extends IService<SysJobEntity> {

    PageResult<SysJobVO> page(SysJobQueryParams sysJobQueryParams);

    /**
     * 新增定时任务
     */
    void save(SysJobAddParams sysJobAddParams);

    /**
     * 更新定时任务
     */
    void update(SysJobUpdateParams sysJobUpdateParams);

    /**
     * 定时任务列表
     */
    List<SysJobVO> list(SysJobEntity sysJobEntity);

    /**
     * 获取定时任务详情
     */
    SysJobVO get(Long sysJobId);

    /**
     * 删除定时任务
     */
    void del(SysJobUpdateParams sysJobUpdateParams);

    /**
     * 暂停定时任务
     */
    void pause(SysJobUpdateParams sysJobUpdateParams);

    /**
     * 恢复定时任务
     */
    void resume(SysJobUpdateParams sysJobUpdateParams);

    /**
     * 立即执行一次
     */
    void run(SysJobUpdateParams sysJobUpdateParams);


}
