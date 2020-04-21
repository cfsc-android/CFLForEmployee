package com.chanfinecloud.cflforemployee.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.chanfinecloud.cflforemployee.CFLApplication;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.weidgt.BadgeView;

import org.xutils.image.ImageOptions;


/**
 * Created by Loong on 2020/3/25.
 * Version: 1.0
 * Describe: Utils
 */
public class Utils {

    /**
     * 获取当前客户端版本信息versionName
     * @return String
     */
    public static String getCurrentVersion() {
        try {
            PackageInfo info = CFLApplication.getAppContext().getPackageManager().getPackageInfo(
                    CFLApplication.getAppContext().getPackageName(), 0);
            return info.versionName.trim();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        return "";
    }
    /**
     * 获取当前客户端版本信息versionCode
     * @return int
     */
    public static int getCurrentBuild() {
        try {
            PackageInfo info = CFLApplication.getAppContext().getPackageManager().getPackageInfo(
                    CFLApplication.getAppContext().getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        return 0;
    }

    /**
     * 将Drawable转化为Bitmap
     *
     * @param drawable Drawable
     * @return Bitmap
     */
    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
    /**
     * 计算状态栏高度高度 getStatusBarHeight
     *
     * @return
     */
    public static int getStatusBarHeight() {
        return getInternalDimensionSize(Resources.getSystem(),
                STATUS_BAR_HEIGHT_RES_NAME);
    }

    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static ImageOptions getImageOption(){
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setSize(120, 120)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                //设置加载过程中的图片
                .setLoadingDrawableId(R.drawable.ic_launcher)
                //设置加载失败后的图片
                .setFailureDrawableId(R.drawable.ic_launcher)
                //设置支持gif
                .setIgnoreGif(false)
                //设置显示圆形图片
                .setCircular(false)
                .setSquare(true)
                .build();
        return imageOptions;
    }



    public static BadgeView toShowBadgeView(Context context, View target, final int count, BadgeView badgeView, int nType) {

        int lastNum = 0;

        if(badgeView == null){
            badgeView = new BadgeView(context, target);//getActivity(), 目标控件
            badgeView.setBadgeBackgroundColor(Color.parseColor("#fffa5050"));
            badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);//设置显示的位置
            badgeView.setTextSize(12);//设置字体大小
            badgeView.setTextColor(Color.WHITE);//设置字体颜色
            badgeView.setGravity(Gravity.CENTER);
        }else {

            lastNum = Integer.parseInt(badgeView.getText().toString());
        }

        if (count > 0) {
            if (count + lastNum < 99) {
                badgeView.setBadgeMargin(24,10);//设置margin
                badgeView.setText((count + lastNum) + "");//设置显示的内容
            } else {
                badgeView.setText("99+");
                badgeView.setBadgeMargin(20,10);//设置margin
            }

            // 1
            // 设置进入的移动动画，设置了插值器，可以实现颤动的效果
            TranslateAnimation anim1 = new TranslateAnimation(0, 0, 0, 0);
            anim1.setInterpolator(new BounceInterpolator());
            // 设置动画的持续时间
            anim1.setDuration(500);
            badgeView.show(true,anim1);

            //orderBadgeTextView.show();//显示
        } else {
            badgeView.hide();//隐藏
        }

        return badgeView;

    }



    public static void toHideBadgeView(BadgeView badgeView) {

        if (badgeView != null && badgeView.isShown()){
            // 设置退出的移动动画
            TranslateAnimation anim2 = new TranslateAnimation(0, 0, 0, 0);
            anim2.setDuration(500);
            badgeView.hide(false, anim2);
            badgeView.setText(0+"");
        }
    }
}
