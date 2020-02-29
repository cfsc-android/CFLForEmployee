package com.chanfinecloud.cflforemployee.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.chanfinecloud.cflforemployee.R;

import org.xutils.image.ImageOptions;

/**
 * Created by Loong on 2020/2/10.
 * Version: 1.0
 * Describe: Utils
 */
public class Utils {
    /**
     * 获取当前客户端版本信息versionName
     * @param mContext
     * @return
     */
    public static String getCurrentVersion(Context mContext) {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0);
            return info.versionName.trim();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        return "";
    }
    /**
     * 获取当前客户端版本信息versionCode
     * @param mContext
     * @return
     */
    public static int getCurrentBuild(Context mContext) {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        return 0;
    }

    public static String transformIOSTime(String time){
        String str=time.replace("T","日 ");
        str=str.replaceFirst("-","年");
        str=str.replaceFirst("-","月");
        return str;
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
}

