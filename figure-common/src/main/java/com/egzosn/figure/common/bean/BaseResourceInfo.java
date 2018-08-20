package com.egzosn.figure.common.bean;

/**
 *  基础资源
 * @author egan
 *         email egzosn@gmail.com
 *         date 2018/8/20.20:09
 */
public class BaseResourceInfo {

    /**
     * 文件唯一标识信息
     */
    private String id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件存放地址，正常为相对地址
     */
    private String path;

    /**
     * 文件MD5
     */
    private String md5;
    /**
     * 附加信息，开发者自行定义使用
     */
    private String addition;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }
}
