package com.chanfinecloud.cflforemployee.util;


import com.chanfinecloud.cflforemployee.CFLApplication;
import com.chanfinecloud.cflforemployee.entity.LoginEntity;
import com.chanfinecloud.cflforemployee.entity.OrderStatusEntity;
import com.chanfinecloud.cflforemployee.entity.OrderTypeEntity;
import com.chanfinecloud.cflforemployee.entity.TokenEntity;
import com.chanfinecloud.cflforemployee.entity.UserEntity;
import com.chanfinecloud.cflforemployee.entity.UserInfoEntity;

import java.util.List;

/**
 * Created by Loong on 2020/2/7.
 * Version: 1.0
 * Describe: SharedPreferences管理类
 */
public class SharedPreferencesManage {
    public static void setComplainType(List<OrderTypeEntity> list){
        SharedPreferencesUtil.getInstance().saveObject(CFLApplication.getAppContext(),"cfl","complainType",list);
    }

    public static List<OrderTypeEntity> getComplainType(){
        return (List<OrderTypeEntity>) SharedPreferencesUtil.getInstance().getObject(CFLApplication.getAppContext(),"cfl","complainType");
    }

    public static void setComplainStatus(List<OrderStatusEntity> list){
        SharedPreferencesUtil.getInstance().saveObject(CFLApplication.getAppContext(),"cfl","complainStatus",list);
    }

    public static List<OrderStatusEntity> getComplainStatus(){
        return (List<OrderStatusEntity>) SharedPreferencesUtil.getInstance().getObject(CFLApplication.getAppContext(),"cfl","complainStatus");
    }

    public static void setOrderType(List<OrderTypeEntity> list){
        SharedPreferencesUtil.getInstance().saveObject(CFLApplication.getAppContext(),"cfl","orderType",list);
    }

    public static List<OrderTypeEntity> getOrderType(){
        return (List<OrderTypeEntity>) SharedPreferencesUtil.getInstance().getObject(CFLApplication.getAppContext(),"cfl","orderType");
    }

    public static void setOrderStatus(List<OrderStatusEntity> list){
        SharedPreferencesUtil.getInstance().saveObject(CFLApplication.getAppContext(),"cfl","orderStatus",list);
    }

    public static List<OrderStatusEntity> getOrderStatus(){
        return (List<OrderStatusEntity>) SharedPreferencesUtil.getInstance().getObject(CFLApplication.getAppContext(),"cfl","orderStatus");
    }

    public static void setLoginInfo(LoginEntity loginInfo){
        SharedPreferencesUtil.getInstance().saveObject(CFLApplication.getAppContext(),"cfl","loginInfo",loginInfo);
    }

    public static LoginEntity getLoginInfo(){
        return (LoginEntity) SharedPreferencesUtil.getInstance().getObject(CFLApplication.getAppContext(),"cfl","loginInfo");
    }


    public static void setToken(TokenEntity token){
        SharedPreferencesUtil.getInstance().saveObject(CFLApplication.getAppContext(),"cfl","Token",token);
    }
    public static TokenEntity getToken(){
        return (TokenEntity) SharedPreferencesUtil.getInstance().getObject(CFLApplication.getAppContext(),"cfl","Token");
    }

    public static void setUserInfo(UserInfoEntity userInfo){
        SharedPreferencesUtil.getInstance().saveObject(CFLApplication.getAppContext(),"cfl","UserInfo",userInfo);
    }
    public static UserInfoEntity getUserInfo(){
        return (UserInfoEntity) SharedPreferencesUtil.getInstance().getObject(CFLApplication.getAppContext(),"cfl","UserInfo");
    }

    public static void setDirectorList(List<UserEntity> userList){
        SharedPreferencesUtil.getInstance().saveObject(CFLApplication.getAppContext(),"cfl","Director",userList);
    }
    public static List<UserEntity> getDirectorList(){
        return (List<UserEntity>) SharedPreferencesUtil.getInstance().getObject(CFLApplication.getAppContext(),"cfl","Director");
    }

    public static void setEmployeeList(List<UserEntity> userList){
        SharedPreferencesUtil.getInstance().saveObject(CFLApplication.getAppContext(),"cfl","Employee",userList);
    }
    public static List<UserEntity> getEmployeeList(){
        return (List<UserEntity>) SharedPreferencesUtil.getInstance().getObject(CFLApplication.getAppContext(),"cfl","Employee");
    }


    public static void setPushFlag(boolean flag){
        SharedPreferencesUtil.getInstance().saveBooleanValue(CFLApplication.getAppContext(),"cfl","PushFlag",flag);
    }

    public static boolean getPushFlag(){
        return SharedPreferencesUtil.getInstance().getBooleanValue(CFLApplication.getAppContext(),"cfl","PushFlag",true);
    }

    public static void setNotificationFlag(boolean flag){
        SharedPreferencesUtil.getInstance().saveBooleanValue(CFLApplication.getAppContext(),"cfl","NotificationFlag",flag);
    }

    public static boolean getNotificationFlag(){
        return SharedPreferencesUtil.getInstance().getBooleanValue(CFLApplication.getAppContext(),"cfl","NotificationFlag",true);
    }

}

