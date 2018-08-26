package com.egzosn.figure.ftp.api;

import com.egzosn.figure.common.api.FigureBedConfigStorage;
import com.egzosn.figure.common.api.FigureBedService;
import com.egzosn.figure.common.bean.BaseResourceInfo;
import com.egzosn.figure.common.bean.ProcessingCode;
import com.egzosn.figure.common.bean.ProcessingResults;
import com.egzosn.figure.common.bean.ResourceInfo;
import com.egzosn.figure.common.exception.FigureException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static com.egzosn.figure.common.bean.BaseResourceInfo.SEPARATOR;
import static com.egzosn.figure.common.bean.ProcessingCode.*;

/**
 *  ftp服务
 * @author egan
 *         email egzosn@gmail.com
 *         date 2018/8/20.20:19
 */
public class FTPFigureBedService implements FigureBedService {

    private final  static Logger logger =  LoggerFactory.getLogger(FTPFigureBedService.class);;
    /**
     * 配置信息
     */
    private FigureBedConfigStorage configStorage;
    /**
     * 暂时使用一个服务一个客户端形式，后期用连接池
     */
    private FTPClient ftpClient;

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    //    private String

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
        logger.debug(configStorage.toString());
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(configStorage.getHostname(), configStorage.getPort());
            if (SUCCESS.getCode() != auth().getCode()) {
                throw new FigureException(SERVER_ERROR.getCode(), "ftp 远程连接登录异常：账号或密码错误");
            }

            String workingDirectory = ftpClient.printWorkingDirectory();
            if (!configStorage.getDataBase().startsWith(workingDirectory)){
                ((FTPFigureBedConfigStorage)configStorage).setWorkingDirectory(workingDirectory + configStorage.getDataBase() );
            }
            ftpClient.setControlEncoding(configStorage.getEncoding());
            if (!configStorage.isPassiveMode()){
                ftpClient.enterLocalPassiveMode();
            }

        } catch (IOException e) {
            throw new FigureException(SERVER_ERROR.getCode(), String.format("ftp 远程连接异常：%s", e.getMessage()), e);
        }

    }



    /**
     * 授权
     */
    public ProcessingResults auth() {
        try {
            ProcessingCode processingCode = ftpClient.login(configStorage.getUserName(), configStorage.getSecretKey()) ? SUCCESS : UNAUTH;
         return new ProcessingResults(processingCode.getCode());
        } catch (IOException e) {
            throw new FigureException(UNAUTH.getCode(), String.format("ftp 登录异常：%s", e.getMessage()), e);
        }
    }

    /**
     * 存储
     *
     * @param body 资源信息
     * @return 处理结果
     */
    public ProcessingResults store(ResourceInfo body) {

        String pathname = configStorage.getDataBase() + body.getPath();
        createDirectory(pathname);
        ftpClient.setDataTimeout(24000);//设置超时时间
        String remote = null;
        try {
            remote = new String(body.getFileName().getBytes(configStorage.getEncoding()), "iso-8859-1");
            if (ftpClient.storeFile(remote, body.getFile())) {
                logger.debug(body.getFileName() + "上传文件成功！");
                return ProcessingResults.SUCCESS;
            }
            logger.debug(body.pathname() + "上传文件失败！");
            return ProcessingResults.ERROR(body.pathname() + "上传文件失败！");
        } catch (IOException e) {
            logger.error(body.pathname() + "上传文件失败！", e);
        }


        return ProcessingResults.ERROR(body.pathname() + "上传文件失败！");
    }

    /**
     * 列出服务器上文件和目录
     *
     * @param regPathname --匹配的正则表达式
     * @return 匹配成功的文件列表， 未匹配成功则返回空数组
     *
     * <code>
     *       FTPFigureBedConfigStorage configStorage = new FTPFigureBedConfigStorage();
     *       configStorage.setHostname("10.45.44..");
     *       configStorage.setPort(21);
     *       configStorage.setUserName("UserName");
     *       configStorage.setPassword("Password");
     *       FTPFigureBedService service = new FTPFigureBedService(configStorage);
     *       service.auth();
     *       //指定文件名获取，这里返回 files[] 长度为1（遵循系统文件名不重复）
     *       String files[] = service.listRemoteFiles("data/images/350000/201/35020301000061/35020301000061_20180514143733.png");
     *       //指定文件名或目录前缀， 这里返回 files[] 长度多个
     *       files[] = service.listRemoteFiles("data/images/350000/201/35020301000061/35020301000061_2018051");
     * </code>
     *
     */
    public String[] listRemoteFiles(String regPathname) {
        String files[] = null;
        try {
            files = ftpClient.listNames(regPathname.replace("//", SEPARATOR));
        } catch (IOException e) {
            throw new FigureException(ProcessingCode.SERVER_ERROR.getCode(), "获取FTP上文件目录失败：" + regPathname, e);
        }
        if (files == null || files.length == 0) {
            logger.debug("没有任何文件!");
        }
        return files;

    }
    /**
     * 文件是否存在
     *
     * @param body 资源信息
     * @return 处理结果
     * <code>
     *       FTPFigureBedConfigStorage configStorage = new FTPFigureBedConfigStorage();
     *       configStorage.setHostname("10.45.44..");
     *       configStorage.setPort(21);
     *       configStorage.setUserName("UserName");
     *       configStorage.setPassword("Password");
     *       FTPFigureBedService service = new FTPFigureBedService(configStorage);
     *       service.auth();
     *       BaseResourceInfo resource = new BaseResourceInfo();
     *       resource.setPath("data/images/350000/201/35020301000061");
     *       resource.setFileName("35020301000061_20180514145859.jpg");
     *       //返回true则为存在
     *       boolean flag = service.exists(resource);
     * </code>
     *
     */
    public boolean exists(BaseResourceInfo body) {
        return  listRemoteFiles(configStorage.getDataBase() + body.pathname()).length != 0;

    }


    /**
     * 进入到服务器的某个目录下
     *
     * @param directory
     */
    public boolean changeWorkingDirectory(String directory) {
        try {

           return ftpClient.changeWorkingDirectory(directory);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return false;
    }

    /**
     * 返回到上一层目录
     */
    public void changeToParentDirectory() {
        try {
            ftpClient.changeToParentDirectory();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    /**
     * 在服务器上创建一个文件夹
     *
     * @param dir 文件夹名称，不能含有特殊字符，如 \ 、/ 、: 、* 、?、 "、 <、>...
     */
    public boolean makeDirectory(String dir) {
        boolean flag = true;
        try {
            flag = ftpClient.makeDirectory(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 递归创建远程服务器目录
     *
     * @param remote
     *            远程服务器文件绝对路径
     *
     * @return 目录创建是否成功
     * @throws IOException
     */
    public boolean createDirectory(String remote){
        logger.debug("创建或切换工作目录["+remote+"]");

        boolean success = true;
        String directory = remote.substring(0, remote.lastIndexOf(SEPARATOR) + 1);
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase(SEPARATOR)&& !changeWorkingDirectory(directory)) {
            if (!makeDirectory(directory)){
                logger.debug("创建目录["+directory+"]失败");
            }
            changeWorkingDirectory(directory);

        }
        return success;
    }


    /**
     * 获取资源文件
     *
     * @param body
     * @return 资源文件
     */
    public ProcessingResults retrieveResource(BaseResourceInfo body) {
//        ProcessingResults processingResults = auth();
//        if (SUCCESS.getCode() != processingResults.getCode()) {
//            return processingResults;
//        }

        // 设置文件类型为二进制，与ASCII有区别
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 设置编码格式
        ftpClient.setControlEncoding(configStorage.getEncoding());
        if (!changeWorkingDirectory(configStorage.getDataBase() + body.getPath())){
            throw new FigureException(ProcessingCode.SERVER_ERROR.getCode(), "未找到工作目录");
        }
        if (!exists(body)){
            return ProcessingResults.ERROR("未找到对应文件");
        }

        try {
            InputStream is = ftpClient.retrieveFileStream(new String(body.getFileName().getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING));
            ftpClient.completePendingCommand();
            ProcessingResults processingResults = ProcessingResults.SUCCESS;
            processingResults.setData(is);
            return processingResults;
        } catch (IOException e) {
          throw new FigureException(ProcessingCode.SERVER_ERROR.getCode(), "文件获取异常", e);
        }
    }

    /**
     * 删除
     *
     * @param body 资源信息
     * @return 处理结果
     */
    public ProcessingResults delete(BaseResourceInfo body) {
            String pathname = String.format("%s/%s", body.getPath(), body.getFileName());
        try {
            if (ftpClient.deleteFile(pathname)){
                logger.debug("删除文件"+pathname+"成功！");
                return ProcessingResults.SUCCESS;
            }
            return ProcessingResults.ERROR("远程文件删除失败: " + pathname);
        } catch (IOException e) {
            throw new FigureException(SERVER_ERROR.getCode(), "远程文件删除失败: " + pathname, e);
        }

    }


}
