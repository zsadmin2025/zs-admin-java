package com.zs.common.core.config;


import com.zs.common.core.interceptor.TenantInterceptor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zs
 */
@Configuration()
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private TenantInterceptor tenantInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantInterceptor);
    }

    // Jackson 序列化配置已移至 MyJacksonConfig（Jackson2ObjectMapperBuilderCustomizer）
    // 注意：不要使用 configureMessageConverters 或 extendMessageConverters
    // 自定义 MappingJackson2HttpMessageConverter 会拦截 String 返回值导致 Knife4j/SpringDoc 的
    // OpenAPI 响应被异常 base64 编码
}
