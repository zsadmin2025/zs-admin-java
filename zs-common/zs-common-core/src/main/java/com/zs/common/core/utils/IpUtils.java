package com.zs.common.core.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author zsadmin
 */
@Slf4j
@Component
public class IpUtils {


    private static Searcher searcher;
    private static final String DB_FILE_PATH = "/city/ip2region.xdb";

    @PostConstruct
    private static void initIp2region() {
        try {
            InputStream inputStream = new ClassPathResource(DB_FILE_PATH).getInputStream();
            byte[] dbBinStr = FileCopyUtils.copyToByteArray(inputStream);
            // 创建一个完全基于内存的查询对象
            searcher = Searcher.newWithBuffer(dbBinStr);
        } catch (Exception e) {
            log.info("初始化失败", e);
        }
    }

    /**
     * 获取客户端IP地址
     * <p>
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddr(@NotNull HttpServletRequest request) {
        String[] headers = {"x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
                return getFirstValidIpAddress(ip);
            }
        }
        String remoteAddr = request.getRemoteAddr();
        if ("127.0.0.1".equals(remoteAddr) || "0:0:0:0:0:0:0:1".equals(remoteAddr)) {
            return getLocalIpAddress();
        }
        return remoteAddr;
    }

    private static String getFirstValidIpAddress(@NotNull String ipAddress) {
        if (StringUtils.isNotBlank(ipAddress)) {
            String[] ipAddresses = ipAddress.split(",");
            for (String ip : ipAddresses) {
                if (isValidIpAddress(ip)) {
                    return ip.trim();
                }
            }
        }
        return null;
    }

    private static boolean isValidIpAddress(String ipAddress) {
        return StringUtils.isNotBlank(ipAddress) && !"unknown".equalsIgnoreCase(ipAddress);
    }


    private static String getLocalIpAddress() {
        try {
            InetAddress inet = InetAddress.getLocalHost();
            return inet.getHostAddress();
        } catch (UnknownHostException e) {
            log.error("Failed to get local IP address: ", e);
        }
        return null;
    }


    /**
     * 根据ip从 ip2region.db 中获取地理位置
     * //数据格式： 国家|区域|省份|城市|ISP
     * //192.168.31.160 0|0|0|内网IP|内网IP
     * //47.52.236.180 中国|0|香港|0|阿里云
     * //220.248.12.158 中国|0|上海|上海市|联通
     * //164.114.53.60 美国|0|华盛顿|0|0
     *
     * @return map
     */

    @NotNull
    public static String getCityInfo(String ip) {
        try {
            String searchIpInfo = searcher.search(ip);
            searchIpInfo = searchIpInfo.replace("0|", "");
            return  searchIpInfo;
        }catch (Exception e){
            log.info("Failed to search({}): {}", ip, e.getMessage());
        }
        return "";
    }


    public static void main(String[] args) {
        System.out.println(getCityInfo("47.52.236.180"));
    }

}