package com.egzosn.figure.common.api;

/**
 *  仓储账户信息
 * @author egan
 *         email egzosn@gmail.com
 *         date 2018/8/20.19:24
 */
public interface FigureBedConfigStorage {
    /**
     * 获取用户名
     * @return 用户名
     */
    String getUserName();
    /**
     * 获取密钥
     * @return 密钥
     */
    String getSecretKey();

    /**
     * 访问地址，一般为资源浏览地址，
     * @return 访问地址
     */
    String getAccessAddress();

    /**
     * 获取存放的资源库，服务器存放资源目录，空间名称等
     * @return 资源库，服务器存放资源目录，空间名称等
     */
    String getDataBase();

    /**
     * 获取主机地址
     * @return 主机地址
     */
    String getHostname();

    /**
     *  获取主机端口
     * @return 端口
     */
    int getPort();

    /**
     * 编码格式
     * @return 编码格式
     */
    String getEncoding();
}
