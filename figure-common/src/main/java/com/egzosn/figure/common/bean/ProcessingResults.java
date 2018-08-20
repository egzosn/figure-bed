package com.egzosn.figure.common.bean;

/**
 * 处理结果
 * @author egan
 *         email egzosn@gmail.com
 *         date 2018/8/20.19:53
 */
public class ProcessingResults{

    /**
     * 状态码， 0为成功
     */
    private int code;

    /**
     * 处理消息
     */
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
