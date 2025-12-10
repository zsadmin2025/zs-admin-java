package com.zs.common.mp.repository;

import cn.hutool.core.date.DateUtil;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class UniversalCodeGenerator {

    private final CodeGeneratorRepository codeGeneratorRepo;
    private final Lock lock = new ReentrantLock();

    public UniversalCodeGenerator(CodeGeneratorRepository codeGeneratorRepo) {
        this.codeGeneratorRepo = codeGeneratorRepo;
    }

    public String generateCode(String prefix, String datePattern, int seqLength) {
        lock.lock();
        try {
            return doGenerateCode(prefix, datePattern, seqLength);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 通用编号生成方法
     *
     * @param prefix       前缀，如 "KH"
     * @param datePattern  日期格式，如 "yyyyMMdd"
     * @param seqLength    序号长度，如 6
     * @return             生成的编号
     */
    public String doGenerateCode(String prefix, String datePattern, int seqLength) {
        String dateStr = DateUtil.format(new Date(), datePattern); //根据格式获取当前日期字符串
        String pattern = "%0" + seqLength + "d";
        String fullPrefix = prefix + dateStr;

        // 查询当前前缀下的最大编号
        String maxCode = codeGeneratorRepo.findMaxCodeByPrefix(fullPrefix);

        int nextNum = 1;
        if (maxCode != null && maxCode.length() >= fullPrefix.length() + seqLength) {
            String numStr = maxCode.substring(fullPrefix.length(), fullPrefix.length() + seqLength);
            try {
                nextNum = Integer.parseInt(numStr) + 1;
            } catch (NumberFormatException e) {
                nextNum = 1;
            }
        }

        return fullPrefix + String.format(pattern, nextNum);
    }
}
