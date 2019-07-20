package com.gardenia.gmall.api.bean;

import javax.persistence.*;

@Table(name = "ums_member_receive_address")
public class UmsMemberReceiveAddress {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户id
     */
    @Column(name = "member_id")
    private Long memberId;

    /**
     * 收货人名称
     */
    private String name;

    /**
     * 电环
     */
    private String phone;

    /**
     * 邮政编码
     */
    @Column(name = "post_code")
    private String postCode;

    /**
     * 省份/直辖市
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 详细地址
     */
    @Column(name = "detail_address")
    private String detailAddress;

    /**
     * 省市区代码
     */
    @Column(name = "area_code")
    private String areaCode;

    /**
     * 是否默认0-否，1-是
     */
    @Column(name = "default_status")
    private Short defaultStatus;

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
     * 获取客户id
     *
     * @return member_id - 客户id
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * 设置客户id
     *
     * @param memberId 客户id
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取收货人名称
     *
     * @return name - 收货人名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置收货人名称
     *
     * @param name 收货人名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取电环
     *
     * @return phone - 电环
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置电环
     *
     * @param phone 电环
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取邮政编码
     *
     * @return post_code - 邮政编码
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * 设置邮政编码
     *
     * @param postCode 邮政编码
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * 获取省份/直辖市
     *
     * @return province - 省份/直辖市
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省份/直辖市
     *
     * @param province 省份/直辖市
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取城市
     *
     * @return city - 城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置城市
     *
     * @param city 城市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取详细地址
     *
     * @return detail_address - 详细地址
     */
    public String getDetailAddress() {
        return detailAddress;
    }

    /**
     * 设置详细地址
     *
     * @param detailAddress 详细地址
     */
    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    /**
     * 获取省市区代码
     *
     * @return area_code - 省市区代码
     */
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * 设置省市区代码
     *
     * @param areaCode 省市区代码
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * 获取是否默认0-否，1-是
     *
     * @return default_status - 是否默认0-否，1-是
     */
    public Short getDefaultStatus() {
        return defaultStatus;
    }

    /**
     * 设置是否默认0-否，1-是
     *
     * @param defaultStatus 是否默认0-否，1-是
     */
    public void setDefaultStatus(Short defaultStatus) {
        this.defaultStatus = defaultStatus;
    }
}
