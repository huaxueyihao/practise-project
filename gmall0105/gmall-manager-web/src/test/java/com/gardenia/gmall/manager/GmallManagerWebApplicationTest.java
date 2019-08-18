package com.gardenia.gmall.manager;


import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class GmallManagerWebApplicationTest {


    @Test
    public void contextLoads() throws IOException, MyException {
        // 获得配置文件路径
        String tracker = GmallManagerWebApplicationTest.class.getResource("/tracker.conf").getPath();

        ClientGlobal.init(tracker);
        TrackerClient trackerClient = new TrackerClient();

        TrackerServer trackerServer = trackerClient.getConnection();


        StorageClient storageClient = new StorageClient(trackerServer, null);

        String[] jpgs = storageClient.upload_file("/Users/amao/Documents/picture/common/21.jpg", "jpg", null);

        System.out.println("hehhehhehhe");
        for (String jpg : jpgs) {
            System.out.println(jpg);
        }


    }


}
