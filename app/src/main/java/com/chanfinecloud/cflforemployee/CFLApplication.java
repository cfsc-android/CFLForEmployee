package com.chanfinecloud.cflforemployee;

import android.app.Application;
import android.content.Context;

import com.chanfinecloud.cflforemployee.entity.Transition;
import com.pgyersdk.crash.PgyCrashManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * Created by Loong on 2020/2/3.
 * Version: 1.0
 * Describe:
 */
public class CFLApplication extends Application {

    public static CFLApplication getInstance = null;
    private Context mContext;

    public static Map<Class, Transition> activityTrans=new HashMap<>();

    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        CFLApplication application = (CFLApplication) context.getApplicationContext();
        return application.refWatcher;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        getInstance = this;
        mContext = getApplicationContext();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        //蒲公英crash收集注册
        PgyCrashManager.register();
        UMConfigure.init(this,"5cb6d69f0cafb29742001079","umeng",UMConfigure.DEVICE_TYPE_PHONE,"");

        refWatcher = LeakCanary.install(this);

    }

    {
        PlatformConfig.setWeixin("wxd49aa41b4120e385", "56aeeca40a0f8165ac28c102795255ff");
        PlatformConfig.setQQZone("101852439", "acddc3daf0e8f674cde2068ccdf282ea");
    }

    public static CFLApplication getInstance() {
        return getInstance;
    }

    public static Context getAppContext() {
        return CFLApplication.getInstance.mContext;
    }

    static {//static 代码段可以防止内存泄露
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.main_background, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }

        });
    }
}