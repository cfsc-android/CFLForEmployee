package com.chanfinecloud.cflforemployee.weidgt.photopicker;

import android.app.Activity;
import android.content.pm.ActivityInfo;

import com.chanfinecloud.cflforemployee.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import static com.chanfinecloud.cflforemployee.config.Config.PROVIDER_AUTHORITY;

/**
 * Created by Loong on 2019/12/31.
 * Version: 1.0
 * Describe: 照片选取
 */
public class PhotoPicker {

    public static void pick(Activity context, int request_code){
        Matisse.from(context)
                .choose(MimeType.ofAll())
                .theme(R.style.Matisse_Customer)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(context.getResources().getDimensionPixelSize(R.dimen.dp_120))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(request_code);
    }
    public static void pick(Activity context, int maxSelectable,int request_code){
        Matisse.from(context)
                .choose(MimeType.ofAll())
                .theme(R.style.Matisse_Customer)
                .countable(true)
                .maxSelectable(maxSelectable)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(context.getResources().getDimensionPixelSize(R.dimen.dp_120))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(request_code);
    }
    public static void pick(Activity context,boolean capture, int request_code){
        Matisse.from(context)
                .choose(MimeType.ofAll())
                .theme(R.style.Matisse_Customer)
                .capture(capture)
                .captureStrategy(new CaptureStrategy(true, PROVIDER_AUTHORITY))
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(context.getResources().getDimensionPixelSize(R.dimen.dp_120))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(request_code);
    }
    public static void pick(Activity context,int maxSelectable, boolean capture,int request_code){
        Matisse.from(context)
                .choose(MimeType.ofImage())
                .theme(R.style.Matisse_Customer)
                .capture(capture)
                .captureStrategy(new CaptureStrategy(true, PROVIDER_AUTHORITY))
                .countable(true)
                .maxSelectable(maxSelectable)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(context.getResources().getDimensionPixelSize(R.dimen.dp_120))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(request_code);
    }
}
