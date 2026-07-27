package com.zs.service;

import com.zs.common.core.model.user.SysUser;
import com.zs.common.core.page.PageResult;
import com.zs.model.params.OnlineUserQueryParams;


public interface OnlineUserService {


    PageResult<SysUser> getOnlineUsersPage(OnlineUserQueryParams onlineUserQueryParams);

}
