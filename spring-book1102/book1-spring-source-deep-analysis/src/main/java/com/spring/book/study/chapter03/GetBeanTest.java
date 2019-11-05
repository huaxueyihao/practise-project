package com.spring.book.study.chapter03;

// 创建调用方法
public abstract class GetBeanTest {

    public void showMe() {
        this.getBean().showMe();
    }

    public abstract User getBean();

}
