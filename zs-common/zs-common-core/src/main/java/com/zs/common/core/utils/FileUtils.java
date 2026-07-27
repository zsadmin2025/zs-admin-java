package com.zs.common.core.utils;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;

import java.util.Date;

public class FileUtils {

    private static final String DATE_PATTERN = "yyyy/MM/dd";

    private FileUtils() {
    }

    public static String generateUniqueFileName(String originalFilename) {
        String extension = getExtension(originalFilename);
        return IdUtil.fastSimpleUUID() + extension;
    }

    public static String generateObjectKey(String originalFilename) {
        String datePath = DateUtil.format(new Date(), DATE_PATTERN);
        return datePath + "/" + generateUniqueFileName(originalFilename);
    }

    public static String getExtension(String filename) {
        if (filename == null) return "";
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex);
        }
        return "";
    }
}

