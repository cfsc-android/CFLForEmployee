package com.chanfinecloud.cflforemployee.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Loong on 2020/2/13.
 * Version: 1.0
 * Describe: 用户实体类
 */
public class UserInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * permissions : [{"id":"","permission":"ROLE_ADMIN","name":"","createTime":"","updateTime":"","roleId":"","authIds":""}]
     * resp_code : 200
     * user : {"id":"a75d45a015c44384a04449ee80dc3503","createBy":"admin","updateBy":"admin","createTime":"2019-11-20 18:07:27","updateTime":"2019-11-20 18:07:27","username":"admin","password":"$2a$10$Veft7gw9/6DO/zVD./7Wn.PBf9UjslozXNrlDA4nAjwBTOyJX01SG","salt":"5FMD48RM","realName":"超级管理员","avatarUrl":"","avatarStr":"","phone":"13787176775","email":"13787176775@139.com","birthday":"2010-01-12 00:00:00","sex":1,"status":0,"workNo":"002323","post":"","type":"BACKEN","description":"","lastLoginTime":"","lastLoginIp":"","loginCount":0,"roles":"","roleId":"","oldPassword":"","newPassword":"","sysRoles":[{"id":"1664b92dff13e1575e3a929caa2fa14d","createBy":"admin","updateBy":"admin","createTime":"2019-11-21 02:13:29","updateTime":"2019-11-21 02:13:29","roleName":"管理员","roleCode":"ADMIN","description":""}],"permissions":"","enabled":false,"accountNonLocked":true,"credentialsNonExpired":true,"accountNonExpired":true}
     */

    private String resp_code;
    private UserBean user;
    private List<PermissionsBean> permissions;

    public String getResp_code() {
        return resp_code;
    }

    public void setResp_code(String resp_code) {
        this.resp_code = resp_code;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public List<PermissionsBean> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionsBean> permissions) {
        this.permissions = permissions;
    }

    public static class UserBean implements Serializable{
        private static final long serialVersionUID = 1L;
        /**
         * id : a75d45a015c44384a04449ee80dc3503
         * createBy : admin
         * updateBy : admin
         * createTime : 2019-11-20 18:07:27
         * updateTime : 2019-11-20 18:07:27
         * username : admin
         * password : $2a$10$Veft7gw9/6DO/zVD./7Wn.PBf9UjslozXNrlDA4nAjwBTOyJX01SG
         * salt : 5FMD48RM
         * realName : 超级管理员
         * avatarUrl :
         * avatarStr :
         * phone : 13787176775
         * email : 13787176775@139.com
         * birthday : 2010-01-12 00:00:00
         * sex : 1
         * status : 0
         * workNo : 002323
         * post :
         * type : BACKEN
         * description :
         * lastLoginTime :
         * lastLoginIp :
         * loginCount : 0
         * roles :
         * roleId :
         * oldPassword :
         * newPassword :
         * sysRoles : [{"id":"1664b92dff13e1575e3a929caa2fa14d","createBy":"admin","updateBy":"admin","createTime":"2019-11-21 02:13:29","updateTime":"2019-11-21 02:13:29","roleName":"管理员","roleCode":"ADMIN","description":""}]
         * permissions :
         * enabled : false
         * accountNonLocked : true
         * credentialsNonExpired : true
         * accountNonExpired : true
         * department:
         */

        private String id;
        private String createBy;
        private String updateBy;
        private String createTime;
        private String updateTime;
        private String username;
        private String password;
        private String salt;
        private String realName;
        private String avatarUrl;
        private String avatarStr;
        private String phone;
        private String email;
        private String birthday;


        private int sex;
        private int status;
        private String workNo;
        private String post;
        private String type;
        private String description;
        private String lastLoginTime;
        private String lastLoginIp;
        private int loginCount;
        private String roles;
        private String roleId;
        private String oldPassword;
        private String newPassword;
        private String permissions;
        private boolean enabled;
        private boolean accountNonLocked;
        private boolean credentialsNonExpired;
        private boolean accountNonExpired;
        private List<SysRolesBean> sysRoles;

        private String department;

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getAvatarStr() {
            return avatarStr;
        }

        public void setAvatarStr(String avatarStr) {
            this.avatarStr = avatarStr;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getWorkNo() {
            return workNo;
        }

        public void setWorkNo(String workNo) {
            this.workNo = workNo;
        }

        public String getPost() {
            return post;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLastLoginTime() {
            return lastLoginTime;
        }

        public void setLastLoginTime(String lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
        }

        public String getLastLoginIp() {
            return lastLoginIp;
        }

        public void setLastLoginIp(String lastLoginIp) {
            this.lastLoginIp = lastLoginIp;
        }

        public int getLoginCount() {
            return loginCount;
        }

        public void setLoginCount(int loginCount) {
            this.loginCount = loginCount;
        }

        public String getRoles() {
            return roles;
        }

        public void setRoles(String roles) {
            this.roles = roles;
        }

        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }

        public String getPermissions() {
            return permissions;
        }

        public void setPermissions(String permissions) {
            this.permissions = permissions;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isAccountNonLocked() {
            return accountNonLocked;
        }

        public void setAccountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
        }

        public boolean isCredentialsNonExpired() {
            return credentialsNonExpired;
        }

        public void setCredentialsNonExpired(boolean credentialsNonExpired) {
            this.credentialsNonExpired = credentialsNonExpired;
        }

        public boolean isAccountNonExpired() {
            return accountNonExpired;
        }

        public void setAccountNonExpired(boolean accountNonExpired) {
            this.accountNonExpired = accountNonExpired;
        }

        public List<SysRolesBean> getSysRoles() {
            return sysRoles;
        }

        public void setSysRoles(List<SysRolesBean> sysRoles) {
            this.sysRoles = sysRoles;
        }

        public static class SysRolesBean implements Serializable{
            private static final long serialVersionUID = 1L;
            /**
             * id : 1664b92dff13e1575e3a929caa2fa14d
             * createBy : admin
             * updateBy : admin
             * createTime : 2019-11-21 02:13:29
             * updateTime : 2019-11-21 02:13:29
             * roleName : 管理员
             * roleCode : ADMIN
             * description :
             */

            private String id;
            private String createBy;
            private String updateBy;
            private String createTime;
            private String updateTime;
            private String roleName;
            private String roleCode;
            private String description;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCreateBy() {
                return createBy;
            }

            public void setCreateBy(String createBy) {
                this.createBy = createBy;
            }

            public String getUpdateBy() {
                return updateBy;
            }

            public void setUpdateBy(String updateBy) {
                this.updateBy = updateBy;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public String getRoleName() {
                return roleName;
            }

            public void setRoleName(String roleName) {
                this.roleName = roleName;
            }

            public String getRoleCode() {
                return roleCode;
            }

            public void setRoleCode(String roleCode) {
                this.roleCode = roleCode;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }

        @Override
        public String toString() {
            return "UserBean{" +
                    "id='" + id + '\'' +
                    ", createBy='" + createBy + '\'' +
                    ", updateBy='" + updateBy + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", updateTime='" + updateTime + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", salt='" + salt + '\'' +
                    ", realName='" + realName + '\'' +
                    ", avatarUrl='" + avatarUrl + '\'' +
                    ", avatarStr='" + avatarStr + '\'' +
                    ", phone='" + phone + '\'' +
                    ", email='" + email + '\'' +
                    ", birthday='" + birthday + '\'' +
                    ", sex=" + sex +
                    ", status=" + status +
                    ", workNo='" + workNo + '\'' +
                    ", post='" + post + '\'' +
                    ", type='" + type + '\'' +
                    ", description='" + description + '\'' +
                    ", lastLoginTime='" + lastLoginTime + '\'' +
                    ", lastLoginIp='" + lastLoginIp + '\'' +
                    ", loginCount=" + loginCount +
                    ", roles='" + roles + '\'' +
                    ", roleId='" + roleId + '\'' +
                    ", oldPassword='" + oldPassword + '\'' +
                    ", newPassword='" + newPassword + '\'' +
                    ", permissions='" + permissions + '\'' +
                    ", enabled=" + enabled +
                    ", accountNonLocked=" + accountNonLocked +
                    ", credentialsNonExpired=" + credentialsNonExpired +
                    ", accountNonExpired=" + accountNonExpired +
                    ", sysRoles=" + sysRoles +
                    '}';
        }

    }

    public static class PermissionsBean implements Serializable{
        private static final long serialVersionUID = 1L;
        /**
         * id :
         * permission : ROLE_ADMIN
         * name :
         * createTime :
         * updateTime :
         * roleId :
         * authIds :
         */

        private String id;
        private String permission;
        private String name;
        private String createTime;
        private String updateTime;
        private String roleId;
        private String authIds;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPermission() {
            return permission;
        }

        public void setPermission(String permission) {
            this.permission = permission;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        public String getAuthIds() {
            return authIds;
        }

        public void setAuthIds(String authIds) {
            this.authIds = authIds;
        }

        @Override
        public String toString() {
            return "PermissionsBean{" +
                    "id='" + id + '\'' +
                    ", permission='" + permission + '\'' +
                    ", name='" + name + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", updateTime='" + updateTime + '\'' +
                    ", roleId='" + roleId + '\'' +
                    ", authIds='" + authIds + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserInfoEntity{" +
                "resp_code='" + resp_code + '\'' +
                ", user=" + user.toString() +
                ", permissions=" + permissions +
                '}';
    }
}
