package com.zs.common.core.exception;

import lombok.Data;

@Data
public class ErrorCode {

    private Integer code;

    private String msg;

    public ErrorCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
