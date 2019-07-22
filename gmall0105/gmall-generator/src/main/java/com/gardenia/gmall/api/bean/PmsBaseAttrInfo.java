package com.gardenia.gmall.api.bean;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pms_base_attr_info")
public class PmsBaseAttrInfo {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 属性名称
     */
    @Column(name = "attr_name")
    private String attrName;

    /**
     * 三级级分类id
     */
    @Column(name = "catalog_id")
    private Long catalogId;

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
     * 获取属性名称
     *
     * @return attr_name - 属性名称
     */
    public String getAttrName() {
        return attrName;
    }

    /**
     * 设置属性名称
     *
     * @param attrName 属性名称
     */
    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    /**
     * 获取三级级分类id
     *
     * @return catalog_id - 三级级分类id
     */
    public Long getCatalogId() {
        return catalogId;
    }

    /**
     * 设置三级级分类id
     *
     * @param catalogId 三级级分类id
     */
    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
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