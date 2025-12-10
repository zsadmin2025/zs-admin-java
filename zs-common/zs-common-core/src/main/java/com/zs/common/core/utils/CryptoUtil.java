package com.zs.common.core.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SM4;
import org.bouncycastle.util.encoders.Hex;

/**
 * 加密解密工具
 */
public class CryptoUtil {

//    // sm4密钥
//    private static  final String SM4_KEY = "5010d0bbd0b56f65";
    // 公钥
    private static final String PUBLIC_KEY = "04babc11e13cac8eec36457420c5fd7ee5ae2ce21df6a8907dbce8f667750a7069ab6054a6db718daa0b0bb49808db150704dff5d39358513ac472bee04120274e";
    // 私钥
    private static final String PRIVATE_KEY = "6a487bdb1e5752edbadd893fc21484d2c1f66ef9b062b607c3df3fb839c07efe";


//    // 生成SM4密钥
//    private static final byte[] sm4Key;

//    static {
//        try {
//            sm4Key = generateSM4Key();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to generate SM4 key", e);
//        }
//    }

//    private static byte[] generateSM4Key() throws Exception {
//        return KeyUtil.generateKey(SM4.ALGORITHM_NAME, SM4_KEY.getBytes(StandardCharsets.UTF_8)).getEncoded();
//    }

    /**
     * sm2加密
     * @param plainText 明文
     * @return 密文
     */
    public static String sm2Encrypt(String plainText) {
        SM2 sm2 = SmUtil.sm2(PRIVATE_KEY, PUBLIC_KEY);
        return sm2.encryptHex(plainText, CharsetUtil.CHARSET_UTF_8, KeyType.PublicKey);
    }

    /**
     * sm2解密
     * @param cipherText 密文
     * @return 明文
     */
    public static String sm2Decrypt(String cipherText) {
        SM2 sm2 = SmUtil.sm2(PRIVATE_KEY, PUBLIC_KEY);
        return sm2.decryptStr(cipherText, KeyType.PrivateKey, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * sm4加密
     * @param plainText 明文
     * @return 密文
     */
    public static String sm4Encrypt(String plainText, String key) {
        try {
            SM4 sm4 = SmUtil.sm4(HexUtil.decodeHex(key));
            return sm4.encryptHex(plainText, CharsetUtil.CHARSET_UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt with SM4", e);
        }
    }

    /**
     * sm4解密
     * @param cipherText 密文
     * @return 明文
     */
    public static String sm4Decrypt(String cipherText, String key) {
        try {
            SM4 sm4 = SmUtil.sm4(HexUtil.decodeHex(key));
            return sm4.decryptStr(cipherText, CharsetUtil.CHARSET_UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt with SM4", e);
        }
    }


    private static String bytesToHex(byte[] bytes) {
        return new String(Hex.encode(bytes));
    }
    public static void main(String[] args) {

        String sm4 = "4262494f6d776a545263624e6a457967";
//        // sm4是string转换成十六进制的字符串，将sm4从16进制转换为原来的字符串
//        // 将十六进制字符串转换为字节数组
//        byte[] bytes = HexUtil.decodeHex(sm4);
//
//        // 假设字符串是以 UTF-8 编码的
//        String originalStr = new String(bytes, StandardCharsets.UTF_8);
//
//        System.out.println("原始字符串: " + originalStr);
//
//
//        // 16进制转换字符串
//        String sm41 = Arrays.toString(Hex.decode(sm4));
//        System.out.println("sm4：" + sm41);

        String str = "044fcdc036d06792606d581df62a78da56f98b648c4adc187d75f2dcc362f86023b3e01fda19932efc8a63969166bce9d1bbf5645af76771932e1fe5d57096f58a60155306f51e1c8d3fe429b340ca03efe831494041c913e7615352a473ed4040ea32eb9f246dc5b25f3d3d6857c1cedef6df871447a6f5efe6fda3f37c449d7a1e65759da3f9f7157584a205395f1e8c39157274525c51210f404ee91bf55d8d2fd45c4cfb8a47585887fc5ae2069575f1b806cb27d64350a020d46d5c2163bd99bd3e0c201c56177f374fc2e0c9fb52d173b64b80f243e9323b0a89a4a53de941d59712c1485ad09e89aefab309cd01db7485b0480e4bbe6941c42ade74b8cd56e62e2219cb5f3041cc1cef8a69583328667a711b5098aafb08d4ef0a3fce087c6dfb99543284a3fb335cdbc103d56520c2e2c107f4d1cd2a5f2166d671eb2161d759c1143fb890f16080d3f785ef9077ef0ce7a33c5b8979df9118ba1aaeaba5fd471cc77a767da75ccf58403716432ceee6198ea6d760d766871474f71dfcca0c5f1f56248e68ec3f0f8df01a6fe95aaf19e76894aa810318f7f9351e4539d3f18bd07275cba6e231";

//        String encrypt = sm4Encrypt(str, sm4);
//        System.out.println("加密：" + encrypt);
        String decrypt = sm4Decrypt(str, sm4);
        System.out.println("解密：" + decrypt);
    }
}
