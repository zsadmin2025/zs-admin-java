package com.zs.common.core.config;


import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zs.common.core.constant.Constants;
import com.zs.common.core.constant.RedisConstants;
import com.zs.common.core.interceptor.TenantInterceptor;
import com.zs.common.core.model.file.SysConfigFileVO;
import com.zs.common.redis.config.RedisUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 */
@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {



    @Resource
    private RedisUtil redisUtil;

    // 文件上传路径
    String filePath;
    // 前缀
    String prefix;

    @PostConstruct
    public void init() {
        try{
            Object object = redisUtil.get(RedisConstants.SYS_DICT_CONFIG_KEY + Constants.FILE_UPLOAD);
            if (object == null) {
                log.warn("Redis 中未找到文件上传路径配置");
                return;
            }
            // 转换为 VO 对象
            SysConfigFileVO sysConfigFileVO = JSONUtil.toBean(JSONUtil.toJsonStr(object), SysConfigFileVO.class);
            if (sysConfigFileVO == null || sysConfigFileVO.getLocal() == null) {
                log.warn("解析文件配置失败：配置对象为空");
                return;
            }
            String path = sysConfigFileVO.getLocal().getPath();
            String prefix = sysConfigFileVO.getLocal().getPrefix();

            if (path == null || path.trim().isEmpty()) {
                log.warn("解析文件配置失败：路径为空");
                return;
            }

            this.filePath = path;
            this.prefix = prefix;
            log.info("成功加载文件路径: {}", path);

        }catch (Exception e){
            log.error("初始化文件路径配置失败", e);
        }

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TenantInterceptor());
    }

    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        String filePath = this.filePath.endsWith("/") ?  this.filePath : this.filePath + "/";
        registry.addResourceHandler("/file/**").addResourceLocations("file:" + filePath);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        // 1. Create a new ObjectMapper for Jackson
        ObjectMapper objectMapper = new ObjectMapper();

        // 2. Configure the ObjectMapper
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false); // Do not write null map values
        objectMapper.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, true); // Unwrap single element arrays
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // Exclude null values

        // Date format configuration
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // Write dates as ISO-8601
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); // Custom date format

        // 3. Create a new MappingJackson2HttpMessageConverter
        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);

        // 4. Set the supported media types
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8); // If you need UTF-8 support
        jacksonConverter.setSupportedMediaTypes(supportedMediaTypes);

        // 将 Long 类型序列化为字符串
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);

        // 5. Add the converter to the list
        converters.add(jacksonConverter);
    }
}
