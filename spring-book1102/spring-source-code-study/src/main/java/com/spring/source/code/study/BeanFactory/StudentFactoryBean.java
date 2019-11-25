package com.spring.source.code.study.BeanFactory;

import org.springframework.beans.factory.FactoryBean;

public class StudentFactoryBean implements FactoryBean<Student> {

    private String student;

    public Student getObject() throws Exception {
        Student stu = new Student();
        String[] split = student.split(",");
        stu.setName(split[0]);
        stu.setAge(Integer.valueOf(split[1]));
        return stu;
    }

    public Class<?> getObjectType() {
        return Student.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }
}
