package com.zs.common.core.core;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author zsadmin
 */
@Data
@Schema(description = "统一返回结果")
public class Result<T> implements Serializable {

    @Schema(description = "状态码")
    private Integer code;

    @Schema(description = "返回信息")
    private String msg;

    @Schema(description = "返回数据")
    private T data;

    @Schema(description = "时间戳")
    private long timestamp = System.currentTimeMillis();

    public Result() {

    }

    public Result(@NotNull HttpEnum httpEnum) {
        this.code = httpEnum.getCode();
        this.msg = httpEnum.getMsg();
    }

    public Result(@NotNull HttpEnum httpEnum, String msg) {
        this.code = httpEnum.getCode();
        this.msg = msg;
    }

    public Result(String msg) {
        this.code = HttpEnum.INTERNAL_SERVER_ERROR.getCode();
        this.msg = msg;
        this.data = null;
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public Result(@NotNull HttpEnum httpEnum, @Nullable T data) {
        this.code = httpEnum.getCode();
        this.msg = httpEnum.getMsg();
        this.data = data;
    }

    public Result(Integer code, String msg, @Nullable T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @NotNull
    public Result<T> ok() {
        return new Result<>(HttpEnum.OK);
    }

    @NotNull
    public Result<T> ok(T data) {
        return new Result<>(HttpEnum.OK, data);
    }

    @NotNull
    public Result<T> ok(@NotNull HttpEnum httpEnum) {
        return new Result<>(httpEnum);
    }

    @NotNull
    public Result<T> ok(Integer code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    @NotNull
    public Result<T> error() {
        return new Result<>( HttpEnum.INTERNAL_SERVER_ERROR);
    }

    @NotNull
    public Result<T> error(T data) {
        return new Result<>(HttpEnum.INTERNAL_SERVER_ERROR, data);
    }

    @NotNull
    public Result<T> error(@NotNull HttpStatus httpStatus) {
        return new Result<>(httpStatus.value(), httpStatus.getReasonPhrase());
    }

    @NotNull
    public Result<T> error(String msg) {
        return new Result<>(msg);
    }

    @NotNull
    public Result<T> error(@NotNull HttpEnum httpEnum) {
        return new Result<>(httpEnum);
    }

    @NotNull
    public Result<T> error(Integer code, String msg) {
        return new Result<>(code, msg);
    }

    @NotNull
    public Result<T> error(@NotNull HttpEnum httpEnum, String msg) {
        return new Result<>(httpEnum, msg);
    }

    @NotNull
    public Result<T> error(Integer code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

}
