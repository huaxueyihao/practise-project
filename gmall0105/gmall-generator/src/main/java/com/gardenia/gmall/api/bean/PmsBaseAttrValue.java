package com.gardenia.gmall.api.bean;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pms_base_attr_value")
public class PmsBaseAttrValue {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 属性值
     */
    @Column(name = "attr_value")
    private String attrValue;

    /**
     * 属性id
     */
    @Column(name = "attr_name_id")
    private Long attrNameId;

    /**
     * 状态0-有效，1-删除
     */
    private Integer status;

    /**
     * 创建日期
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改日期
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 获取主键id
     *
     * @return id - 主键id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键id
     *
     * @param id 主键id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取属性值
     *
     * @return attr_value - 属性值
     */
    public String getAttrValue() {
        return attrValue;
    }

    /**
     * 设置属性值
     *
     * @param attrValue 属性值
     */
    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    /**
     * 获取属性id
     *
     * @return attr_name_id - 属性id
     */
    public Long getAttrNameId() {
        return attrNameId;
    }

    /**
     * 设置属性id
     *
     * @param attrNameId 属性id
     */
    public void setAttrNameId(Long attrNameId) {
        this.attrNameId = attrNameId;
    }

    /**
     * 获取状态0-有效，1-删除
     *
     * @return status - 状态0-有效，1-删除
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态0-有效，1-删除
     *
     * @param status 状态0-有效，1-删除
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取创建日期
     *
     * @return create_time - 创建日期
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建日期
     *
     * @param createTime 创建日期
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改日期
     *
     * @return update_time - 修改日期
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改日期
     *
     * @param updateTime 修改日期
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}