package com.chanfinecloud.cflforemployee.adapter;

import com.chanfinecloud.cflforemployee.ui.HomeTodoFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by Loong on 2020/2/12.
 * Version: 1.0
 * Describe: 首页待处理ViewPaper适配器
 */
public class HomeTodoPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager fragmentManager;
    private ArrayList<HomeTodoFragment> list;

    public HomeTodoPagerAdapter(@NonNull FragmentManager fm, int behavior, FragmentManager fragmentManager, ArrayList<HomeTodoFragment> list) {
        super(fm, behavior);
        this.fragmentManager = fragmentManager;
        this.list = list;
    }

    @Override//返回要显示的碎片
    public HomeTodoFragment getItem(int position) {
        return list.get(position);
    }

    @Override//返回要显示多少页
    public int getCount() {
        return list.size();
    }

}
