package com.boot.study.util;

import com.boot.study.exeception.BusinessException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class FileUploadUtil {

    /**
     * 上传文件至fdfs服务器
     *
     * @param path    tracker配置文件路径
     * @param bytes   文件字节数组
     * @param extName 扩展名
     * @return
     */
    public static String updoad(String path, byte[] bytes, String extName) {
        try {
            String tracker = FileUploadUtil.class.getResource(path).getPath();
            ClientGlobal.init(tracker);
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            String[] files = storageClient.upload_file(bytes, extName, null);
            return String.join("/", files);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(100, "文件上传异常");
        }
    }
}
