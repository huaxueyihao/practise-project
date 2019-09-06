package com.boot.study.util;

import com.boot.study.model.SysUser;
import org.junit.Test;

public class ValidatorUtilTest {

    @Test
    public void test(){
        SysUser user = SysUser.builder().userName("张").age(12).password("2222").build();
//        ValidatorUtil.validatorProperty(user,"userName");

        ValidatorUtil.validatorProperties(user,"userName","password");
    }
}
