package com.boot.study;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import java.io.IOException;

public class SpringbootLayUIApplicationTest {


    /**
     * fdfs上传文件测试
     *
     *
     * @throws IOException
     * @throws MyException
     */
    @Test
    public void upload() throws IOException, MyException {
        String tracker = SpringbootLayUIApplicationTest.class.getResource("/fdfs/tracker.conf").getPath();

        ClientGlobal.init(tracker);
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        String[] jpgs = storageClient.upload_file("/Users/amao/Documents/picture/common/21.jpg", "jpg", null);

        for (String jpg : jpgs) {
            System.out.println(jpg);
        }
    }

}
