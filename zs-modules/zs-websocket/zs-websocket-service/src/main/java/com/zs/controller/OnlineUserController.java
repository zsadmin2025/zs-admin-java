package com.zs.controller;

import com.zs.common.core.core.Result;
import com.zs.common.core.model.user.SysUser;
import com.zs.common.core.page.PageResult;
import com.zs.model.params.OnlineUserQueryParams;
import com.zs.service.OnlineUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/sys/onlineUser")
@Tag(name = "在线用户")
public class OnlineUserController {

    @Resource
    private OnlineUserService onlineUserService;


    @Operation(summary = "获取在线用户列表")
    @RequestMapping("/getOnlineUsersPage")
    public Result<PageResult<SysUser>> getOnlineUsersPage(OnlineUserQueryParams onlineUserQueryParams) {
        PageResult<SysUser> iPage = onlineUserService.getOnlineUsersPage(onlineUserQueryParams);
        return new Result<PageResult<SysUser>>().ok(iPage);
    }
}
