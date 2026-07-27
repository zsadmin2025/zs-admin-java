package com.zs.common.core.crypto;


import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.zs.common.core.constant.RedisConstants;
import com.zs.common.core.crypto.annotation.Decryption;
import com.zs.common.core.enums.CryptoTypeEnum;
import com.zs.common.core.utils.CryptoUtil;
import com.zs.common.core.utils.SecurityUtil;
import com.zs.common.redis.config.RedisUtil;
import io.micrometer.common.lang.Nullable;
import jakarta.annotation.Resource;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 请求参数解密
 */
@RestControllerAdvice
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    @Resource
    private RedisUtil redisUtil;

    /**
     * 第一步被调用：判断当前的拦截器是否支持，如果返回 false, 则该拦截器不处理请求信息
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {

//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        if(attributes == null){
//            return false;
//        }
//        HttpServletRequest request = attributes.getRequest();
//        System.out.println("当前请求方法： " + request);
//        String cryptoKey = request.getHeader("cryptoKey");
//        System.out.println("当前请求头： " + cryptoKey);
//        if(cryptoKey != null){
//          String decrypt =  CryptoUtil.sm2Decrypt(cryptoKey);
//          System.out.println("解密后的请求头： " + decrypt);
//        }
//        // 获取请求路径
//        String path = methodParameter.getExecutable().getDeclaringClass().getSimpleName();
//        System.out.println("当前请求路径： " + path);

        return methodParameter.hasMethodAnnotation(Decryption.class);
    }

    /**
     * 第二步被调用：在读取和转换请求正文之前调用。
     * 在这里可以通过解析 inputMessage 的 body ，对原 body 进行解密，
     * 将解密的数据重新构建一个 HttpInputMessage，来实现加解密操作
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

        Decryption decryption = parameter.getMethodAnnotation(Decryption.class);
        if (decryption == null) {
            return inputMessage;
        }
        try (InputStream originalStream = inputMessage.getBody()) {
            byte[] buffer = originalStream.readAllBytes();
            String encryptedBody = new String(buffer, StandardCharsets.UTF_8).replace("\"", "");
            JSON json = JSONUtil.parse(encryptedBody);
            // 根据json的key获取value

            String jsonStr = json.getByPath("encrypt").toString();

            String decryptedBody = decryptBody(jsonStr, decryption.value());
            InputStream decryptedStream = new ByteArrayInputStream(decryptedBody.getBytes(StandardCharsets.UTF_8));
            return new CustomHttpInputMessage(inputMessage.getHeaders(), decryptedStream);
        } catch (Exception e) {
            throw new IOException("Failed to decrypt the request body", e);
        }
    }

    private String decryptBody(String encryptedBody, CryptoTypeEnum type) {
        return switch (type) {
            case SM4 -> CryptoUtil.sm4Decrypt(encryptedBody, Objects.requireNonNull(redisUtil.get(RedisConstants.SM4_KEY + SecurityUtil.getUserId())).toString());
            case SM2 -> CryptoUtil.sm2Decrypt(encryptedBody);
        };
    }
    /**
     * 第三步被调用：在请求体已经被转换成参数对象之后被调用
     * 在这里因为已经转换成了对象，到了这一步已经不能修改对应的类型了，但是可以修改对象里面的属性
     * 如果在这里处理，可以通过继承的关系来实现加解密
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        System.out.println("解密后的请求报文： " + body);
        return body;
    }

    /**
     * 如果请求体是空时被调用
     */
    @Override
    public Object handleEmptyBody(@Nullable Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

}
