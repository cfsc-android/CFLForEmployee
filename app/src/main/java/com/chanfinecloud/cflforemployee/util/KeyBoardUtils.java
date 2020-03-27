package com.chanfinecloud.cflforemployee.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Loong on 2020/2/19.
 * Version: 1.0
 * Describe: 软键盘工具类
 */
public class KeyBoardUtils {
    /**
     * 打卡软键盘
     *
     * @param mEditText 输入框
     * @param mContext 上下文
     */
    public static void openKeybord(EditText mEditText, Context mContext)
    {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     * @param mContext 上下文
     */
    public static void closeKeybord(EditText mEditText, Context mContext)
    {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 隐藏软键盘
     * @param context Context
     * @param windowToken View
     */
    public static void hideSoftInputMode(Context context, View windowToken) {

        InputMethodManager imm = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));

        imm.hideSoftInputFromWindow(windowToken.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

    }

    /**
     * 弹出软键盘
     * @param context Context
     * @param windowToken View
     */
    public static void showSoftInputMode(Context context,View windowToken) {

        final InputMethodManager imm =(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(windowToken, InputMethodManager.SHOW_FORCED);
    }
}
