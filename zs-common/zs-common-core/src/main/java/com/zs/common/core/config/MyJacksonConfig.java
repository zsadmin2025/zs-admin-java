package com.zs.common.core.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zs.common.core.serializer.BigDecimalNumberSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.TimeZone;

@Configuration
public class MyJacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // 设置全局特性
            builder.locale(Locale.CHINA)
                    .timeZone(TimeZone.getTimeZone("GMT+8"))
                    .featuresToDisable(
                            SerializationFeature.INDENT_OUTPUT,
                            SerializationFeature.FAIL_ON_EMPTY_BEANS,
                            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
                    )
                    .serializerByType(Long.class, new ToStringSerializer())
                    .serializerByType(Long.TYPE, new ToStringSerializer());

            // 添加时间支持
            builder.modules(new JavaTimeModule());

            // 设置 JsonInclude.Include.ALWAYS 策略
            builder.serializationInclusion(JsonInclude.Include.ALWAYS);

            // 添加 BigDecimal 序列化器
            int defaultScale = 2;
            SimpleModule bigDecimalModule = new SimpleModule();
            bigDecimalModule.addSerializer(BigDecimal.class, new BigDecimalNumberSerializer(defaultScale));
            builder.modules(bigDecimalModule);

            // 添加 String null -> "" 处理
            SimpleModule stringModule = new SimpleModule();
            stringModule.addSerializer(String.class, new CustomStringSerializer());
            builder.modules(stringModule);

            // 添加 Boolean null -> false 处理
            SimpleModule booleanModule = new SimpleModule();
            booleanModule.addSerializer(Boolean.class, new CustomBooleanSerializer());
            builder.modules(booleanModule);
        };
    }

    // 自定义序列化器处理 String 类型的 null 值
    static class CustomStringSerializer extends com.fasterxml.jackson.databind.JsonSerializer<String> {
        @Override
        public void serialize(String value, com.fasterxml.jackson.core.JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws IOException {
            gen.writeString(value == null ? "" : value);
        }
    }

    // 自定义序列化器处理 Boolean 类型的 null 值
    static class CustomBooleanSerializer extends com.fasterxml.jackson.databind.JsonSerializer<Boolean> {
        @Override
        public void serialize(Boolean value, com.fasterxml.jackson.core.JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws IOException {
            gen.writeBoolean(value != null && value);
        }
    }
}