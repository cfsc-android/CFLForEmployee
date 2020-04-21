package com.chanfinecloud.cflforemployee.ui;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chanfinecloud.cflforemployee.CFLApplication;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.OrderStatusEntity;
import com.chanfinecloud.cflforemployee.entity.OrderTypeListEntity;
import com.chanfinecloud.cflforemployee.entity.UserEntity;
import com.chanfinecloud.cflforemployee.entity.UserListEntity;
import com.chanfinecloud.cflforemployee.http.HttpMethod;
import com.chanfinecloud.cflforemployee.http.JsonParse;
import com.chanfinecloud.cflforemployee.http.MyCallBack;
import com.chanfinecloud.cflforemployee.http.RequestParam;
import com.chanfinecloud.cflforemployee.ui.base.BaseActivity;
import com.chanfinecloud.cflforemployee.entity.EventBusMessage;
import com.chanfinecloud.cflforemployee.entity.LoginEntity;
import com.chanfinecloud.cflforemployee.entity.TokenEntity;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.util.LynActivityManager;
import com.chanfinecloud.cflforemployee.util.SharedPreferencesManage;
import com.google.gson.reflect.TypeToken;
import com.pgyersdk.update.PgyUpdateManager;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cn.jpush.android.api.JPushInterface;
import me.leolin.shortcutbadger.ShortcutBadger;

import static android.provider.Settings.EXTRA_APP_PACKAGE;
import static android.provider.Settings.EXTRA_CHANNEL_ID;
import static com.chanfinecloud.cflforemployee.config.Config.BASE_URL;
import static com.chanfinecloud.cflforemployee.config.Config.SET_JPUSH_ALIAS_SEQUENCE;
import static com.chanfinecloud.cflforemployee.config.Config.SET_JPUSH_TAGS_SEQUENCE;
import static com.chanfinecloud.cflforemployee.config.Config.USER;
import static com.chanfinecloud.cflforemployee.config.Config.WORKORDER;
import static com.chanfinecloud.cflforemployee.util.SharedPreferencesManage.getUserInfo;


/**
 * Created by Loong on 2020/2/10.
 * Version: 1.0
 * Describe: 主页
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @ViewInject(R.id.main_tabs_iv_home)
    private ImageView main_tabs_iv_home;
    @ViewInject(R.id.main_tabs_iv_mine)
    private ImageView main_tabs_iv_mine;

    @ViewInject(R.id.person_ll_logout_layer)
    LinearLayout logout_layer;

    private Context context;
    private FragmentManager fragmentManager;
    private Fragment home, mine;
    private long time=0;
    private int isFirstIn = 0;
    private boolean permissionFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkAppPermission();
        super.onCreate(savedInstanceState);
        context=this;
        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);
        EventBus.getDefault().register(this);
        setAliasAndTag();
        initData();
    }

    /**
     * 初始化基础配置数据
     */
    private void initData(){

        isFirstIn = 0;
        permissionFlag = false;
        initOrderType();
        initOrderStatus();
        initComplainType();
        initComplainStatus();
        initEmployeeData();
        initDirectorData();
    }
    /**
     * 初始化工单类型
     */
    private void initOrderType(){
        RequestParam requestParam=new RequestParam(BASE_URL+WORKORDER+"work/orderType/pageByCondition", HttpMethod.Get);
        Map<String,String> map=new HashMap<>();
        map.put("pageNo","1");
        map.put("pageSize","100");
        requestParam.setRequestMap(map);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity<OrderTypeListEntity> baseEntity= JsonParse.parse(result,OrderTypeListEntity.class);
                if(baseEntity.isSuccess()){
                    SharedPreferencesManage.setOrderType(baseEntity.getResult().getData());
                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }
        });
        sendRequest(requestParam,false);
    }

    /**
     * 初始化工单状态
     */
    private void initOrderStatus(){
        RequestParam requestParam=new RequestParam(BASE_URL+WORKORDER+"work/orderStatus/selectWorkorderStatus",HttpMethod.Get);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                Type type = new TypeToken<List<OrderStatusEntity>>() {}.getType();
                BaseEntity<List<OrderStatusEntity>> baseEntity=JsonParse.parse(result,type);
                if(baseEntity.isSuccess()){
                    SharedPreferencesManage.setOrderStatus(baseEntity.getResult());
                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }
        });
        sendRequest(requestParam,false);
    }

    /**
     * 初始化投诉类型
     */
    private void initComplainType(){
        RequestParam requestParam=new RequestParam(BASE_URL+WORKORDER+"work/complaintType/pageByCondition",HttpMethod.Get);
        Map<String,String> map=new HashMap<>();
        map.put("pageNo","1");
        map.put("pageSize","100");
        requestParam.setRequestMap(map);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity<OrderTypeListEntity> baseEntity= JsonParse.parse(result,OrderTypeListEntity.class);
                if(baseEntity.isSuccess()){
                    SharedPreferencesManage.setComplainType(baseEntity.getResult().getData());
                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }
        });
        sendRequest(requestParam,false);
    }

    /**
     * 初始化投诉状态
     */
    private void initComplainStatus(){
        RequestParam requestParam=new RequestParam(BASE_URL+WORKORDER+"work/complaintStatus/complaintStatusList",HttpMethod.Get);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                Type type = new TypeToken<List<OrderStatusEntity>>() {}.getType();
                BaseEntity<List<OrderStatusEntity>> baseEntity=JsonParse.parse(result,type);
                if(baseEntity.isSuccess()){
                    SharedPreferencesManage.setComplainStatus(baseEntity.getResult());
                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }
        });
        sendRequest(requestParam,false);
    }
    /**
     * 初始化主管
     */
    private void initDirectorData(){
        RequestParam requestParam=new RequestParam(BASE_URL+USER+"sys/user/departSupervisor",HttpMethod.Get);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                Type type = new TypeToken<List<UserEntity>>() {}.getType();
                BaseEntity<List<UserEntity>> baseEntity=JsonParse.parse(result,type);
                if(baseEntity.isSuccess()){
                    SharedPreferencesManage.setDirectorList(baseEntity.getResult());
                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }
        });
        sendRequest(requestParam,false);
    }

    /**
     * 初始化员工
     */
    private void initEmployeeData(){
        RequestParam requestParam=new RequestParam(BASE_URL+USER+"sys/user/departStaff",HttpMethod.Get);
        Map<String,Object> map=new HashMap<>();
        StringBuilder departIds=new StringBuilder();
        List<String> list =SharedPreferencesManage.getUserInfo().getDepartId();
        if(list!=null&&list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                if(i!=0){
                    departIds.append(",");
                }
                departIds.append(list.get(i));
            }
            map.put("departIds",departIds.toString());
            requestParam.setRequestMap(map);
            requestParam.setCallback(new MyCallBack<String>(){
                @Override
                public void onSuccess(String result) {
                    super.onSuccess(result);
                    LogUtils.d("result",result);
                    Type type = new TypeToken<List<UserEntity>>() {}.getType();
                    BaseEntity<List<UserEntity>> baseEntity=JsonParse.parse(result,type);
                    if(baseEntity.isSuccess()){
                        SharedPreferencesManage.setEmployeeList(baseEntity.getResult());
                    }else{
                        showToast(baseEntity.getMessage());
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    super.onError(ex, isOnCallback);
                    showToast(ex.getMessage());
                }
            });
            sendRequest(requestParam,false);
        }

    }


    /**
     * 设置极光推送的alias（别名）和tag(标签)
     */
    private void setAliasAndTag(){
        if(SharedPreferencesManage.getPushFlag()){
            JPushInterface.setAlias(this,SET_JPUSH_ALIAS_SEQUENCE, getUserInfo().getId());
            Set<String> tagSet = new LinkedHashSet<>();
            tagSet.add("YG");
            List<String> projectIds= SharedPreferencesManage.getUserInfo().getProjectIds();
            if(projectIds!=null){
                for (int i = 0; i < projectIds.size(); i++) {
                    tagSet.add("P_"+projectIds.get(i));
                }
            }
            List<String> departIds= SharedPreferencesManage.getUserInfo().getDepartId();
            if(departIds!=null){
                for (int i = 0; i < departIds.size(); i++) {
                    tagSet.add("D_"+departIds.get(i));
                }
            }
            JPushInterface.setTags(this,SET_JPUSH_TAGS_SEQUENCE,tagSet);
        }
    }

    @Event({R.id.main_tabs_iv_home,R.id.main_tabs_iv_mine,R.id.person_ll_logout_layer,R.id.person_ll_logout_layer_change,
            R.id.person_ll_logout_out, R.id.person_ll_logout_cancel})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.main_tabs_iv_home:
                setTabSelection(0);
                break;
            case R.id.main_tabs_iv_mine:
                setTabSelection(1);
                break;
            case R.id.person_ll_logout_layer_change:
                SharedPreferencesManage.setLoginInfo(new LoginEntity("",""));
                TokenEntity tokenEntity=SharedPreferencesManage.getToken();
                tokenEntity.setExpires_in(0);
                SharedPreferencesManage.setToken(tokenEntity);
                LynActivityManager.getInstance().removeAllActivity();
                startActivity(LoginActivity.class);
                break;
            case R.id.person_ll_logout_out:
                LynActivityManager.getInstance().exit();
                break;
            default:
                logout_layer.setVisibility(View.GONE);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage message){
        if("Quit".equals(message.getMessage())){
            logout_layer.setVisibility(View.VISIBLE);
            logout_layer.startAnimation(alphaAnimation());
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 退出ActionSheet动画
     * @return AlphaAnimation
     */
    private AlphaAnimation alphaAnimation(){
        AlphaAnimation animation=new AlphaAnimation(0,1);
        animation.setDuration(200);
        return animation;
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index tab页index
     */
    private void setTabSelection(int index) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                if (home == null) {
                    home = new HomeFragment();
                    transaction.add(R.id.main_fl_content, home);
                } else {
                    transaction.show(home);
                }
                main_tabs_iv_home.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_tab_home_focus));
                main_tabs_iv_mine.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_tab_mine_blur));
                break;
            case 1:
                if (mine == null) {
                    mine = new MineFragment();
                    transaction.add(R.id.main_fl_content, mine);
                } else {
                    transaction.show(mine);
                }
                main_tabs_iv_home.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_tab_home_blur));
                main_tabs_iv_mine.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_tab_mine_focus));
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (home != null) {
            transaction.hide(home);
        }
        if (mine != null) {
            transaction.hide(mine);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断按返回键时
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(new Date().getTime()-time<2000&&time!=0){
                LynActivityManager.getInstance().exit();
            }else{
                showToast("再按一次退出");
                time=new Date().getTime();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ShortcutBadger.removeCount(getApplicationContext()); //for 1.1.4+
        if ( isFirstIn == 0){
            isFirstIn = 1;
        }else{

            if (!permissionFlag){
                //悬浮窗权限不强求吧
                //requestAndOverlay();
                requestAndNotificationListener();
            }

        }
    }

    public void requestAndOverlay(){
        AndPermission.with(this)
                .overlay()
                .rationale(new Rationale<Void>() {
                    @Override
                    public void showRationale(Context context, Void data, final RequestExecutor executor) {

                        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.dialog);
                        View view = LayoutInflater.from(context).inflate(R.layout.dialog_runtime_before_request, null);
                        TextView tvTitle = view.findViewById(R.id.tv_dialog_permission_title);
                        TextView tvDescription = view.findViewById(R.id.tv_dialog_permission_description);
                        TextView tvNext = view.findViewById(R.id.tv_dialog_permission_next);
                        TextView tvClose = view.findViewById(R.id.tv_dialog_permission_close);

                        tvNext.setText("去打开");
                        tvTitle.setText("悬浮窗显示");
                        tvDescription.setText("我们将开始请求悬浮窗显示权限");
                        // 设置我们自己定义的布局文件作为弹出框的Content
                        alertDialogBuilder.setView(view);
                        //按对话框以外的地方不起作用,按返回键也不起作用,防止点击外面对话框消失
                        alertDialogBuilder.setCancelable(false);
                        //这个位置十分重要，只有位于这个位置逻辑才是正确的
                        final androidx.appcompat.app.AlertDialog dialog = alertDialogBuilder.show();
                        tvNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                executor.execute();
                                dialog.dismiss();
                            }
                        });

                        tvClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                executor.cancel();
                                dialog.dismiss();
                            }
                        });

                    }
                })
                .onGranted(new Action<Void>() {
                    @Override
                    public void onAction(Void permissions) {
                        // Storage permission are allowed.
                        requestAndNotificationListener();
                    }
                })
                .onDenied(new Action<Void>() {
                    @Override
                    public void onAction(Void permissions) {
                        // Storage permission are not allowed.
                        Toast.makeText(MainActivity.this, "悬浮窗权限获取失败", Toast.LENGTH_SHORT).show();
                    }
                })
                .start();

    }

    private void requestAndNotificationListener() {
        AndPermission.with(this)
                .notification()
                .listener()
                .rationale(new Rationale<Void>() {
                    @Override
                    public void showRationale(Context context, Void data, final RequestExecutor executor) {
                        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.dialog);
                        View view = LayoutInflater.from(context).inflate(R.layout.dialog_runtime_before_request, null);
                        TextView tvTitle = view.findViewById(R.id.tv_dialog_permission_title);
                        TextView tvDescription = view.findViewById(R.id.tv_dialog_permission_description);
                        TextView tvNext = view.findViewById(R.id.tv_dialog_permission_next);
                        TextView tvClose = view.findViewById(R.id.tv_dialog_permission_close);

                        tvNext.setText("去打开");
                        tvTitle.setText("管理通知");
                        tvDescription.setText("我们将开始请求通知管理权限");
                        // 设置我们自己定义的布局文件作为弹出框的Content
                        alertDialogBuilder.setView(view);
                        //按对话框以外的地方不起作用,按返回键也不起作用,防止点击外面对话框消失
                        alertDialogBuilder.setCancelable(false);
                        //这个位置十分重要，只有位于这个位置逻辑才是正确的
                        final androidx.appcompat.app.AlertDialog dialog = alertDialogBuilder.show();
                        tvNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                executor.execute();
                                dialog.dismiss();
                            }
                        });

                        tvClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                executor.cancel();
                                dialog.dismiss();
                            }
                        });

                    }
                })
                .onGranted(new Action<Void>() {
                    @Override
                    public void onAction(Void permissions) {
                        // Storage permission are allowed.
                        //permissionFlag = true;
                        requestAndNotificationShow();

                    }
                })
                .onDenied(new Action<Void>() {
                    @Override
                    public void onAction(Void permissions) {
                        // Storage permission are not allowed.
                        Toast.makeText(MainActivity.this, "通知管理权限获取失败", Toast.LENGTH_SHORT).show();
                    }
                })
                .start();

    }


    private void requestAndNotificationShow() {
        AndPermission.with(this)
                .notification()
                .permission()
                .rationale(new Rationale<Void>() {
                    @Override
                    public void showRationale(Context context, Void data, final RequestExecutor executor) {

                        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.dialog);
                        View view = LayoutInflater.from(context).inflate(R.layout.dialog_runtime_before_request, null);
                        TextView tvTitle = view.findViewById(R.id.tv_dialog_permission_title);
                        TextView tvDescription = view.findViewById(R.id.tv_dialog_permission_description);
                        TextView tvNext = view.findViewById(R.id.tv_dialog_permission_next);
                        TextView tvClose = view.findViewById(R.id.tv_dialog_permission_close);

                        tvNext.setText("去打开");
                        tvTitle.setText("接收通知");
                        tvDescription.setText("我们将开始请求通知显示权限");
                        // 设置我们自己定义的布局文件作为弹出框的Content
                        alertDialogBuilder.setView(view);
                        //按对话框以外的地方不起作用,按返回键也不起作用,防止点击外面对话框消失
                        alertDialogBuilder.setCancelable(false);
                        //这个位置十分重要，只有位于这个位置逻辑才是正确的
                        final AlertDialog dialog = alertDialogBuilder.show();
                        tvNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    toOpenNotificationAccessManually();
                                }else{
                                    executor.execute();
                                }
                                dialog.dismiss();
                            }
                        });

                        tvClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                executor.cancel();
                                dialog.dismiss();
                            }
                        });

                    }
                })
                .onGranted(new Action<Void>() {
                    @Override
                    public void onAction(Void permissions) {
                        // Storage permission are allowed.
                        //requestAndNotificationListener();
                        permissionFlag = true;
                    }
                })
                .onDenied(new Action<Void>() {
                    @Override
                    public void onAction(Void permissions) {
                        // Storage permission are not allowed.
                        Toast.makeText(MainActivity.this, "显示通知权限获取失败", Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }

    /**
     * 去设置里手动打开Notification权限
     *
     */
    public void toOpenNotificationAccessManually() {
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
            intent.putExtra(EXTRA_APP_PACKAGE, getPackageName());
            intent.putExtra(EXTRA_CHANNEL_ID, getApplicationInfo().uid);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
            Intent intent = new Intent();
            //下面这种方案是直接跳转到当前应用的设置界面。
            //https://blog.csdn.net/ysy950803/article/details/71910806
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

}
