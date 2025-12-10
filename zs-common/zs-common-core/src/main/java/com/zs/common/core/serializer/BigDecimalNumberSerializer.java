package com.zs.common.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalNumberSerializer extends JsonSerializer<BigDecimal> {

    private final int scale; // 保留的小数位数，默认为2

    public BigDecimalNumberSerializer(int scale) {
        this.scale = scale;
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            // 四舍五入并保留指定位数
            BigDecimal scaled = value.setScale(scale, RoundingMode.HALF_UP);
            // 写出为数字类型，而不是字符串
            gen.writeNumber(scaled.stripTrailingZeros().toPlainString());
        }
    }
}