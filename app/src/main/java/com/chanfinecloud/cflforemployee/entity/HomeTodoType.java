package com.chanfinecloud.cflforemployee.entity;

/**
 * Created by Loong on 2020/2/12.
 * Version: 1.0
 * Describe: 首页待处理枚举
 */
public enum HomeTodoType {
    待处理工单(0x001),//工单
    待处理投诉(0x002);//投诉

    private final int type;
    HomeTodoType(int type){
        this.type=type;
    }

    public int getType() {
        return type;
    }

    public static int size() {
        return HomeTodoType.values().length;
    }

    public static String[] getPageNames() {
        HomeTodoType[] pages = HomeTodoType.values();
        String[] pageNames = new String[pages.length];
        for (int i = 0; i < pages.length; i++) {
            pageNames[i] = pages[i].name();
        }
        return pageNames;
    }
}
