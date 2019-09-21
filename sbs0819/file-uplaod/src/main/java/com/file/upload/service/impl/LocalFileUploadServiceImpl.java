package com.file.upload.service.impl;

import com.file.upload.service.FileUploadService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 单机版：和web服务共享服务器的服务实现
 * 1.需要指定文件上传的路径，这个配置
 * 2.需要原文件的路径
 *
 */
public class LocalFileUploadServiceImpl implements FileUploadService {


    @Override
    public String upload() {
        // 文件原路径
        String filePath = "/picture/common/21.jpg";
        String destinationPath = "picture/common/localtest/test/";

        File desFile = null;
        try {
            File sourceFile = new File(filePath);
            String sourceName = sourceFile.getName();
            String baseName = FilenameUtils.getBaseName(sourceName);
            String extension = FilenameUtils.getExtension(sourceName);
            System.out.println("baseName=" + baseName);
            System.out.println("extension=" + extension);


            LocalDate now = LocalDate.now();
            String currentDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String temp = destinationPath + currentDate + "/";
            desFile = new File(temp);
            if (!desFile.exists()) {
                desFile.mkdirs();
            }
            destinationPath = temp + sourceName;
            desFile = new File(destinationPath);
            if (!desFile.exists()) {
                desFile.createNewFile();
            } else {
                destinationPath =  temp + baseName + System.currentTimeMillis() + FilenameUtils.EXTENSION_SEPARATOR + extension;
                desFile = new File(destinationPath);
                desFile.createNewFile();
            }

            FileUtils.copyFile(sourceFile, desFile);
//            return destinationPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
