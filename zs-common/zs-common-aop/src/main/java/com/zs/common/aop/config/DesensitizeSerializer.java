package com.zs.common.aop.config;

import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.zs.common.aop.annotation.Desensitize;
import com.zs.common.core.enums.DesensitizeTypeEnum;

import java.io.IOException;

/**
 * 脱敏序列化
 */
public class DesensitizeSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    private DesensitizeTypeEnum desensitizeTypeEnum;
    private int startIndex;
    private int endIndex;



    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        switch (desensitizeTypeEnum) {
            case CUSTOM:
                gen.writeString(value.toString().substring(startIndex, endIndex));
                break;
            case CHINESE_NAME:
                gen.writeString(DesensitizedUtil.chineseName(value.toString()));
                break;
            case ID_CARD:
                gen.writeString(DesensitizedUtil.idCardNum(value.toString(), startIndex, endIndex));
                break;
            case BANK_CARD:
                gen.writeString(DesensitizedUtil.bankCard(value.toString()));
                break;
            case EMAIL:
                gen.writeString(DesensitizedUtil.email(value.toString()));
                break;
            case PASSWORD:
                gen.writeString(DesensitizedUtil.password(value.toString()));
                break;
            case MOBILE_PHONE:
                gen.writeString(DesensitizedUtil.mobilePhone(value.toString()));
                break;
            case FIXED_PHONE:
                gen.writeString(DesensitizedUtil.fixedPhone(value.toString()));
                break;
            case CAR_LICENSE:
                gen.writeString(DesensitizedUtil.carLicense(value.toString()));
                break;
            case ADDRESS:
                gen.writeString(DesensitizedUtil.address(value.toString(), 8));
                break;
            case IPV4:
                gen.writeString(DesensitizedUtil.ipv4(value.toString()));
                break;
            case IPV6:
                gen.writeString(DesensitizedUtil.ipv6(value.toString()));
                break;
            default:
                gen.writeString(value.toString());
                break;
        }
    }


    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        Annotated annotated = property.getMember();
        if (annotated.hasAnnotation(Desensitize.class)) {
            Desensitize desensitize = annotated.getAnnotation(Desensitize.class);
            this.desensitizeTypeEnum = desensitize.type();
            this.startIndex = desensitize.startIndex();
            this.endIndex = desensitize.endIndex();
        }
        return this;
    }
}
