package com.zs.file.domain.dto;

import lombok.Data;


@Data
public class SysFileAddDto {

    private Integer type;

    @Data
    private static class local {
        private String domain;
        private String prefix;
        private String path;
    }
}
