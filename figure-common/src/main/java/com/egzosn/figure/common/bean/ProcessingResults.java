package com.egzosn.figure.common.bean;

/**
 * 处理结果
 * @author egan
 *         email egzosn@gmail.com
 *         date 2018/8/20.19:53
 */
public final class ProcessingResults{


    /**
     * 状态码， 0为成功
     */
    private int code;

    /**
     * 处理消息
     */
    private String message;
    /**
     * 处理结果数据
     */
    private Object data;

    public final static ProcessingResults SUCCESS = new ProcessingResults();

    public final static ProcessingResults ERROR(String message){
        return new ProcessingResults(ProcessingCode.SERVER_ERROR.getCode(), message);
    }

    public ProcessingResults() {
    }

    public ProcessingResults(int code) {
        this.code = code;
    }

    public ProcessingResults(int code, String message) {
        this.code = code;
        this.message = message;
    }

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
