package com.zs.common.mp.repository;

/**
 * 用于获取指定前缀下的最大业务编号
 */
public interface CodeGeneratorRepository {

    String findMaxCodeByPrefix(String prefix);
}
