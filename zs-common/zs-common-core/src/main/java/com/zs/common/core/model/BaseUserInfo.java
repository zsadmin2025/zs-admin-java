package com.zs.common.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zs.common.core.enums.UserTypeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public abstract class BaseUserInfo implements Serializable {

    protected Long userId;

    protected String username;

    @JsonIgnore
    protected String password;

    protected String realName;

    protected String avatar;

    protected String phone;

    protected String email;

    protected Integer status;

    public abstract UserTypeEnum getUserType();

    public boolean isEnabled() {
        return status != null && status == 1;
    }
}
