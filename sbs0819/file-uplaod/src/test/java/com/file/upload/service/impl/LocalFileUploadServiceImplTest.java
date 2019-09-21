package com.file.upload.service.impl;


import org.junit.Test;

import static org.junit.Assert.*;

public class LocalFileUploadServiceImplTest {


    @Test
    public void testLocalUpload() {
        new LocalFileUploadServiceImpl().upload();
    }


}
