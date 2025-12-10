package com.zs.common.mp.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 多租户配置属性类
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "tenant")
public class TenantProperties {

    /**
     * 是否开启多租户
     */
    private Boolean enable;

    /**
     * 租户id字段名
     */
    private String column;

    /**
     * 需要进行租户id过滤的表名集合
     */
    private List<String> filterTables;

    /**
     * 忽略的表前缀
     * 忽略的表前缀，如：sys_
     */
    private List<String> ignoreTablePrefix;

    /**
     * 需要忽略的多租户的表，此配置优先filterTables，若此配置为空则启用filterTables
     */
    private List<String> ignoreTables;

    /**
     * 需要排除租户过滤的登录用户名
     */
    private List<String> ignoreLoginNames;
}
