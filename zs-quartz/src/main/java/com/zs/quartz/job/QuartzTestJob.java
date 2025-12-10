package com.zs.quartz.job;

import com.zs.quartz.utils.ScheduleJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * 测试任务
 */
@Component
public class QuartzTestJob extends ScheduleJob {



    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("doExecute执行:  " + System.currentTimeMillis());
    }
}
