package com.egzosn.figure.common.bean;


/**
 * 资源信息
 * @author egan
 *         email egzosn@gmail.com
 *         date 2018/8/20.19:18
 */
public class ResourceInfo extends BaseResourceInfo{

    /**
     * 上传时文件名
     */
    private String uploadFileName;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 内容类型
     */
    private String contentType;


    /**
     * 文件信息
     */
    private byte[] file;


    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
