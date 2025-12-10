package com.zs.file.manager;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import com.zs.common.core.model.file.Tencent;

/**
 * 腾讯云COS对象存储客户端管理器
 */
public class COSClientManager {

    private static volatile COSClient cosClient;


    public static COSClient getCOSClient(Tencent tencent) {

        if (cosClient == null) {
            synchronized (COSClientManager.class) {
                if (cosClient == null) {
                    COSCredentials cred = new BasicCOSCredentials(tencent.getSecretId(), tencent.getSecretKey());
                    Region region = new Region(tencent.getRegion());
                    ClientConfig clientConfig = new ClientConfig(region);
                    clientConfig.setHttpProtocol(HttpProtocol.https);
                    cosClient = new COSClient(cred, clientConfig);

                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        if (cosClient != null) {
                            cosClient.shutdown();
                            System.out.println("COS Client 已关闭");
                        }
                    }));
                }
            }
        }
        return cosClient;
    }


    public static void shutdown() {
        if (cosClient != null) {
            cosClient.shutdown();
            cosClient = null;
        }
    }
}
