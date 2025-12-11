package com.zs.sys.log.domain.vo;

import lombok.Data;

/**
 * @author zsadmin
 */
@Data
public class SysLogErrorVO {

    private Long sysLogErrorId;
    private String username;
    private String module;
    private String ipAddress;
    private String exceptionType;
    private String exceptionMessage;
    private String requestMethod;
    private String requestPath;
    private String requestParams;
    private String createTime;
}
