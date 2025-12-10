package com.zs.sys.config.controller;

import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.model.file.SysConfigFileVO;
import com.zs.sys.config.domain.params.SysConfigParams;
import com.zs.sys.config.domain.vo.*;
import com.zs.sys.config.service.ISysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 系统配置
 */
@RestController
@RequestMapping("system/sys/config")
@Tag(name = "系统配置")
public class SysConfigController {

    @Resource
    private ISysConfigService iSysConfigService;

    @Operation(summary = "修改系统配置")
    @Log(module = "系统配置-修改", type = OperationTypeEnum.UPDATE, description = "修改系统配置")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:config:update')")
    public Result<?> update(@RequestBody SysConfigParams sysConfigParams){
        iSysConfigService.update(sysConfigParams);
        return new Result<>().ok();
    }

    @Operation(summary = "获取网站配置")
    @GetMapping("website")
    public Result<SysConfigWebsiteVO> websiteInfo(){
        SysConfigWebsiteVO sysConfigWebsiteVO = iSysConfigService.websiteInfo();
        return new Result<SysConfigWebsiteVO>().ok(sysConfigWebsiteVO);
    }

    @Operation(summary = "获取文件上传配置")
    @GetMapping("file")
    @PreAuthorize("hasAuthority('sys:config:info')")
    public Result<SysConfigFileVO> fileUploadInfo(){
        SysConfigFileVO sysConfigFileVO = iSysConfigService.fileUploadInfo();
        return new Result<SysConfigFileVO>().ok(sysConfigFileVO);
    }

    @Operation(summary = "获取短信配置")
    @GetMapping("sms")
    @PreAuthorize("hasAuthority('sys:config:info')")
    public Result<SysConfigSmsVO> smsInfo(){
        SysConfigSmsVO sysConfigSmsVO = iSysConfigService.smsInfo();
        return new Result<SysConfigSmsVO>().ok(sysConfigSmsVO);
    }

    @Operation(summary = "获取邮件配置")
    @GetMapping("email")
    @PreAuthorize("hasAuthority('sys:config:info')")
    public Result<SysConfigEmailVO> emailInfo(){
        SysConfigEmailVO sysConfigEmailVO = iSysConfigService.emailInfo();
        return new Result<SysConfigEmailVO>().ok(sysConfigEmailVO);
    }

    @GetMapping("pay")
    @PreAuthorize("hasAuthority('sys:config:info')")
    public Result<SysConfigPayVO> payInfo(){
        SysConfigPayVO sysConfigPayVO = iSysConfigService.payInfo();
        return new Result<SysConfigPayVO>().ok(sysConfigPayVO);
    }


    @GetMapping("other")
    @PreAuthorize("hasAuthority('sys:config:info')")
    public Result<SysConfigOtherVO> otherInfo(){
        SysConfigOtherVO sysConfigOtherVO = iSysConfigService.otherInfo();
        return new Result<SysConfigOtherVO>().ok(sysConfigOtherVO);
    }
}
