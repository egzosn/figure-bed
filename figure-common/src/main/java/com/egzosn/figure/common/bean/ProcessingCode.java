package com.egzosn.figure.common.bean;

/**
 * 处理代码
 * @author egan
 *         email egzosn@gmail.com
 *         date 2018/8/21.11:13
 */
public enum ProcessingCode {

    SUCCESS(0),SERVER_ERROR(500),UNAUTH(99);

    /**
     * 状态码
     */
    private int code;

    ProcessingCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
