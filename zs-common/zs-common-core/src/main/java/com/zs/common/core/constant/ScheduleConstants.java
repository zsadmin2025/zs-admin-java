package com.zs.common.core.constant;

import lombok.Getter;

/**
 * 任务调度通用常量
 */
public class ScheduleConstants {


    public static final String JOB_PARAM_KEY = "ZS_JOB_KEY:";

    public static final String JOB_PROPERTIES_KEY = "JOB";


    @Getter
    public enum Status {
        /**
         * 正常
         */
        NORMAL(1),
        /**
         * 暂停
         */
        PAUSE(0);

        private final Integer value;

        Status(Integer value) {
            this.value = value;
        }

    }


}
