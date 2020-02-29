package com.chanfinecloud.cflforemployee.util;

import android.content.Context;

import com.chanfinecloud.cflforemployee.CFLApplication;

/**
 * Created by Loong on 2020/2/12.
 * Version: 1.0
 * Describe: 像素转换工具类
 */
public class SizeUtils {

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     * @param pxValue
     */
    public static int px2dip(float pxValue) {
        final float scale = CFLApplication.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     * @param dipValue
     */
    public static int dip2px(float dipValue) {
        final float scale = CFLApplication.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * @param pxValue
     */
    public static int px2sp(float pxValue) {
        final float fontScale = CFLApplication.getAppContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @param spValue
     */
    public static int sp2px(float spValue) {
        final float fontScale = CFLApplication.getAppContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
