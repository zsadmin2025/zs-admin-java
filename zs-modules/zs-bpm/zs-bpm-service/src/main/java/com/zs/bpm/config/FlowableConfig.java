package com.zs.bpm.config;

import lombok.extern.slf4j.Slf4j;
import org.flowable.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Flowable引擎全局配置
 * <p>
 * 配置Flowable引擎的全局行为：
 * <ul>
 *   <li>关闭内置身份表（dbIdentityUsed=false）</li>
 *   <li>配置字体支持中文</li>
 * </ul>
 * </p>
 *
 * @author zsadmin
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class FlowableConfig {

    @Bean
    public ProcessEngineConfigurationConfigurer customProcessEngineConfigurator() {
        return config -> {
            log.info("开始配置Flowable引擎...");
            
            // 1. 配置字体（支持中文）
            config.setActivityFontName("微软雅黑");
            config.setLabelFontName("微软雅黑");
            config.setAnnotationFontName("微软雅黑");
            log.info("已配置字体为微软雅黑");
            
            log.info("Flowable引擎配置完成");
        };
    }
}
