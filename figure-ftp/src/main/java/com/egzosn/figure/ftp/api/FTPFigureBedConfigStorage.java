package com.egzosn.figure.ftp.api;

import com.egzosn.figure.common.api.FigureBedConfigStorage;


import static com.egzosn.figure.common.bean.BaseResourceInfo.SEPARATOR;

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
    private int port = 21;
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

    /**
     *  是否为被动模式 true 是，否则主动， 默认false
     */
    private boolean passiveMode;

    /**
     * 超时时间
     */
    private int clientTimeout;
    /**
     * 线程数
     */
    private int threaNum;
    /**
     * 文件传送类型
     * 0=ASCII_FILE_TYPE（ASCII格式） 1=EBCDIC_FILE_TYPE 2=LOCAL_FILE_TYPE（二进制文件）
     */
    private int transferFileType = 2;

    /**
     * 重新连接时间
     */
    private int retryTimes = 1200;
    /**
     * 缓存大小
     */
    private int bufferSize = 1024;



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
        if (null == encoding || "".equals(encoding)){
            return "GBK";
        }
        return encoding;
    }


    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * 是否为被动模式 true 是，否则主动
     *
     * @return 是否为被动模式
     */
    public boolean isPassiveMode() {
        return passiveMode;
    }

    /**
     * 超时时间
     *
     * @return 超时时间
     */
    public int getClientTimeout() {
        return clientTimeout;
    }

    public void setClientTimeout(int clientTimeout) {
        this.clientTimeout = clientTimeout;
    }

    public void setPassiveMode(boolean passiveMode) {
        this.passiveMode = passiveMode;
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
        if (null == workingDirectory){
            return "";
        }
        if ( !workingDirectory.endsWith(SEPARATOR)){
            workingDirectory +=  SEPARATOR;
        }

        if ( !workingDirectory.startsWith(SEPARATOR)){
            workingDirectory = SEPARATOR + workingDirectory ;
        }

        return workingDirectory;
    }

    public int getThreaNum() {
        return threaNum;
    }

    public void setThreaNum(int threaNum) {
        this.threaNum = threaNum;
    }

    public int getTransferFileType() {
        return transferFileType;
    }

    public void setTransferFileType(int transferFileType) {
        this.transferFileType = transferFileType;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FTPFigureBedConfigStorage{");
        sb.append("hostname='").append(hostname).append('\'');
        sb.append(", port=").append(port);
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", encoding='").append(encoding).append('\'');
        sb.append(", workingDirectory='").append(workingDirectory).append('\'');
        sb.append(", accessAddress='").append(accessAddress).append('\'');
        sb.append(", passiveMode=").append(passiveMode);
        sb.append(", clientTimeout=").append(clientTimeout);
        sb.append(", threaNum=").append(threaNum);
        sb.append(", transferFileType=").append(transferFileType);
        sb.append(", retryTimes=").append(retryTimes);
        sb.append(", bufferSize=").append(bufferSize);
        sb.append('}');
        return sb.toString();
    }
}
