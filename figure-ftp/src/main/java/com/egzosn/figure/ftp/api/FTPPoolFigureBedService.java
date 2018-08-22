package com.egzosn.figure.ftp.api;

import com.egzosn.figure.common.api.FigureBedConfigStorage;
import com.egzosn.figure.common.api.FigureBedService;
import com.egzosn.figure.common.bean.BaseResourceInfo;
import com.egzosn.figure.common.bean.ProcessingResults;
import com.egzosn.figure.common.bean.ResourceInfo;
import com.egzosn.figure.common.exception.FigureException;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ftp连接池服务
 *
 * @author egan
 *         email egzosn@gmail.com
 *         date 2018/8/20.20:19
 */
public class FTPPoolFigureBedService implements FigureBedService {

    private final static Logger logger = LoggerFactory.getLogger(FTPPoolFigureBedService.class);

    private final GenericObjectPool<FTPFigureBedService> pool;
    /**
     * 用于存放当前线程使用同一的连接
     */
    private static final ThreadLocal<FTPFigureBedService> THREAD_LOCAL = new ThreadLocal<FTPFigureBedService>();




    public FTPPoolFigureBedService(GenericObjectPoolConfig poolConfig, FigureBedConfigStorage configStorage) {
        // 初始化对象池
        pool = new GenericObjectPool<FTPFigureBedService>(new FTPPoolFigureBedFactory(configStorage), poolConfig);
    }



    /**
     * properties 文件中获取
     * @param properties properties
     */
    public FTPPoolFigureBedService(InputStream properties) {
        Properties pro = new Properties();
        try {
            pro.load(properties);
        } catch (IOException e) {
            throw new FigureException(500, "ftp连接池服务初始化失败,加载配置文件出错", e);
        }finally {
            if (null != properties){
                try {
                    properties.close();
                } catch (IOException e) {
                    throw new FigureException(500, "ftp连接池服务初始化失败,InputStream关闭流失败", e);
                }
            }
        }
        // 初始化对象池
        pool = new GenericObjectPool<FTPFigureBedService>(new FTPPoolFigureBedFactory(initFigureBedConfigStorage(pro)), initPoolConfig(pro));
    }


    /**
     * 通过属性文件进行初始化
     * @param pro 属性文件
     * @return GenericObjectPoolConfig
     */
    public GenericObjectPoolConfig initPoolConfig(Properties pro){

        // 初始化对象池配置
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setBlockWhenExhausted(Boolean.parseBoolean(pro.getProperty("blockWhenExhausted")));
        poolConfig.setMaxWaitMillis(Long.parseLong(pro.getProperty("maxWait")));
        poolConfig.setMinIdle(Integer.parseInt(pro.getProperty("minIdle")));
        poolConfig.setMaxIdle(Integer.parseInt(pro.getProperty("maxIdle")));
        poolConfig.setMaxTotal(Integer.parseInt(pro.getProperty("maxTotal")));
        poolConfig.setTestOnBorrow(Boolean.parseBoolean(pro.getProperty("testOnBorrow")));
        poolConfig.setTestOnReturn(Boolean.parseBoolean(pro.getProperty("testOnReturn")));
        poolConfig.setTestOnCreate(Boolean.parseBoolean(pro.getProperty("testOnCreate")));
        poolConfig.setTestWhileIdle(Boolean.parseBoolean(pro.getProperty("testWhileIdle")));
        poolConfig.setLifo(Boolean.parseBoolean(pro.getProperty("lifo")));
        logger.debug("连接池配置：" + poolConfig.toString());
        return poolConfig;
    }
    /**
     * 通过属性文件进行初始化
     * @param pro 属性文件
     * @return FTPFigureBedConfigStorage
     */
    public FTPFigureBedConfigStorage initFigureBedConfigStorage(Properties pro){
        FTPFigureBedConfigStorage configStorage=new FTPFigureBedConfigStorage();
        configStorage.setHostname(pro.getProperty("hostname"));
        configStorage.setPort(Integer.parseInt(pro.getProperty("port")));
        configStorage.setUserName(pro.getProperty("userName"));
        configStorage.setPassword(pro.getProperty("pasword"));
        configStorage.setClientTimeout(Integer.parseInt(pro.getProperty("clientTimeout")));
        configStorage.setEncoding(pro.getProperty("encoding"));
        configStorage.setWorkingDirectory(pro.getProperty("workingDirectory"));
        configStorage.setPassiveMode(Boolean.parseBoolean(pro.getProperty("passiveMode")));
        configStorage.setRetryTimes(Integer.parseInt(pro.getProperty("retryTimes")));
        configStorage.setTransferFileType(Integer.parseInt(pro.getProperty("transferFileType")));
        configStorage.setBufferSize(Integer.parseInt(pro.getProperty("bufferSize")));
        return configStorage;
    }


    public void setConfigStorage(FigureBedConfigStorage configStorage) {

    }

    private FTPFigureBedService getFigureBedService(){
        FTPFigureBedService service = THREAD_LOCAL.get();
        if (null != service){
            return service;
        }
        try {
            service = pool.borrowObject();
        } catch (Exception e) {
            throw new FigureException(500, "获取FTP服务失败", e);
        }
        THREAD_LOCAL.set(service);

        return service;
    }

    /**
     * 授权
     */
    public ProcessingResults auth() {
        return getFigureBedService().auth();
    }

    /**
     * 存储
     *
     * @param body 资源信息
     * @return 处理结果
     */
    public ProcessingResults store(ResourceInfo body) {
        return getFigureBedService().store(body);
    }



    /**
     * 文件是否存在
     *
     * @param body 资源信息
     * @return 处理结果
     * <code>
     * BaseResourceInfo resource = new BaseResourceInfo();
     * resource.setPath("data/images/350000/201/35020301000061");
     * resource.setFileName("35020301000061_20180514145859.jpg");
     * //返回true则为存在
     * boolean flag = service.exists(resource);
     * </code>
     */
    public boolean exists(BaseResourceInfo body) {
        return getFigureBedService().exists(body);

    }


    /**
     * 获取资源文件
     *
     * @param body
     * @return 资源文件
     */
    public ProcessingResults retrieveResource(BaseResourceInfo body) {
        return getFigureBedService().retrieveResource(body);
    }

    /**
     * 删除
     *
     * @param body 资源信息
     * @return 处理结果
     */
    public ProcessingResults delete(BaseResourceInfo body) {
        return getFigureBedService().delete(body);
    }


}
