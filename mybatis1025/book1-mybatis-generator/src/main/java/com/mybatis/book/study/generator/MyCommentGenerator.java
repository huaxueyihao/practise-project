package com.mybatis.book.study.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Properties;

public class MyCommentGenerator extends DefaultCommentGenerator {

    private boolean suppressAllComments;

    private boolean addRemarkComments;

    @Override
    public void addConfigurationProperties(Properties properties) {
        super.addConfigurationProperties(properties);
        suppressAllComments = StringUtility.isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));
        addRemarkComments = StringUtility.isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_ADD_REMARK_COMMENTS));
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
//        super.addFieldComment(field, introspectedTable);
        // 如果阻止生成所有注释，直接返回
        if (suppressAllComments) {
            return;
        }
        // 文档注释开始
        field.addJavaDocLine("/**");
        // 获取数据库字段的备注信息
        String remarks = introspectedColumn.getRemarks();
        // 根据参数和备注信息判断是否添加备注信息
        if (addRemarkComments && StringUtility.stringHasValue(remarks)) {
            String[] remarkLines = remarks.split(System.getProperty("line. separator "));
            for (String remarkLine : remarkLines) {
                field.addJavaDocLine("*" + remarkLine);
            }
        }

        // 由于Java对象名和数据库字段名可能不一样，注释中保留数据库字段名
        field.addJavaDocLine("*" + introspectedColumn.getActualColumnName());
        field.addJavaDocLine(" */");
    }
}
