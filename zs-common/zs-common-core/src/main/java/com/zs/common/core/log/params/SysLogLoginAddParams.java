package com.zs.common.core.log.params;

import lombok.Data;

/**
 * @author zsadmin
 */
@Data
public class SysLogLoginAddParams {


    private Long sysLogLoginId;
    private String loginTime;
    private Long sysUserId;
    private String username;
    private String ipAddress;
    private String city;
    private String userAgent;
    private Integer loginStatus;
    private String failureReason;
    private Integer loginMethod;
    private String loginSource;
    private String browser;
    private String os;
}
