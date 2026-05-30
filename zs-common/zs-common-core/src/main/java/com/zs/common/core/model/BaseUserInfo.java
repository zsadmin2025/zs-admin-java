package com.zs.common.core.model;

import com.zs.common.core.enums.UserTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public abstract class BaseUserInfo implements Serializable {

    private Long userId;

    private String username;

    private String ip;

    private String ipAddress;

    private String browser;

    private String os;

    private Date loginTime;

    protected Integer status;

    private Long tenantId;

    public abstract UserTypeEnum getUserType();

    public boolean isEnabled() {
        return status != null && status == 1;
    }
}
