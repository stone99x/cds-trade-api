package com.mars.cds.support;


import lombok.Data;

/**
 * 接口返回对象
 */
@Data
public final class RespBody<T> {
    // 默认状态:0表示失败, 1表示成功
    private int code;
    // 请求处理描述
    private String msg;
    // 返回数据集
    private T data;

    public RespBody() {

    }

    public RespBody(int code) {
        this.code = code;
    }

    public RespBody(int code, String msg) {
        this(code);
        this.msg = msg;
    }

    public RespBody(int code, String msg, T data) {
        this(code, msg);
        this.data = data;
    }
}
