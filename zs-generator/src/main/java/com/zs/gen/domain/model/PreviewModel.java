package com.zs.gen.domain.model;

import lombok.Data;

@Data
public class PreviewModel {

    private String controller;

    @Data
    private static class domain {
        private String entity;

        private String params;

        private String vo;

        private String excel;
    };

    private String mapper;

    @Data
    private static class service {
        private String impl;
    }
}
