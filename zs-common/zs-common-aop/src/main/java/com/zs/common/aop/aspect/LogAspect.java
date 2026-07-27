package com.zs.common.aop.aspect;


import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.log.params.SysLogErrorAddParams;
import com.zs.common.core.log.params.SysLogOperationAddParams;
import com.zs.common.core.log.service.ILogErrorAspectService;
import com.zs.common.core.log.service.ILogOperationAspectService;
import com.zs.common.core.utils.IpUtils;
import com.zs.common.core.utils.SecurityUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;


/**
 * @author zsadmin
 */

@Aspect
@Component
public class LogAspect {

    long beginTime;

    @Resource
    private ILogOperationAspectService iLogOperationAspectService;
    @Resource
    private ILogErrorAspectService iLogErrorAspectService;

    /**
     * 操作日志切入点
     */
    @Pointcut("@annotation(com.zs.common.aop.annotation.Log)")
    public void logOperationPointCut() {
    }

    @Before("logOperationPointCut()")
    public void before() {

        beginTime = System.currentTimeMillis();
    }

    /**
     * 记录操作日志
     */
    @AfterReturning(value = "logOperationPointCut()", returning = "obj")
    public void afterReturning(@NotNull JoinPoint point, Object obj) {
        saveLogOperation(point, obj);
    }


    /**
     * 记录异常日志
     * 异常通知，可传递异常对象
     * 目标方法抛出异常后
     * 常用场景：处理异常、回滚事务
     */
    @AfterThrowing(value = "logOperationPointCut()", throwing = "e")
    public void afterThrowing(@NotNull JoinPoint point, @NotNull Exception e) {
        saveLogError(point, e);
    }


    /**
     * 获取HttpServletRequest
     */
    @NotNull
    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    /**
     * 获取请求体参数
     */
    private String getRequestBody(@NotNull JoinPoint point) {
        Object[] args = point.getArgs();
        return JSONUtil.toJsonStr(args[0]);
    }


    /**
     * 操作日志
     */
    @Async
    public void saveLogOperation(@NotNull JoinPoint point, Object obj) {
        HttpServletRequest request = getRequest();
        String params = getRequestBody(point);

        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        Log annotation = method.getAnnotation(Log.class);
        Result<?> result = (Result<?>) obj;

        SysLogOperationAddParams addParams = createLogOperationParams(annotation, request, params, result);

        iLogOperationAspectService.save(addParams);
    }

    /**
     * 错误日志
     */
    @Async
    public void saveLogError(@NotNull JoinPoint point, @NotNull Exception e) {
        HttpServletRequest request = getRequest();
        String params = getRequestBody(point);

        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        Log annotation = method.getAnnotation(Log.class);

        SysLogErrorAddParams sysLogErrorAddParams = createLogErrorParams(annotation, request, params, e);

        iLogErrorAspectService.save(sysLogErrorAddParams);
    }

    /**
     * 创建操作日志参数
     */
    @NotNull
    private SysLogOperationAddParams createLogOperationParams(@NotNull Log annotation, @NotNull HttpServletRequest request, String params, @NotNull Result<?> result) {
        SysLogOperationAddParams addParams = new SysLogOperationAddParams();
        addParams.setUsername(SecurityUtil.getUserInfo().getUsername());
        addParams.setModule(annotation.module());
        addParams.setIpAddress(IpUtils.getIpAddr(request));
        addParams.setOperationType(annotation.type().toString());
        addParams.setOperationDescription(annotation.description());
        addParams.setRequestMethod(request.getMethod());
        addParams.setRequestPath(request.getRequestURI());
        addParams.setRequestParams(params);
        addParams.setResponseStatusCode(result.getCode());
        addParams.setResponseMessage(result.getMsg());
        addParams.setOperationDuration((int) (System.currentTimeMillis() - beginTime));
        addParams.setCreateTime(DateUtil.now());
        return addParams;
    }

    /**
     * 创建异常日志参数
     */
    @NotNull
    private SysLogErrorAddParams createLogErrorParams(@NotNull Log annotation, @NotNull HttpServletRequest request, String params, @NotNull Exception e) {
        SysLogErrorAddParams addParams = new SysLogErrorAddParams();
        addParams.setUsername(SecurityUtil.getUserInfo().getUserInfo().getUsername());
        addParams.setModule(annotation.module());
        addParams.setIpAddress(IpUtils.getIpAddr(request));
        addParams.setExceptionType(e.getClass().getName());
        addParams.setExceptionMessage(e.getMessage());
        addParams.setRequestMethod(request.getMethod());
        addParams.setRequestPath(request.getRequestURI());
        addParams.setRequestParams(params);
        addParams.setCreateTime(DateUtil.now());
        return addParams;
    }


}
