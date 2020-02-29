package com.chanfinecloud.cflforemployee.base;


import com.chanfinecloud.cflforemployee.BuildConfig;

/**
 * Created by Loong on 2020/2/6.
 * Version: 1.0
 * Describe: 基础配置
 */
public class Config {

    public static final String BASE_URL= BuildConfig.BASE_URL;//后台服务地址

    public static final String PROVIDER_AUTHORITY="com.chanfinecloud.cflforemployee.fileprovider";//清单文件中provider的authorities属性的值
    public static final String SD_APP_DIR_NAME = "CFL"; //存储程序在外部SD卡上的根目录的名字
    public static final String PHOTO_DIR_NAME = "photo";    //存储照片在根目录下的文件夹名字
    public static final String File_DIR_NAME = "file";    //存储文件在根目录下的文件夹名字

}