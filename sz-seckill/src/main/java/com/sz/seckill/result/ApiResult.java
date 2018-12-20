package com.sz.seckill.result;

import lombok.Data;

/**
 * @author ly
 */
@Data
public class ApiResult<T> {

    private int code;
    private String msg;
    private T data;

    private ApiResult(T data) {
        this.code = 1;
        this.msg = "success";
        this.data = data;
    }

    private ApiResult(ErrorCode errorCode) {
        this.code = errorCode.code;
        this.msg = errorCode.msg;
    }

    private ApiResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 成功返回
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<T>(data);
    }

    /**
     * 失败返回
     *
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> error(ErrorCode errorCode) {
        return new ApiResult<T>(errorCode);
    }
}
