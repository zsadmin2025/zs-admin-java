package com.zs.file.manager;

import com.aliyun.sdk.service.oss2.OSSAsyncClient;
import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.credentials.Credentials;
import com.aliyun.sdk.service.oss2.credentials.CredentialsProvider;
import com.aliyun.sdk.service.oss2.credentials.CredentialsProviderSupplier;
import com.zs.common.core.model.file.Aliyun;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OSSAsyncClientManager {

    private static volatile OSSAsyncClient ossClient;

    public static OSSAsyncClient getOSSClient(Aliyun aliyun){
        if (aliyun == null) {
            throw new IllegalArgumentException("Aliyun 配置不能为空");
        }

        if (ossClient == null){
            synchronized (OSSAsyncClientManager.class){
                if (ossClient == null){
                    CredentialsProvider provider = new CredentialsProviderSupplier(
                            () -> new Credentials(aliyun.getAccessKeyId(), aliyun.getAccessKeySecret()));

                    String region = extractRegionFromEndpoint(aliyun.getEndpoint());
                    if (region == null) {
                        throw new IllegalArgumentException(
                                "无法从 endpoint 中提取 region，请检查配置: " + aliyun.getEndpoint());
                    }
                    // 构建 OSSClient
                    ossClient = OSSAsyncClient.newBuilder()
                            .credentialsProvider(provider)
                            .endpoint(aliyun.getEndpoint())
                            .region(region)
                            .build();

                    // 注册 JVM 关闭钩子，自动关闭客户端
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        try {
                            ossClient.close();
                            log.info("OSS Client 已关闭");
                        } catch (Exception e) {
                            log.error("关闭 OSS Client 失败", e);
                        }
                    }));
                }
            }
        }


        return ossClient;
    }


    /**
     * 从 endpoint 提取 region ID，例如 oss-cn-hangzhou.aliyuncs.com -> cn-hangzhou
     */
    private static String extractRegionFromEndpoint(String endpoint) {
        if (endpoint == null || !endpoint.startsWith("oss-")) return null;
        int endIndex = endpoint.indexOf('.');
        return endIndex > 4 ? endpoint.substring(4, endIndex) : null;
    }
}
