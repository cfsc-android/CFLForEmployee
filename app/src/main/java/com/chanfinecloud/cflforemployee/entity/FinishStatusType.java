package com.chanfinecloud.cflforemployee.entity;

/**
 * Created by Loong on 2020/2/17.
 * Version: 1.0
 * Describe: 用户类型
 */
public enum FinishStatusType {
    UnFinish("0","进行中"),
    IsFinish("1","已完成");

    private final String type;
    private final String typeChs;
    FinishStatusType(String type, String typeChs){
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
