package com.zs.common.core.config;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.zs.common.core.interceptor.TenantInterceptor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

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

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        // 1. 创建 Jackson 的 ObjectMapper 核心对象
        ObjectMapper objectMapper = new ObjectMapper();

        // 2. 基础序列化配置
        // 排除 null 值字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 不输出 Map 中 null 值的键值对
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        // 单个元素的数组不展开（建议关闭，展开可能导致前端解析异常）
        objectMapper.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, false);
        // 日期不序列化为时间戳，使用文本格式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 忽略未知的 JSON 字段，避免反序列化报错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 3. 日期格式配置（兼容传统 Date 和 Java 8 时间类型）
        // 配置传统 Date 类型的序列化格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 设置时区（避免时区偏移导致日期显示错误）
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        objectMapper.setDateFormat(dateFormat);

        // 支持 Java 8 时间类型（LocalDateTime/LocalDate 等）
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 序列化 LocalDateTime
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        // 反序列化 LocalDateTime
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
        objectMapper.registerModule(javaTimeModule);

        // 4. Long 类型序列化为字符串（解决前端大数精度丢失问题）
        SimpleModule longModule = new SimpleModule();
        // 覆盖 Long 类型的序列化器，转为字符串
        longModule.addSerializer(Long.class, ToStringSerializer.instance);
        longModule.addSerializer(Long.TYPE, ToStringSerializer.instance); // 兼容基本类型 long
        objectMapper.registerModule(longModule);

        // 5. 创建并配置 Jackson 消息转换器
        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);

        // 6. 设置支持的媒体类型（APPLICATION_JSON_UTF8 已过时，建议用 APPLICATION_JSON）
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        // 兼容旧版客户端的 UTF-8 声明
        supportedMediaTypes.add(MediaType.valueOf("application/json;charset=UTF-8"));
        jacksonConverter.setSupportedMediaTypes(supportedMediaTypes);

        // 7. 将自定义转换器添加到列表（建议添加到首位，优先使用）
        converters.add(0, jacksonConverter);
    }
}
