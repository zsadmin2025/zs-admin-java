package com.zs.sys.log.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * @author zsadmin
 */
@Data
@TableName("sys_log_login")
public class SysLogLoginEntity {

    @TableId
    private Long sysLogLoginId;
    private String username;
    private String ipAddress;
    private String loginTime;
    private String city;
    private String userAgent;
    private Integer loginStatus;
    private String failureReason;
    private Integer loginMethod;
    private String loginSource;
    private String browser;
    private String os;


}
