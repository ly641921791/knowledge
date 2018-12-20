package com.sz.seckill.result;

import lombok.AllArgsConstructor;

/**
 * @author ly
 */
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(1, "success"),

    SERVER_ERROR(500, "服务端异常");

    int code;
    String msg;
}
