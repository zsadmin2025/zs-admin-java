package com.zs.file.manager;


import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.Credentials;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentials;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.common.comm.SignVersion;
import com.zs.common.core.model.file.Aliyun;

/**
 * OSS 阿里云客户端管理器
 */
public class OSSClientManager {

    private static volatile OSS ossClient;

    public static OSS getOSSClient(Aliyun aliyun) {
        if (ossClient == null) {
            synchronized (OSSClientManager.class) {
                if (ossClient == null) {
                    // 配置客户端
                    ClientBuilderConfiguration config = new ClientBuilderConfiguration();
                    config.setSignatureVersion(SignVersion.V4); // 签名版本
                    config.setProtocol(Protocol.HTTPS); // 设置使用 HTTPS

                    // 凭证提供者
                    CredentialsProvider credentialsProvider = new CredentialsProvider() {

                        @Override
                        public void setCredentials(Credentials credentials) {
                        }

                        @Override
                        public Credentials getCredentials() {
                            // 返回长期凭证 access_key_id, access_key_secrect
                            return new DefaultCredentials(aliyun.getAccessKeyId(), aliyun.getAccessKeySecret());
                        }
                    };

                    // 构建 OSS 客户端
                    ossClient = OSSClientBuilder.create()
                            .endpoint(aliyun.getEndpoint())           // 使用配置中的 endpoint
                            .region(extractRegionFromEndpoint(aliyun.getEndpoint()))
                            .credentialsProvider(credentialsProvider)
                            .clientConfiguration(config)
                            .build();

                    // 注册 JVM 关闭钩子，自动关闭客户端
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        if (ossClient != null) {
                            ossClient.shutdown();
                            System.out.println("OSS Client 已关闭");
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

    /**
     * 手动关闭 OSSClient（可用于测试或手动重置）
     */
    public static void shutdown() {
        if (ossClient != null) {
            ossClient.shutdown();
            ossClient = null;
        }
    }
}