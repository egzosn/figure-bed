package com.egzosn.figure.common.api;

import com.egzosn.figure.common.bean.BaseResourceInfo;
import com.egzosn.figure.common.bean.ProcessingResults;
import com.egzosn.figure.common.bean.ResourceInfo;

import java.io.InputStream;


/**
 * 图床基础服务
 * @author egan
 *         email egzosn@gmail.com
 *         date 2018/8/17.16:43
 */
public interface FigureBedService {

    /**
     * 设置账户相关配置信息
     * @param configStorage 配置信息
     */
    void setConfigStorage(FigureBedConfigStorage configStorage);

    /**
     * 授权
     */
    void auth();

    /**
     * 存储
     * @param body 资源信息
     * @return 处理结果
     */
    ProcessingResults store(ResourceInfo body);

    /**
     * 文件是否存在
     * @param body 资源信息
     * @return 处理结果
     */
    boolean exists(BaseResourceInfo body);

    /**
     * 获取资源文件
     * @return 资源文件
     */
    ResourceInfo retrieveResource(BaseResourceInfo body);

}
