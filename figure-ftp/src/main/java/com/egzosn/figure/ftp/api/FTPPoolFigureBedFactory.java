package com.egzosn.figure.ftp.api;

import com.egzosn.figure.common.api.FigureBedConfigStorage;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import java.io.*;

/**
 *  ftp连接池工厂
 * @author egan
 *         email egzosn@gmail.com
 *         date 2018/8/20.20:19
 */
public class FTPPoolFigureBedFactory extends BasePooledObjectFactory<FTPFigureBedService>  {

    /**
     * 配置信息
     */
    private FigureBedConfigStorage configStorage;

    public FTPPoolFigureBedFactory(FigureBedConfigStorage configStorage) {
        this.configStorage = configStorage;
    }


    /**
     * Creates an object instance, to be wrapped in a {@link PooledObject}.
     * <p>This method <strong>must</strong> support concurrent, multi-threaded
     * activation.</p>
     *
     * @return an instance to be served by the pool
     * @throws Exception if there is a problem creating a new instance,
     *                   this will be propagated to the code requesting an object.
     */

    public FTPFigureBedService create() throws Exception {
        return new FTPFigureBedService(configStorage);
    }



    /**
     * Wrap the provided instance with an implementation of
     * {@link PooledObject}.
     *
     * @param service the instance to wrap
     * @return The provided instance, wrapped by a {@link PooledObject}
     */
    public PooledObject<FTPFigureBedService> wrap(FTPFigureBedService service) {
        return new DefaultPooledObject<FTPFigureBedService>(service);
    }

    /**
     * 销毁对象
     * @param p 连接池内的对象
     */
    @Override
    public void destroyObject(PooledObject<FTPFigureBedService> p) throws Exception {
        FTPFigureBedService service = p.getObject();
        service.getFtpClient().logout();
        super.destroyObject(p);
    }

    /**
     *  校验对象
     * @param p 连接池内的对象
     * @return 是否
     */
    @Override
    public boolean validateObject(PooledObject<FTPFigureBedService> p) {
        FTPFigureBedService service = p.getObject();
        FTPClient ftpClient = service.getFtpClient();
        boolean connect = false;
        try {
            connect = ftpClient.sendNoOp();
            if(connect){
                ftpClient.changeWorkingDirectory(configStorage.getDataBase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connect;
    }


}
