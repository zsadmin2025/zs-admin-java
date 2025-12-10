package com.zs.common.security.propetties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author zsadmin
 */
@Configuration
@ConfigurationProperties(prefix = "security.white")
@Data
public class WhiteUrlProperties {

    /**
     * 需要忽略的接口路径 不限制请求类型
     */
    public List<String> url;
}
