package com.zs.common.core.utils;

import com.zs.common.core.enums.UserTypeEnum;
import com.zs.common.core.model.BaseUserInfo;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.model.user.MemberUser;

public class LoginVOConverter {

    public static BaseUserInfo convert(LoginUserInfo userInfo) {
        if (userInfo == null) {
            return null;
        }
        UserTypeEnum userType = userInfo.getUserType();
        if (UserTypeEnum.PLATFORM.getCode().equals(userType)) {
//            return toPlatformVO(userInfo);
            return null;
        } else if (UserTypeEnum.MEMBER.getCode().equals(userType)) {
            return toMemberVO(userInfo);
        }
        throw new IllegalArgumentException("Unknown user type: " + userType);
    }

//    public static PlatformUserInfo toPlatformVO(LoginUserInfo userInfo) {
//        if (userInfo == null || userInfo.getUserInfo() == null) {
//            return null;
//        }
//        PlatformUserInfo platformUserInfo = (PlatformUserInfo) userInfo.getUserInfo();
//        return  platformUserInfo;
//    }

    public static MemberUser toMemberVO(LoginUserInfo userInfo) {
        if (userInfo == null || !(userInfo.getUserInfo() instanceof MemberUser)) {
            return null;
        }

        MemberUser member = (MemberUser) userInfo.getUserInfo();
        return member;
    }


}
