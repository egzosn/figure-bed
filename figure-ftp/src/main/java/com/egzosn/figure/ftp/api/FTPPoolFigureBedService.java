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


    /**
     *
     * @param poolConfig 连接池配置
     * @param configStorage FTP账户配置
     */
    public FTPPoolFigureBedService(GenericObjectPoolConfig poolConfig, FigureBedConfigStorage configStorage) {
        // 初始化对象池
        pool = new GenericObjectPool<FTPFigureBedService>(new FTPPoolFigureBedFactory(configStorage), poolConfig);
    }


    /**
     * properties 文件中获取
     * @param poolConfig 连接池配置
     * @param configStorage FTP账户配置
     */
    public FTPPoolFigureBedService(InputStream poolConfig, FigureBedConfigStorage configStorage) {
        this( initPoolConfig(getProperties(poolConfig)), configStorage);
    }
    /**
     * properties 文件中获取
     * @param poolConfig 连接池配置
     * @param configStorage FTP账户配置
     */
    public FTPPoolFigureBedService(GenericObjectPoolConfig poolConfig, InputStream configStorage) {
        this(poolConfig, initFigureBedConfigStorage(getProperties(configStorage)) );
    }



    /**
     * properties 文件中获取
     * @param properties 属性文件
     */
    public FTPPoolFigureBedService(InputStream properties) {
        Properties pro = getProperties(properties);
        // 初始化对象池
        pool = new GenericObjectPool<FTPFigureBedService>(new FTPPoolFigureBedFactory(initFigureBedConfigStorage(pro)), initPoolConfig(pro));
    }

    /**
     * 配置文件转化为属性对象
     * @param properties 属性文件
     * @return 属性对象
     */
    private final static Properties getProperties(InputStream properties){
        Properties pro = new Properties();
        try {
            pro.load(properties);
            return pro;
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
    }

    /**
     * 通过属性文件进行初始化
     * @param pro 属性文件
     * @return GenericObjectPoolConfig
     */
    public final static GenericObjectPoolConfig initPoolConfig(Properties pro){

        // 初始化对象池配置
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        //#最大数
        poolConfig.setMaxTotal(Integer.parseInt(pro.getProperty("maxTotal")));
        //最小空闲
        poolConfig.setMinIdle(Integer.parseInt(pro.getProperty("minIdle")));
        //最大空闲
        poolConfig.setMaxIdle(Integer.parseInt(pro.getProperty("maxIdle")));
        //最大等待时间
        poolConfig.setMaxWaitMillis(Long.parseLong(pro.getProperty("maxWait")));
        //池对象耗尽之后是否阻塞,maxWait<0时一直等待
        poolConfig.setBlockWhenExhausted(Boolean.parseBoolean(pro.getProperty("blockWhenExhausted")));
        //取对象时验证
        poolConfig.setTestOnBorrow(Boolean.parseBoolean(pro.getProperty("testOnBorrow")));
        //回收验证
        poolConfig.setTestOnReturn(Boolean.parseBoolean(pro.getProperty("testOnReturn")));
        //创建时验证
//        poolConfig.setTestOnCreate(Boolean.parseBoolean(pro.getProperty("testOnCreate")));
        //空闲验证
        poolConfig.setTestWhileIdle(Boolean.parseBoolean(pro.getProperty("testWhileIdle")));
        //后进先出
        poolConfig.setLifo(Boolean.parseBoolean(pro.getProperty("lifo")));
        logger.debug("连接池配置：" + poolConfig.toString());
        return poolConfig;
    }
    /**
     * 通过属性文件进行初始化
     * @param pro 属性文件
     * @return FTPFigureBedConfigStorage
     */
    public final static FTPFigureBedConfigStorage initFigureBedConfigStorage(Properties pro){
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
        FTPFigureBedService figureBedService = getFigureBedService();
        ProcessingResults results = figureBedService.store(body);
        pool.returnObject(figureBedService);
        return results;
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
        FTPFigureBedService figureBedService = getFigureBedService();
        boolean flag = figureBedService.exists(body);
        pool.returnObject(figureBedService);
        return flag;

    }


    /**
     * 获取资源文件
     *
     * @param body
     * @return 资源文件
     */
    public ProcessingResults retrieveResource(BaseResourceInfo body) {
        FTPFigureBedService figureBedService = getFigureBedService();

        ProcessingResults results = figureBedService.retrieveResource(body);
        pool.returnObject(figureBedService);
        return results;
    }

    /**
     * 删除
     *
     * @param body 资源信息
     * @return 处理结果
     */
    public ProcessingResults delete(BaseResourceInfo body) {
        FTPFigureBedService figureBedService = getFigureBedService();
        ProcessingResults results = figureBedService.delete(body);
        pool.returnObject(figureBedService);
        return results;
    }


}
