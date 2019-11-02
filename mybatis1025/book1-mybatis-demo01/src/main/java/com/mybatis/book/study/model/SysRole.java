package com.mybatis.book.study.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SysRole implements Serializable {

    private static final long serialVersionUID = -257709890466747509L;
    private Long id;
    private String roleName;
//    private Integer enabled;
    private Enabled enabled;
    private Long createBy;
    private Date createTime;

    private SysUser user;

    private List<SysPrivilege> privilegeList;


    private CreateInfo createInfo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Enabled getEnabled() {
        return enabled;
    }

    public void setEnabled(Enabled enabled) {
        this.enabled = enabled;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    public List<SysPrivilege> getPrivilegeList() {
        return privilegeList;
    }

    public void setPrivilegeList(List<SysPrivilege> privilegeList) {
        this.privilegeList = privilegeList;
    }

    public CreateInfo getCreateInfo() {
        return createInfo;
    }

    public void setCreateInfo(CreateInfo createInfo) {
        this.createInfo = createInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysRole role = (SysRole) o;
        return Objects.equals(id, role.id) &&
                Objects.equals(roleName, role.roleName) &&
                enabled == role.enabled &&
                Objects.equals(createBy, role.createBy) &&
                Objects.equals(createTime, role.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName, enabled, createBy, createTime);
    }
}
