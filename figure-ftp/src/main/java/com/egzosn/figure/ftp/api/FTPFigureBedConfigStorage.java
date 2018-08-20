package com.egzosn.figure.ftp.api;

import com.egzosn.figure.common.api.FigureBedConfigStorage;

/**
 * FTP服务配置
 * @author egan
 *         email egzosn@gmail.com
 *         date 2018/8/20.20:20
 */
public class FTPFigureBedConfigStorage implements FigureBedConfigStorage {
    /**
     * FTP服务器主机地址
     */
    private String hostname;
    /**
     *  FTP服务器端口
     */
    private int port;
    /**
     * FTP登录账号
     */
    private String userName;
    /**
     * FTP登录密码
     */
    private String password;
    /**
     * 编码格式
     */
    private String encoding;
    /**
     * 资源存放目录，这里为业务根路径，非必须
     */
    private String workingDirectory;

    /**
     * 资源访问地址，域名等
     */
    private String accessAddress;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 编码格式
     *
     * @return 编码格式
     */
    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    /**
     * 获取密钥
     *
     * @return 密钥
     */
    public String getSecretKey() {
        return password;
    }
    /**
     * 访问地址，一般为资源浏览地址，
     * @return 访问地址
     */
    public String getAccessAddress() {
        return accessAddress;
    }

    public void setAccessAddress(String accessAddress) {
        this.accessAddress = accessAddress;
    }
    /**
     * 获取存放的资源库，服务器存放资源目录，空间名称等
     *
     * @return 资源库，服务器存放资源目录，空间名称等
     */
    public String getDataBase() {
        return workingDirectory;
    }


}
