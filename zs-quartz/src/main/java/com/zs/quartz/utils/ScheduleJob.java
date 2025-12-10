package com.zs.quartz.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.zs.common.core.constant.ScheduleConstants;
import com.zs.quartz.domain.entity.SysJobEntity;
import com.zs.quartz.domain.entity.SysJobLogEntity;
import com.zs.quartz.service.ISysJobLogService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 */

public abstract class ScheduleJob implements Job {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap  jobDataMap = context.getMergedJobDataMap();
        SysJobEntity sysJobEntity = BeanUtil.toBean(jobDataMap.get(ScheduleConstants.JOB_PROPERTIES_KEY), SysJobEntity.class);
        // 开始时间毫秒
        long startTimeMillis = System.currentTimeMillis();
        // 开始时间
        Date startTime = new Date();

        SysJobLogEntity sysJobLogEntity = new SysJobLogEntity();
        try {
            executeInternal(context);
            sysJobLogEntity.setStatus(1);
        }catch (Exception e) {
            sysJobLogEntity.setExceptionInfo(e.getMessage());
            sysJobLogEntity.setStatus(0);
        }


        // 结束时间毫秒
        long endTimeMillis = System.currentTimeMillis();
        // 获取执行日志
        String runMsg = sysJobEntity.getJobName() + "共耗时：" + (endTimeMillis - startTimeMillis) + "ms";

        // 结束时间
        Date endTime = new Date();
        // 计算执行时长
        long duration = (endTimeMillis - startTimeMillis);


        sysJobLogEntity.setSysJobId(sysJobEntity.getSysJobId());
        sysJobLogEntity.setJobClass(context.getJobDetail().getJobClass().getName());
        sysJobLogEntity.setJobName(sysJobEntity.getJobName());
        sysJobLogEntity.setJobGroup(sysJobEntity.getJobGroup());
        sysJobLogEntity.setJobMessage(runMsg);
        sysJobLogEntity.setStartTime(startTime);
        sysJobLogEntity.setEndTime(endTime);
        sysJobLogEntity.setDuration(duration);


        SpringUtil.getBean(ISysJobLogService.class).addJobLog(sysJobLogEntity);
    }



    protected abstract void executeInternal(JobExecutionContext context) throws JobExecutionException;
}
