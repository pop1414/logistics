package com.logistics.rbacbackend.common;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-13:48
 * @description com.logistics.rbacbackend.common
 */

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 统一返回体：
 * 成功：{"code":200,"msg":"ok","data":...}
 * 失败：{"code":400,"msg":"xxx","data":null}
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 200=成功，其它=失败
     */
    private int code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // ===== static factory =====

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(200, "ok", null);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(200, "ok", data);
    }

    public static <T> ApiResponse<T> ok(String msg, T data) {
        return new ApiResponse<>(200, msg == null ? "ok" : msg, data);
    }

    public static <T> ApiResponse<T> fail(int code, String msg) {
        return new ApiResponse<>(code, msg, null);
    }

    public static <T> ApiResponse<T> fail(String msg) {
        return new ApiResponse<>(400, msg, null);
    }

    // ===== getters/setters =====

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
