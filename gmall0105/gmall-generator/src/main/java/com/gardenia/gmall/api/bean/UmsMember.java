package com.gardenia.gmall.api.bean;

import java.util.Date;
import javax.persistence.*;

@Table(name = "ums_member")
public class UmsMember {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户等级
     */
    @Column(name = "member_level_id")
    private Long memberLevelId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机
     */
    private String phone;

    /**
     * 头像
     */
    private String header;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别0-男1-女2-位置
     */
    private Boolean gender;

    /**
     * 生日
     */
    private Date birth;

    /**
     * 城市
     */
    private String city;

    /**
     * 职业
     */
    private String job;

    /**
     * 个性签名
     */
    private String sign;

    /**
     * 状态0-启用-1禁用
     */
    private Boolean status;

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
     * 获取客户等级
     *
     * @return member_level_id - 客户等级
     */
    public Long getMemberLevelId() {
        return memberLevelId;
    }

    /**
     * 设置客户等级
     *
     * @param memberLevelId 客户等级
     */
    public void setMemberLevelId(Long memberLevelId) {
        this.memberLevelId = memberLevelId;
    }

    /**
     * 获取用户名称
     *
     * @return username - 用户名称
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名称
     *
     * @param username 用户名称
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取昵称
     *
     * @return nickname - 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置昵称
     *
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取手机
     *
     * @return phone - 手机
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机
     *
     * @param phone 手机
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取头像
     *
     * @return header - 头像
     */
    public String getHeader() {
        return header;
    }

    /**
     * 设置头像
     *
     * @param header 头像
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取性别0-男1-女2-位置
     *
     * @return gender - 性别0-男1-女2-位置
     */
    public Boolean getGender() {
        return gender;
    }

    /**
     * 设置性别0-男1-女2-位置
     *
     * @param gender 性别0-男1-女2-位置
     */
    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    /**
     * 获取生日
     *
     * @return birth - 生日
     */
    public Date getBirth() {
        return birth;
    }

    /**
     * 设置生日
     *
     * @param birth 生日
     */
    public void setBirth(Date birth) {
        this.birth = birth;
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
     * 获取职业
     *
     * @return job - 职业
     */
    public String getJob() {
        return job;
    }

    /**
     * 设置职业
     *
     * @param job 职业
     */
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * 获取个性签名
     *
     * @return sign - 个性签名
     */
    public String getSign() {
        return sign;
    }

    /**
     * 设置个性签名
     *
     * @param sign 个性签名
     */
    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * 获取状态0-启用-1禁用
     *
     * @return status - 状态0-启用-1禁用
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置状态0-启用-1禁用
     *
     * @param status 状态0-启用-1禁用
     */
    public void setStatus(Boolean status) {
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