package com.egzosn.figure.ftp.api;

import com.egzosn.figure.common.api.FigureBedConfigStorage;
import com.egzosn.figure.common.api.FigureBedService;
import com.egzosn.figure.common.bean.BaseResourceInfo;
import com.egzosn.figure.common.bean.ProcessingResults;
import com.egzosn.figure.common.bean.ResourceInfo;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

/**
 *  ftp服务
 * @author egan
 *         email egzosn@gmail.com
 *         date 2018/8/20.20:19
 */
public class FTPFigureBedService implements FigureBedService {

    /**
     * 配置信息
     */
    private FigureBedConfigStorage configStorage;
    /**
     * 暂时使用一个服务一个客户端形式，后期用连接池
     */
    private FTPClient ftpClient;

    public FTPFigureBedService(FigureBedConfigStorage configStorage) {
        setConfigStorage(configStorage);
    }

    public FTPFigureBedService() {

    }

    public FigureBedConfigStorage getConfigStorage() {
        return configStorage;
    }

    public void setConfigStorage(FigureBedConfigStorage configStorage) {
        this.configStorage = configStorage;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(configStorage.getHostname(), configStorage.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 授权
     */
    public void auth() {

    }

    /**
     * 存储
     *
     * @param body 资源信息
     * @return 处理结果
     */
    public ProcessingResults store(ResourceInfo body) {
        return null;
    }

    /**
     * 文件是否存在
     *
     * @param body 资源信息
     * @return 处理结果
     */
    public boolean exists(BaseResourceInfo body) {
        return false;
    }

    /**
     * 获取资源文件
     *
     * @param body
     * @return 资源文件
     */
    public ResourceInfo retrieveResource(BaseResourceInfo body) {
        return null;
    }
}
