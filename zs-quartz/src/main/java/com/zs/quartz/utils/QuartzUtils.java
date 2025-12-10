package com.zs.quartz.utils;

import com.zs.common.core.constant.ScheduleConstants;
import com.zs.quartz.domain.entity.SysJobEntity;
import org.quartz.*;

import java.util.Objects;

/**
 * 定时任务工具类
 */
public class QuartzUtils {






    private static JobKey getJobKey(Long jobId,String jobGroup) {
        return JobKey.jobKey(ScheduleConstants.JOB_PARAM_KEY + jobId, jobGroup);
    }

    private static TriggerKey getTriggerKey(Long jobId,String jobGroup) {
        return TriggerKey.triggerKey(ScheduleConstants.JOB_PARAM_KEY + jobId, jobGroup);
    }

    /**
     * 创建定时任务
     * @param scheduler 任务调度器
     * @param sysJobEntity 定时任务信息
     */
    public static void createScheduleJob(Scheduler scheduler, SysJobEntity sysJobEntity) {

        Class<? extends Job> jobClass;
        try {
            jobClass = Class.forName(sysJobEntity.getJobClass()).asSubclass(Job.class);
            // 构建定时任务 withIdentity-唯一标识
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(getJobKey(sysJobEntity.getSysJobId(), sysJobEntity.getJobGroup())).build();
            // 设置定时任务的执行方式
            CronScheduleBuilder cronTriggerBuilder = CronScheduleBuilder.cronSchedule(sysJobEntity.getCronExpression());
            // 构建触发器 withIdentity-唯一标识
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(sysJobEntity.getSysJobId(), sysJobEntity.getJobGroup())).withSchedule(cronTriggerBuilder).build();


            jobDetail.getJobDataMap().put(ScheduleConstants.JOB_PROPERTIES_KEY, sysJobEntity);

            // 生成一个新的定时任务
            scheduler.scheduleJob(jobDetail, cronTrigger);



            // 暂停任务
            if (Objects.equals(sysJobEntity.getStatus(), ScheduleConstants.Status.PAUSE.getValue())) {
                scheduler.pauseJob(getJobKey(sysJobEntity.getSysJobId(), sysJobEntity.getJobGroup()));
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("无法加载指定的 Job 类: " + sysJobEntity.getJobClass(), e);
        } catch (Exception e) {
            throw new RuntimeException("定时任务添加失败", e);
        }
    }


    /**
     * 更新定时任务
     * @param scheduler 任务调度器
     * @param sysJobEntity 定时任务信息
     */
    public static void updateScheduleJob(Scheduler scheduler,SysJobEntity sysJobEntity) {
        try {
            //获取到对应任务的触发器
            TriggerKey triggerKey = getTriggerKey(sysJobEntity.getSysJobId(), sysJobEntity.getJobGroup() );
            //设置定时任务执行方式
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(sysJobEntity.getCronExpression());
            //重新构建任务的触发器trigger
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            // 修改对应的任务
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException("更新定时任务失败", e);
        }

    }


    /**
     * 删除定时任务
     * @param scheduler 任务调度器
     * @param jobId 唯一标识
     *
     */
    public static void delScheduleJob(Scheduler scheduler,Long jobId, String jobGroup) {
        try {
            // 先暂停在删除
            scheduler.pauseJob(getJobKey(jobId, jobGroup));
            scheduler.deleteJob(getJobKey(jobId, jobGroup));
        } catch (SchedulerException e) {
            throw new RuntimeException("删除定时任务失败", e);
        }
    }

    /**
     * 暂停定时任务
     * @param scheduler 任务调度器
     * @param jobId 唯一标识
     */
    public static void pauseScheduleJob(Scheduler scheduler, Long jobId, String jobGroup) {
        try {
            scheduler.pauseJob(getJobKey(jobId, jobGroup));
        } catch (SchedulerException e) {
            throw new RuntimeException("暂停定时任务失败", e);
        }

    }

    /**
     * 恢复定时任务
     * @param scheduler 任务调度器
     * @param jobId 唯一标识
     */
    public static void resumeScheduleJob(Scheduler scheduler,Long jobId, String jobGroup) {
        try {
            scheduler.resumeJob(getJobKey(jobId, jobGroup));
        } catch (SchedulerException e) {
            throw new RuntimeException("恢复定时任务失败", e);
        }
    }

    /**
     * 立即执行一次定时任务
     * @param scheduler 任务调度器
     * @param jobId 唯一标识
     */
    public static void runScheduleJob(Scheduler scheduler, Long jobId, String jobGroup) {
        try {
            scheduler.triggerJob(getJobKey(jobId, jobGroup));
        } catch (SchedulerException e) {
            throw new RuntimeException("立即执行定时任务失败", e);
        }
    }

}
