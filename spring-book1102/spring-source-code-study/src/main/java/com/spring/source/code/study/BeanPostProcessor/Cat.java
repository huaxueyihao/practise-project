package com.spring.source.code.study.BeanPostProcessor;

import org.springframework.beans.factory.InitializingBean;

public class Cat implements InitializingBean {

    private String color;

    private String kind;


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "color='" + color + '\'' +
                ", kind='" + kind + '\'' +
                '}';
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean.afterPropertiesSet before=" + toString());
        this.kind = "bigFaceCat";
        this.color = "black";
        System.out.println("InitializingBean.afterPropertiesSet after=" + toString());
    }

    public void customInitMethod(){
        System.out.println("bean element customInitMethod before=" + toString());
        this.kind = "littleCatCat";
        this.color = "green";
        System.out.println("bean element customInitMethod after=" + toString());
    }





}
