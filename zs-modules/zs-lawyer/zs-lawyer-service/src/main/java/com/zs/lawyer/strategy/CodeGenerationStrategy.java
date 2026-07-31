package com.zs.lawyer.strategy;

public interface CodeGenerationStrategy {

    String generateCode(String prefix, String datePattern, int seqLength);
}
