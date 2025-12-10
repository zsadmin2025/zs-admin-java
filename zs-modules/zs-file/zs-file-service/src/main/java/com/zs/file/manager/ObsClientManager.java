package com.zs.file.manager;

import com.obs.services.ObsClient;
import com.zs.common.core.model.file.Huawei;

import java.io.IOException;

/**
 * 华为云对象存储客户端管理器
 */
public class ObsClientManager {

    private static volatile ObsClient obsClient;


    public static ObsClient getObsClient(Huawei huawei) {

        if (obsClient == null) {
            synchronized (ObsClientManager.class) {
                if (obsClient == null) {
                    obsClient = new ObsClient(huawei.getAccessKey(), huawei.getSecretAccessKey(), huawei.getEndPoint());

                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        if (obsClient != null) {
                            try {
                                obsClient.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println("ObsClient 已关闭");
                        }
                    }));
                }
            }
        }
        return obsClient;
    }

    public static void shutdown() {
        if (obsClient != null) {
            try {
                obsClient.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            obsClient = null;
        }
    }
}
