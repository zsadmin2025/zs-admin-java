package com.zs.infra.quartz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.infra.quartz.domain.entity.SysJobEntity;
import com.zs.infra.quartz.domain.params.SysJobAddParams;
import com.zs.infra.quartz.domain.params.SysJobQueryParams;
import com.zs.infra.quartz.domain.params.SysJobUpdateParams;
import com.zs.infra.quartz.domain.vo.SysJobVO;
import com.zs.infra.quartz.mapper.SysJobMapper;
import com.zs.infra.quartz.service.ISysJobService;
import com.zs.infra.quartz.utils.QuartzUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 定时任务实现类
 */
@Service
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJobEntity> implements ISysJobService {

    @Resource
    private Scheduler scheduler;

    @PostConstruct
    private void init() throws SchedulerException {
        scheduler.clear();
        // 创建一个明确的查询条件对象
        QueryWrapper<SysJobEntity> queryWrapper = new QueryWrapper<>();
        List<SysJobEntity> sysJobEntityList = this.baseMapper.selectList(queryWrapper);
        for (SysJobEntity sysJobEntity : sysJobEntityList) {
          // 创建任务
          QuartzUtils.createScheduleJob(scheduler, sysJobEntity);

        }
    }

    public QueryWrapper<SysJobEntity> getWrapper(@NotNull SysJobQueryParams sysJobQueryParams) {
        QueryWrapper<SysJobEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(Strings.isNotEmpty(sysJobQueryParams.getJobName()), "job_name", sysJobQueryParams.getJobName());
        wrapper.eq(Strings.isNotEmpty(sysJobQueryParams.getJobGroup()), "job_group", sysJobQueryParams.getJobGroup());
        wrapper.eq(Objects.nonNull(sysJobQueryParams.getStatus()), "status", sysJobQueryParams.getStatus());
        wrapper.orderByDesc("create_time");

        return wrapper;
    }

    @Override
    public PageResult<SysJobVO> page(SysJobQueryParams sysJobQueryParams) {
        Page<SysJobEntity> page = new PageInfo<>(sysJobQueryParams);
        IPage<SysJobEntity> iPage = baseMapper.selectPage(page, getWrapper(sysJobQueryParams));

        return new PageResult<>(BeanUtil.copyToList(iPage.getRecords(), SysJobVO.class), page.getTotal(), SysJobVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysJobAddParams sysJobAddParams) {
        SysJobEntity sysJobEntity = BeanUtil.copyProperties(sysJobAddParams, SysJobEntity.class);
        this.baseMapper.insert(sysJobEntity);

        // 创建任务
        QuartzUtils.createScheduleJob(scheduler, sysJobEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysJobUpdateParams sysJobUpdateParams) {
        SysJobEntity sysJobEntity = BeanUtil.copyProperties(sysJobUpdateParams, SysJobEntity.class);

        // 更新任务
        QuartzUtils.updateScheduleJob(scheduler, sysJobEntity);

        this.baseMapper.updateById(sysJobEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(SysJobUpdateParams sysJobUpdateParams) {
        QuartzUtils.delScheduleJob(scheduler, sysJobUpdateParams.getSysJobId(), sysJobUpdateParams.getJobGroup());
        this.baseMapper.deleteById(sysJobUpdateParams.getSysJobId());
    }

    @Override
    public List<SysJobVO> list(SysJobEntity sysJobEntity) {
        return List.of();
    }

    @Override
    public SysJobVO get(Long sysJobId) {
        return BeanUtil.copyProperties(this.baseMapper.selectById(sysJobId), SysJobVO.class);
    }

    @Override
    public void pause(SysJobUpdateParams sysJobUpdateParams) {
        SysJobEntity sysJobEntity = BeanUtil.copyProperties(sysJobUpdateParams, SysJobEntity.class);
        QuartzUtils.pauseScheduleJob(scheduler, sysJobUpdateParams.getSysJobId(), sysJobUpdateParams.getJobGroup());

        this.baseMapper.updateById(sysJobEntity);
    }

    @Override
    public void resume(SysJobUpdateParams sysJobUpdateParams) {
        SysJobEntity sysJobEntity = BeanUtil.copyProperties(sysJobUpdateParams, SysJobEntity.class);
        QuartzUtils.resumeScheduleJob(scheduler, sysJobUpdateParams.getSysJobId(), sysJobUpdateParams.getJobGroup());

        this.baseMapper.updateById(sysJobEntity);
    }

    @Override
    public void run(SysJobUpdateParams sysJobUpdateParams) {
        QuartzUtils.runScheduleJob(scheduler, sysJobUpdateParams.getSysJobId(), sysJobUpdateParams.getJobGroup());
    }
}
