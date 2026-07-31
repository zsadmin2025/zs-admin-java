package com.zs.infra.quartz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.infra.quartz.domain.entity.SysJobLogEntity;
import com.zs.infra.quartz.domain.params.SysJobLogQueryParams;
import com.zs.infra.quartz.domain.vo.SysJobLogVO;
import com.zs.infra.quartz.mapper.SysJobLogMapper;
import com.zs.infra.quartz.service.ISysJobLogService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 定时任务日志service实现
 */
@Service
public class SysJobLogServiceImpl extends ServiceImpl<SysJobLogMapper, SysJobLogEntity> implements ISysJobLogService {

    @Override
    public void addJobLog(SysJobLogEntity sysJobLogEntity) {
        this.baseMapper.insert(sysJobLogEntity);
    }

    @Override
    public PageResult<SysJobLogVO> page(SysJobLogQueryParams sysJobLogQueryParams) {
        Page<SysJobLogEntity> page = new PageInfo<>(sysJobLogQueryParams);
        IPage<SysJobLogEntity> iPage = this.baseMapper.selectPage(page, getWrapper(sysJobLogQueryParams));
        return new PageResult<>(BeanUtil.copyToList(iPage.getRecords(), SysJobLogVO.class), page.getTotal(), SysJobLogVO.class);
    }

    @Override
    public List<SysJobLogVO> list(SysJobLogQueryParams sysJobLogQueryParams) {
        return BeanUtil.copyToList(this.baseMapper.selectList(getWrapper(sysJobLogQueryParams)), SysJobLogVO.class);
    }

    public LambdaQueryWrapper<SysJobLogEntity> getWrapper(SysJobLogQueryParams sysJobLogQueryParams) {
        LambdaQueryWrapper<SysJobLogEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysJobLogEntity::getSysJobId, sysJobLogQueryParams.getSysJobId())
                        .eq(Strings.isNotEmpty(sysJobLogQueryParams.getJobName()), SysJobLogEntity::getJobName, sysJobLogQueryParams.getJobName())
                        .eq(Strings.isNotEmpty(sysJobLogQueryParams.getJobGroup()), SysJobLogEntity::getJobGroup, sysJobLogQueryParams.getJobGroup())
                        .eq(Objects.nonNull(sysJobLogQueryParams.getStatus()), SysJobLogEntity::getStatus, sysJobLogQueryParams.getStatus());

        return queryWrapper;
    }
}
