package com.zs.common.core.model.user;

import com.zs.common.core.enums.UserTypeEnum;
import com.zs.common.core.model.BaseUserInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class MemberUser extends BaseUserInfo {

    private static final long serialVersionUID = 1L;

    private String nickname;

    private String openid;

    private Integer sex;

    private String ip;

    private String ipAddress;

    private Date loginTime;

    private Long tenantId;

    @Override
    public UserTypeEnum getUserType() {
        return UserTypeEnum.MEMBER;
    }
}
