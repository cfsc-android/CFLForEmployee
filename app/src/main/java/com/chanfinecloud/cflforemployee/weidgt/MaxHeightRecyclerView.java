package com.chanfinecloud.cflforemployee.weidgt;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.util.SizeUtils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Loong on 2020/2/21.
 * Version: 1.0
 * Describe:
 */
public class MaxHeightRecyclerView extends RecyclerView {
    /**
     * 默认最大高度
     **/
    private int maxHeight = 1000;

    public MaxHeightRecyclerView(Context context)
    {
        super(context);
        init(context, null);
    }

    public MaxHeightRecyclerView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        TypedArray a = attrs == null ? null : context.obtainStyledAttributes(attrs, R.styleable.MaxHeightRecycler);

        if (a != null)
        {
            try
            {
                maxHeight = a.getInteger(R.styleable.MaxHeightRecycler_maxHeight, 1000);
            } finally
            {
                a.recycle();
            }
        }
        // 设置的高度dp转成px
        maxHeight = SizeUtils.dip2px(maxHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (getChildCount() > 0)
        {
            int max=0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                RecyclerView.LayoutParams params = (LayoutParams) child.getLayoutParams();
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                // item高度
                int height = child.getMeasuredHeight() + getPaddingTop() + getPaddingBottom() + params.topMargin + params.bottomMargin;
//                max+=SizeUtils.dip2px(height);
                max+=height;
            }
            LogUtils.d("max:"+max);
//            setMeasuredDimension(widthMeasureSpec, Math.min(max, maxHeight));
            setMeasuredDimension(widthMeasureSpec, max);
        } else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}

