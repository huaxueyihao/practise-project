package com.spring.source.code.study.NamespaceHandler;

public class Demo {

    private String demoName;

    private String demoDesc;


    public String getDemoName() {
        return demoName;
    }

    public void setDemoName(String demoName) {
        this.demoName = demoName;
    }

    public String getDemoDesc() {
        return demoDesc;
    }

    public void setDemoDesc(String demoDesc) {
        this.demoDesc = demoDesc;
    }

    @Override
    public String toString() {
        return "Demo{" +
                "demoName='" + demoName + '\'' +
                ", demoDesc='" + demoDesc + '\'' +
                '}';
    }
}
