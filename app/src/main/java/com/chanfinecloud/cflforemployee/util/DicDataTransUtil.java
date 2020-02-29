package com.chanfinecloud.cflforemployee.util;

import com.chanfinecloud.cflforemployee.entity.ComplainEntity;
import com.chanfinecloud.cflforemployee.entity.OrderStatusEntity;
import com.chanfinecloud.cflforemployee.entity.OrderTypeEntity;

import java.util.List;

/**
 * Created by Loong on 2020/2/17.
 * Version: 1.0
 * Describe: 处理字典的工具类
 */
public class DicDataTransUtil {
    private List<OrderTypeEntity> orderTypeEntities;
    private List<OrderStatusEntity> orderStatusEntities;
    private List<OrderTypeEntity> complainTypeEntities;
    private List<OrderStatusEntity> complainStatusEntities;

    private static DicDataTransUtil instance = new DicDataTransUtil();

    /**
     * 将构造函数私有化
     */
    private DicDataTransUtil(){
        orderTypeEntities=SharedPreferencesManage.getOrderType();
        orderStatusEntities=SharedPreferencesManage.getOrderStatus();
        complainTypeEntities=SharedPreferencesManage.getComplainType();
        complainStatusEntities=SharedPreferencesManage.getComplainStatus();
    }

    /**
     * 获取DicDataTransUtil的实例，保证只有一个DicDataTransUtil实例存在
     * @return
     */
    public static DicDataTransUtil getInstance(){
        return instance;
    }

    public String getOrderTypeChs(int orderType){
        String result="";
        for (int i = 0; i < orderTypeEntities.size(); i++) {
            if(orderTypeEntities.get(i).getId() == orderType){
                result=orderTypeEntities.get(i).getName();
            }
        }
        return result;
    }

    public String getOrderStatusChs(int orderStatus){
        String result="";
        for (int i = 0; i < orderStatusEntities.size(); i++) {
            if(orderStatusEntities.get(i).getId() == orderStatus){
                result=orderStatusEntities.get(i).getName();
            }
        }
        return result;
    }

    public String getComplainTypeChs(int complainType){
        String result="";
        for (int i = 0; i < complainTypeEntities.size(); i++) {
            if(complainTypeEntities.get(i).getId() == complainType){
                result=complainTypeEntities.get(i).getName();
            }
        }
        return result;
    }

    public String getComplainStatusChs(int complainStatus){
        String result="";
        for (int i = 0; i < complainStatusEntities.size(); i++) {
            if(complainStatusEntities.get(i).getId() == complainStatus){
                result=complainStatusEntities.get(i).getName();
            }
        }
        return result;
    }
}
