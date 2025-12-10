package com.zs;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author zsadmin
 */
@Slf4j
@EnableAsync // 开启异步注解功能
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class },scanBasePackages = {"org.jeecg.jmreport","com.zs", "com.anji.captcha"})
public class ZsAdminApplication {


    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(ZsAdminApplication.class, args);


        // 获取 Environment 对象
        Environment environment = context.getEnvironment();

        // 获取端口号和上下文路径
        String port = environment.getProperty("server.port", "未知");
        String contextPath = environment.getProperty("server.servlet.context-path", "/");


        System.out.println("\n================================================");
        System.out.println("  Application: " + "zs-admin");
        System.out.println("  Environment: " + environment.getProperty("spring.profiles.active"));
        System.out.println("  Running on:  http://localhost:" + port + contextPath);
        System.out.println("  版本: v1.0.0" + " 时间: " + DateUtil.now());
        System.out.println("  " + "zs-admin" + " 启动成功!");
        System.out.println("================================================\n");
        System.setProperty("logging.level.root", "INFO");

        log.info("""
                ================================================
                启动成功:
                Application: {}
                Profiles:     {}
                Port:        {}
                ================================================""",
                "zs-admin",
                environment.getProperty("spring.profiles.active", "default"),
                port);
    }
}
