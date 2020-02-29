package com.chanfinecloud.cflforemployee.weidgt.adtext;

import com.chanfinecloud.cflforemployee.entity.NoticeEntity;

/**
 * Created by Loong on 2020/2/12.
 * Version: 1.0
 * Describe: 广告被点击时回调
 */
public interface OnAdConetentClickListener {
    void OnAdConetentClickListener(int index, NoticeEntity entity);
}
