package com.zs.common.core.reporter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用启动时在控制台输出各业务模块的加载情况
 *
 * @author zsadmin
 */
@Slf4j
@Component
public class ModuleLoadReporter implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * 模块定义列表 —— 新增模块只需在这里加一行即可
     */
    private static final List<ModuleDefinition> MODULES = List.of(
            new ModuleDefinition("zs-system",    "系统管理",   "com.zs.sys.user.service.impl.SysUserServiceImpl"),
            new ModuleDefinition("zs-file",      "文件管理",   "com.zs.file.factory.FileFactory"),
            new ModuleDefinition("zs-mail",      "邮件服务",   "com.zs.mail.config.MailConfig"),
            new ModuleDefinition("zs-sms",       "短信服务",   "com.zs.sms.factory.SmsFactory"),
            new ModuleDefinition("zs-websocket", "WebSocket", "com.zs.config.WebSocketConfig"),
            new ModuleDefinition("zs-quartz",    "定时任务",   "com.zs.quartz.utils.QuartzUtils"),
            new ModuleDefinition("zs-generator", "代码生成",   "com.zs.gen.config.GenConfigProperties"),
            new ModuleDefinition("zs-bpm",       "业务流程",   "com.zs.bpm.BpmMarker")
    );

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<ModuleStatus> statuses = resolveModuleStatuses();
        long loadedCount = statuses.stream().filter(ModuleStatus::loaded).count();

        System.out.println();
        System.out.println("  ========================================");
        System.out.println("       业务模块加载报告  (" + loadedCount + "/" + statuses.size() + ")");
        System.out.println("  ========================================");

        for (ModuleStatus s : statuses) {
            String status = s.loaded() ? "LOADED " : "ABSENT ";
            String info = s.loaded() ? "" : " — 未引入依赖";
            System.out.printf("   [%s] %-12s  %s%s%n", status, s.name(), s.description(), info);
        }

        System.out.println("  ========================================");
        System.out.println();

        // 同时输出到日志
        log.info("业务模块加载完成: {}/{} 个模块已加载", loadedCount, statuses.size());
        for (ModuleStatus s : statuses) {
            if (s.loaded()) {
                log.info("  [LOADED]  {} — {}", s.name(), s.description());
            } else {
                log.warn("  [ABSENT]  {} — {} (未引入依赖)", s.name(), s.description());
            }
        }
    }

    /**
     * 解析各模块状态
     */
    private List<ModuleStatus> resolveModuleStatuses() {
        List<ModuleStatus> result = new ArrayList<>();
        for (ModuleDefinition def : MODULES) {
            boolean present = ClassUtils.isPresent(def.markerClass(), getClass().getClassLoader());
            result.add(new ModuleStatus(def.name(), def.description(), present));
        }
        return result;
    }

    /**
     * 模块定义
     */
    private record ModuleDefinition(String name, String description, String markerClass) {}

    /**
     * 模块加载状态
     */
    private record ModuleStatus(String name, String description, boolean loaded) {}
}
