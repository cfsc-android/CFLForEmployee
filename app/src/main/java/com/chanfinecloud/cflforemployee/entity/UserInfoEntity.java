package com.chanfinecloud.cflforemployee.entity;

import java.io.Serializable;

/**
 * Created by Loong on 2020/3/21.
 * Version: 1.0
 * Describe:
 */
public class UserInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * birthday : 1999-12-31T16:00:00.000+0000
     * roles :
     * description :
     * type : BACKEN
     * lastLoginIp :
     * workNo : 002323
     * password : $2a$10$Veft7gw9/6DO/zVD./7Wn.PBf9UjslozXNrlDA4nAjwBTOyJX01SG
     * avatarId : 170f1eee10c773006abae234fcf947cb
     * post :
     * updateBy : a75d45a015c44384a04449ee80dc3503
     * id : a75d45a015c44384a04449ee80dc3503
     * department : 工程部
     * email : 13787176775@139.com
     * salt : 5FMD48RM
     * oldPassword :
     * roleId :
     * sex : 0
     * newPassword :
     * updateTime : 2020-03-02T03:19:03.000+0000
     * loginCount : 0
     * lastLoginTime :
     * realName : 超级管理员
     * createBy : admin
     * createTime : 2019-11-20T10:07:27.000+0000
     * phone : 13787176775
     * status : 0
     * username : admin
     */

    private String birthday;
    private String roles;
    private String description;
    private String type;
    private String lastLoginIp;
    private String workNo;
    private String password;
    private String avatarId;
    private String post;
    private String updateBy;
    private String id;
    private String department;
    private String email;
    private String salt;
    private String oldPassword;
    private String roleId;
    private int gender;
    private String newPassword;
    private String updateTime;
    private int loginCount;
    private String lastLoginTime;
    private String realName;
    private String createBy;
    private String createTime;
    private String phone;
    private int status;
    private String username;

    private ResourceEntity avatarResuorce;

    public ResourceEntity getAvatarResuorce() {
        return avatarResuorce;
    }

    public void setAvatarResuorce(ResourceEntity avatarResuorce) {
        this.avatarResuorce = avatarResuorce;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
