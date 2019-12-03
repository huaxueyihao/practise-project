package com.spring.source.code.study.Aop;

public class SleepHelperThree {


    public void afterSleep() throws Throwable {
        System.out.println("put on cloth。。。。");
    }


    public void beforeSleep() throws Throwable {
        System.out.println("take off cloth。。。。");
    }
}
