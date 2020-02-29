package com.chanfinecloud.cflforemployee.entity;

/**
 * Created by Loong on 2020/2/6.
 * Version: 1.0
 * Describe: 转场动画枚举
 */
public enum Transition {
    TopIn(0x001),
    TopOut(-0x001),
    LeftIn(0x002),
    LeftOut(-0x002),
    BottomIn(0x003),
    BottomOut(-0x003),
    RightIn(0x004),
    RightOut(-0x004);
    private final int type;

    Transition(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}