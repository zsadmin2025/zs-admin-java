package com.zs.sys.log.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author zsadmin
 */
@Data
@TableName("sys_log_operation")
public class SysLogOperationEntity {

    @TableId
    private Long sysLogOperationId;
    private String username;
    private String module;
    private String ipAddress;
    private String operationType;
    private String operationDescription;
    private String requestMethod;
    private String requestPath;
    private String requestParams;
    private Integer responseStatusCode;
    private String responseMessage;
    private Integer operationDuration;
    private String createTime;

}
