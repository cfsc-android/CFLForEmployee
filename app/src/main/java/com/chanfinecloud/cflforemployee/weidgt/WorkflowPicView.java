package com.chanfinecloud.cflforemployee.weidgt;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chanfinecloud.cflforemployee.entity.ResourceEntity;
import com.chanfinecloud.cflforemployee.weidgt.imagepreview.ImageViewInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by Loong on 2020/2/18.
 * Version: 1.0
 * Describe: 流程图片自定义视图组件
 */
public class WorkflowPicView extends LinearLayout {
    private Context context;

    public WorkflowPicView(Context context) {
        super(context);
        this.context=context;
        setOrientation(VERTICAL);
    }

    public WorkflowPicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WorkflowPicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initData(List<ImageViewInfo> picData,int column){
        int rows= (picData.size()/column)+(picData.size()%column==0?0:1);
        for (int i = 0; i < rows; i++) {
            LinearLayout ll=new LinearLayout(context);
            LayoutParams lp=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.setLayoutParams(lp);
            ll.setOrientation(HORIZONTAL);
            addView(ll);
        }
        for (int i = 0; i < picData.size(); i++) {
            LinearLayout ll= (LinearLayout) getChildAt(((i+1)/3)+((i+1)%3)-1);
            ImageView iv=new ImageView(context);
            LayoutParams lp=new LayoutParams(30,30);
            lp.weight=1;
            iv.setLayoutParams(lp);
//            Glide.with(context)
//                    .load(picData.get(i).getUrl())
//                    .into(iv);
            ll.addView(iv);
        }
    }
}
