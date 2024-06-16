package com.mars.cds.support;


/**
 * 接口返回对象工具类
 */
public final class RespBodyUtils {

    public static final int success = 1; // 成功
    public static final int failure = 0; // 失败

    private RespBodyUtils() {

    }

    /**
     * 请求处理失败时，返回的错误提示
     *
     * @param msg 错误描述
     */
    public static <T> RespBody<T> failure(String msg) {
        return new RespBody<T>(failure, msg);
    }

    /**
     * 请求处理失败时，返回的错误提示
     *
     * @param msg  错误描述
     * @param data 数据
     */
    public static <T> RespBody<T> failure(String msg, T data) {
        return new RespBody<T>(failure, msg, data);
    }

    /**
     * 请求处理失败时，返回的错误提示
     *
     * @param code 自定义编码
     * @param msg  错误描述
     */
    public static <T> RespBody<T> failure(int code, String msg) {
        return new RespBody<T>(code, msg);
    }

    /**
     * 请求处理失败时，返回的错误提示
     *
     * @param code 自定义编码
     * @param msg  错误描述
     */
    public static <T> RespBody<T> failure(int code, String msg, T data) {
        return new RespBody<T>(code, msg, data);
    }

    /**
     * 请求处理成功
     */
    public static <T> RespBody<T> success() {
        return success("");
    }

    /**
     * 请求处理成功
     *
     * @param msg 错误描述
     */
    public static <T> RespBody<T> success(String msg) {
        return new RespBody<T>(success, msg);
    }

    /**
     * 请求处理成功
     *
     * @param msg  成功描述
     * @param data 数据
     */
    public static <T> RespBody<T> success(String msg, T data) {
        return new RespBody<T>(success, msg, data);
    }

    /**
     * 请求处理成功
     *
     * @param code 自定义编码
     * @param msg  成功描述
     */
    public static <T> RespBody<T> success(int code, String msg) {
        return new RespBody<T>(code, msg);
    }

    /**
     * 请求处理成功
     *
     * @param code 自定义编码
     * @param msg  成功描述
     * @param data 数据
     */
    public static <T> RespBody<T> success(int code, String msg, T data) {
        return new RespBody<T>(code, msg, data);
    }
}
