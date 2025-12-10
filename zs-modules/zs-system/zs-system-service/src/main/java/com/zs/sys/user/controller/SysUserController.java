package com.zs.sys.user.controller;


import cn.hutool.core.bean.BeanUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.page.PageResult;
import com.zs.common.core.utils.SecurityUtil;
import com.zs.sys.user.domain.excel.SysUserExcel;
import com.zs.sys.user.domain.params.SysUserAddParams;
import com.zs.sys.user.domain.params.SysUserPasswordParams;
import com.zs.sys.user.domain.params.SysUserQueryParams;
import com.zs.sys.user.domain.params.SysUserUpdateParams;
import com.zs.sys.user.domain.vo.SysUserInfoVO;
import com.zs.sys.user.domain.vo.SysUserVO;
import com.zs.sys.user.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


/**
 * @author zsadmin
 */
@RestController
@RequestMapping("system/sys/user")
@Tag(name = "用户管理")
public class SysUserController {


    @Resource
    private ISysUserService iSysUserService;


//    @Encryption
    @ApiOperationSupport(author = "zs")
    @Operation(summary = "分页获取用户信息")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:user:page')")
    public Result<PageResult<SysUserVO>> page(SysUserQueryParams sysUserQueryParams) {
        PageResult<SysUserVO> iPage = iSysUserService.page(sysUserQueryParams);
        return new Result<PageResult<SysUserVO>>().ok(iPage);
    }


    @ApiOperationSupport(author = "zs")
    @Operation(summary = "新增用户信息")
    @Log(module = "用户管理-新增", type = OperationTypeEnum.ADD, description = "新增用户信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('sys:user:save')")
    public Result<?> save(@Valid @RequestBody SysUserAddParams sysUserAddParams) {
        iSysUserService.save(sysUserAddParams);
        return new Result<>().ok();
    }



    @ApiOperationSupport(author = "zs")
    @Operation(summary = "修改用户信息")
    @Log(module = "用户管理-修改", type = OperationTypeEnum.UPDATE, description = "修改用户信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('sys:user:update')")
    public Result<?> update(@RequestBody SysUserUpdateParams sysUserUpdateParams) {
        iSysUserService.update(sysUserUpdateParams);
        return new Result<>().ok();
    }

    @Operation(summary = "密码修改")
    @Log(module = "用户管理-密码修改", type = OperationTypeEnum.UPDATE, description = "修改用户密码信息")
    @PutMapping("resetPassword")
    @PreAuthorize("hasAuthority('sys:user:resetpassword')")
    public Result<?> resetPassword(@RequestBody SysUserPasswordParams sysUserPasswordParams) {
        iSysUserService.resetPassword(sysUserPasswordParams);
        return new Result<>().ok();
    }

    @Operation(summary = "获取用户个人信息")
    @GetMapping("getUserInfo")
    public Result<LoginUserInfo> getUserInfo() {
        LoginUserInfo loginUserInfo = SecurityUtil.getUserInfo();
        return new Result<LoginUserInfo>().ok(loginUserInfo);
    }

    @Operation(summary = "获取用户信息详情")
//    @Encryption
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('sys:user:info')")
    public Result<SysUserInfoVO> get(@PathVariable("id") Long id) {
        SysUserInfoVO sysUserVO = iSysUserService.getById(id);
        return new Result<SysUserInfoVO>().ok(sysUserVO);
    }

    @Operation(summary = "删除用户信息")
    @Log(module = "用户管理-删除", type = OperationTypeEnum.DELETE, description = "删除用户信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        iSysUserService.delById(id);
        return new Result<>().ok();
    }

    @Operation(summary = "批量删除用户信息")
    @Log(module = "用户管理-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除用户信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('sys:user:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        iSysUserService.batchDelById(ids);
        return new Result<>().ok();
    }

    @Operation(summary = "用户信息列表")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public Result<List<SysUserVO>> list(SysUserQueryParams sysUserQueryParams) {
        List<SysUserVO> sysUserVOs = BeanUtil.copyToList(iSysUserService.list(sysUserQueryParams), SysUserVO.class);
        return new Result<List<SysUserVO>>().ok(sysUserVOs);
    }

    @Operation(summary = "根据用户ID集合获取用户信息列表")
    @PostMapping("getUserListByIds")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public Result<List<SysUserVO>> getUserList(@RequestBody Long[] sysUserIds) {
        List<SysUserVO> sysUserVOs = BeanUtil.copyToList(iSysUserService.getUserList(sysUserIds), SysUserVO.class);
        return new Result<List<SysUserVO>>().ok(sysUserVOs);
    }

    @Operation(summary = "根据用户ID集合获取用户信息列表")
//    @Log(module = "用户管理-导出", type = OperationTypeEnum.EXPORT, description = "导出用户信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('sys:user:export')")
    public void export(HttpServletResponse response, SysUserQueryParams sysUserQueryParams) throws IOException {
        List<SysUserVO> list = iSysUserService.list(sysUserQueryParams);
        List<SysUserExcel> excelList = BeanUtil.copyToList(list, SysUserExcel.class);
        ExcelUtils.exportExcel(response, "用户信息.xlsx", SysUserExcel.class, excelList);

    }
}
