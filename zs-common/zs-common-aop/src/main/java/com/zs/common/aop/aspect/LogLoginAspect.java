package com.zs.common.aop.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zs.common.aop.annotation.LoginLog;
import com.zs.common.core.log.params.SysLogLoginAddParams;
import com.zs.common.core.log.service.ILogLoginAspectService;
import com.zs.common.core.utils.IpUtils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;

/**
 * @author zsadmin
 */
@Aspect
@Component
public class LogLoginAspect {


    @Resource
    private ILogLoginAspectService iLogLoginAspectService;

    @Pointcut("@annotation(com.zs.common.aop.annotation.LoginLog)")
    public void loginPointCut() {
    }

    /**
     * 记录登录日志
     * 环绕通知
     * 用于目标方法的整个调用流程
     */
    @Around("loginPointCut()")
    public Object around(@NotNull ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object object = proceedingJoinPoint.proceed();
        saveLoginLog(proceedingJoinPoint);
        return object;
    }

    @Async("threadPoolExecutor")
    void saveLoginLog(@NotNull ProceedingJoinPoint proceedingJoinPoint) {
        Object[] objects = proceedingJoinPoint.getArgs();
        Throwable failureReason = null;
        if (objects[2] instanceof BadCredentialsException) {
            failureReason = (BadCredentialsException) objects[2];
        }

        HttpServletRequest request = getRequest();
        String username = (String) request.getAttribute("username");
        String ipAddr = IpUtils.getIpAddr(request);
        String userAgentString = request.getHeader(HttpHeaders.USER_AGENT);

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        LoginLog annotation = method.getAnnotation(LoginLog.class);
        String city = IpUtils.getCityInfo(ipAddr);
        UserAgent userAgent = UserAgentUtil.parse(userAgentString);
        String browser = userAgent.getBrowser().toString();
        String os = userAgent.getOs().toString();

        SysLogLoginAddParams sysLogLoginAddParams = createLogLoginParams(username, ipAddr, city, userAgentString, annotation, failureReason, browser, os);

        iLogLoginAspectService.save(sysLogLoginAddParams);
    }


    @NotNull
    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    /**
     * 创建登录日志参数
     */
    @NotNull
    private SysLogLoginAddParams createLogLoginParams(String username, String ipAddr, String city, String userAgentString, @NotNull LoginLog annotation, @Nullable Throwable failureReason, String browser, String os) {
        SysLogLoginAddParams sysLogLoginAddParams = new SysLogLoginAddParams();
        sysLogLoginAddParams.setUsername(username);
        sysLogLoginAddParams.setLoginTime(DateUtil.now());
        sysLogLoginAddParams.setIpAddress(ipAddr);
        sysLogLoginAddParams.setCity(city);
        sysLogLoginAddParams.setUserAgent(userAgentString);
        sysLogLoginAddParams.setLoginStatus(annotation.value());
        sysLogLoginAddParams.setFailureReason(failureReason != null ? failureReason.getMessage() : "登录成功");
        sysLogLoginAddParams.setLoginMethod(1);
        sysLogLoginAddParams.setLoginSource(null);
        sysLogLoginAddParams.setBrowser(browser);
        sysLogLoginAddParams.setOs(os);
        return sysLogLoginAddParams;
    }
}
