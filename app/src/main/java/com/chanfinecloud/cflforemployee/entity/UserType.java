package com.chanfinecloud.cflforemployee.entity;

/**
 * Created by Loong on 2020/2/17.
 * Version: 1.0
 * Describe: 用户类型
 */
public enum UserType {
    Household("1","业主"),
    Employee("2","员工");

    private final String type;
    private final String typeChs;
    UserType(String type, String typeChs){
        this.type=type;
        this.typeChs=typeChs;
    }
    public String getType() {
        return type;
    }

    public String getTypeChs() {
        return typeChs;
    }
}
