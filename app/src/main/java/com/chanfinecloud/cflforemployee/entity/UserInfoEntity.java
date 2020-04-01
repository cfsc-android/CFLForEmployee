package com.chanfinecloud.cflforemployee.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Loong on 2020/3/21.
 * Version: 1.0
 * Describe:
 */
public class UserInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * birthday : 1999-03-01T16:00:00.000+0000
     * logType : employee
     * gender : 1
     * credentialsNonExpired : true
     * authorities : [{"authority":"ROLE_ADMIN"}]
     * enabled : true
     * positionName : 工程部;经理
     * workNo : 002323
     * realName : 超级管理员
     * password : $2a$10$Veft7gw9/6DO/zVD./7Wn.PBf9UjslozXNrlDA4nAjwBTOyJX01SG
     * avatarId : 1711afd87a87c0760348cf944e284d16
     * positionId : ["17057941b938d881e63d11f481a84b7c","1710f7aba4cb029a3132be84bf0b6db7"]
     * phone : 13787176775
     * departName : 长房数创科技有限公司;研发部
     * accountNonExpired : true
     * id : a75d45a015c44384a04449ee80dc3503
     * departId : ["16f8a12e6002a83265d49974d0f853c2","170f0f29aae57d238923aa745afacc3e"]
     * email : 13787176775@139.com
     * sysRoles : [{"id":"1664b92dff13e1575e3a929caa2fa14d","createBy":"admin","updateBy":"admin","createTime":"2019-11-21 02:13:29","updateTime":"2019-11-21 02:13:29","roleName":"管理员","roleCode":"ADMIN","description":""}]
     * accountNonLocked : true
     * status : 0
     * username : admin
     */

    private String birthday;
    private String logType;
    private int gender;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private String positionName;
    private String workNo;
    private String realName;
    private String password;
    private String avatarId;
    private String phone;
    private String departName;
    private boolean accountNonExpired;
    private String id;
    private String email;
    private boolean accountNonLocked;
    private int status;
    private String username;
    private List<AuthoritiesBean> authorities;
    private List<String> projectIds;
    private List<String> positionId;
    private List<String> departId;
    private List<SysRolesBean> sysRoles;
    private List<PermissionsBean> permissions;
    private ResourceEntity avatarResource;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
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

    public List<AuthoritiesBean> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<AuthoritiesBean> authorities) {
        this.authorities = authorities;
    }

    public List<String> getPositionId() {
        return positionId;
    }

    public void setPositionId(List<String> positionId) {
        this.positionId = positionId;
    }


    public List<SysRolesBean> getSysRoles() {
        return sysRoles;
    }

    public void setSysRoles(List<SysRolesBean> sysRoles) {
        this.sysRoles = sysRoles;
    }

    public List<PermissionsBean> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionsBean> permissions) {
        this.permissions = permissions;
    }

    public ResourceEntity getAvatarResource() {
        return avatarResource;
    }

    public void setAvatarResource(ResourceEntity avatarResource) {
        this.avatarResource = avatarResource;
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

    public static class AuthoritiesBean implements Serializable{
        private static final long serialVersionUID = 1L;
        /**
         * authority : ROLE_ADMIN
         */

        private String authority;

        public String getAuthority() {
            return authority;
        }

        public void setAuthority(String authority) {
            this.authority = authority;
        }

        @Override
        public String toString() {
            return "AuthoritiesBean{" +
                    "authority='" + authority + '\'' +
                    '}';
        }
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

        @Override
        public String toString() {
            return "SysRolesBean{" +
                    "id='" + id + '\'' +
                    ", createBy='" + createBy + '\'' +
                    ", updateBy='" + updateBy + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", updateTime='" + updateTime + '\'' +
                    ", roleName='" + roleName + '\'' +
                    ", roleCode='" + roleCode + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public List<String> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<String> projectIds) {
        this.projectIds = projectIds;
    }

    public List<String> getDepartId() {
        return departId;
    }

    public void setDepartId(List<String> departId) {
        this.departId = departId;
    }
}