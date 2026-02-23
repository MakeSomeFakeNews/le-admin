package me.dqq.leadmin.common;

import lombok.Data;

@Data
public class R<T> {
    private int code;
    private T data;
    private String message;
    private boolean success;

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.code = 200;
        r.data = data;
        r.message = "请求成功";
        r.success = true;
        return r;
    }

    public static <T> R<T> ok() {
        return ok(null);
    }

    public static <T> R<T> fail(String message) {
        R<T> r = new R<>();
        r.code = 400;
        r.data = null;
        r.message = message;
        r.success = false;
        return r;
    }

    public static <T> R<T> fail(int code, String message) {
        R<T> r = new R<>();
        r.code = code;
        r.data = null;
        r.message = message;
        r.success = false;
        return r;
    }
}
