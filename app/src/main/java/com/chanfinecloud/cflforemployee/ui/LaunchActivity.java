package com.chanfinecloud.cflforemployee.ui;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.base.BaseActivity;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.OrderStatusEntity;
import com.chanfinecloud.cflforemployee.entity.OrderTypeListEntity;
import com.chanfinecloud.cflforemployee.entity.TokenEntity;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.util.SharedPreferencesManage;
import com.chanfinecloud.cflforemployee.util.Utils;
import com.chanfinecloud.cflforemployee.util.http.HttpMethod;
import com.chanfinecloud.cflforemployee.util.http.JsonParse;
import com.chanfinecloud.cflforemployee.util.http.MyCallBack;
import com.chanfinecloud.cflforemployee.util.http.RequestParam;
import com.google.gson.reflect.TypeToken;
import com.pgyersdk.update.DownloadFileListener;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.update.javabean.AppBean;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

import static com.chanfinecloud.cflforemployee.base.Config.BASE_URL;


/**
 * Created by Loong on 2020/2/10.
 * Version: 1.0
 * Describe: 应用启动页
 */
@ContentView(R.layout.activity_launch)
public class LaunchActivity extends BaseActivity {

    @ViewInject(R.id.tv_loading_version)
    private TextView tv_loading_version;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setFullScreen(false);
        super.onCreate(savedInstanceState);
        tv_loading_version.setText("v-"+ Utils.getCurrentVersion(this)+" : "+Utils.getCurrentBuild(this));
        checkVersion();
    }

    /**
     * 检查版本
     */
    private void checkVersion(){
        new PgyUpdateManager.Builder()
                .setForced(false)                //设置是否强制提示更新,非自定义回调更新接口此方法有用
                .setUserCanRetry(false)         //失败后是否提示重新下载，非自定义下载 apk 回调此方法有用
                .setDeleteHistroyApk(false)     // 检查更新前是否删除本地历史 Apk， 默认为true
                .setUpdateManagerListener(new UpdateManagerListener() {
                    @Override
                    public void onNoUpdateAvailable() {
                        //没有更新是回调此方法
                        Log.d("pgyer", "there is no new version");
//                        finish();
                        checkAutoLogin();
                    }
                    @Override
                    public void onUpdateAvailable(final AppBean appBean) {
                        //有更新回调此方法
                        Log.d("pgyer", "there is new version can update"
                                + " new versionCode is " + appBean.getVersionCode()
                                + " new versionName is " + appBean.getVersionName());
                        //调用以下方法，DownloadFileListener 才有效；
                        //如果完全使用自己的下载方法，不需要设置DownloadFileListener
                        if(Utils.getCurrentVersion(LaunchActivity.this).equals(appBean.getVersionName())){
                            new AlertDialog.Builder(LaunchActivity.this)
                                    .setTitle("更新")
                                    .setMessage("发现新版本"+appBean.getVersionName()+"\n"+appBean.getReleaseNote())
                                    .setCancelable(false)
                                    .setNegativeButton("确认更新",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            PgyUpdateManager.downLoadApk(appBean.getDownloadURL());
                                        }
                                    }).
                                    setNeutralButton("下次再说", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
//                                            finish();
                                            checkAutoLogin();
                                        }
                                    }).show();
                        }else{
                            new AlertDialog.Builder(LaunchActivity.this)
                                    .setTitle("强制更新")
                                    .setMessage("发现新版本"+appBean.getVersionName()+"\n"+appBean.getReleaseNote())
                                    .setCancelable(false)
                                    .setNegativeButton(
                                            "确认更新",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    PgyUpdateManager.downLoadApk(appBean.getDownloadURL());
                                                }
                                            }).show();
                        }
                    }

                    @Override
                    public void checkUpdateFailed(Exception e) {
                        //更新检测失败回调
                        //更新拒绝（应用被下架，过期，不在安装有效期，下载次数用尽）以及无网络情况会调用此接口
                        Log.e("pgyer", "check update failed ", e);
//                        finish();
                        checkAutoLogin();
                    }
                })
                .setDownloadFileListener(new DownloadFileListener() {
                    @Override
                    public void downloadFailed() {
                        //下载失败
                        Log.e("pgyer", "download apk failed");
//                        finish();
                        checkAutoLogin();
                    }

                    @Override
                    public void downloadSuccessful(File file) {
                        PgyUpdateManager.installApk(file);
                    }
                    @Override
                    public void onProgressUpdate(Integer... integers) {
                        Log.e("pgyer", "update download apk progress" + integers[0]);
                        initProgressDialog(integers[0]);
                    }
                })
                .register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyUpdateManager.unRegister();
    }

    /**
     * 下载最新版进度
     * @param progress
     */
    private void initProgressDialog(int progress){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);// 设置是否可以通过点击Back键取消
            progressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
            progressDialog.setTitle("下载最新版");
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
        }else{
            progressDialog.setProgress(progress);
            if(progress==100){
                progressDialog.dismiss();
            }
        }
    }

    /**
     * 检查是否自动登录
     */
    private void checkAutoLogin(){
//        initData();
        //清空项目过滤的值
        TokenEntity token= SharedPreferencesManage.getToken();
        if(token!=null){
            long time=new Date().getTime()/1000 - token.getInit_time();
            if(token.getExpires_in()-time>3){
                initData();
            }else{
                startActivity(LoginActivity.class);
                finish();
            }
        }else{
            startActivity(LoginActivity.class);
            finish();
        }
    }

    private List<Integer> initStatus=new ArrayList<>();

    private void checkInitStatus(int status){
        initStatus.add(status);
        if(initStatus.indexOf(0)!=-1){
            startActivity(LoginActivity.class);
            finish();
        }else{
            if(initStatus.size()==4){
                startActivity(MainActivity.class);
                finish();
            }
        }
    }

    private void initData(){
        initOrderType();
        initOrderStatus();
        initComplainType();
        initComplainStatus();
    }

    //初始化工单类型
    private void initOrderType(){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/orderType/pageByCondition");
        requestParam.setMethod(HttpMethod.Get);
        Map<String,String> map=new HashMap<>();
        map.put("pageNo","1");
        map.put("pageSize","100");
        requestParam.setGetRequestMap(map);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity<OrderTypeListEntity> baseEntity= JsonParse.parse(result,OrderTypeListEntity.class);
                if(baseEntity.isSuccess()){
                    SharedPreferencesManage.setOrderType(baseEntity.getResult().getData());
                    checkInitStatus(1);
                }else{
                    showToast(baseEntity.getMessage());
                    checkInitStatus(0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
                checkInitStatus(0);
            }
        });
        sendRequest(requestParam,false);
    }
    //初始化工单状态
    private void initOrderStatus(){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/orderStatus/selectWorkorderStatus");
        requestParam.setMethod(HttpMethod.Get);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity baseEntity=JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    Type type = new TypeToken<List<OrderStatusEntity>>() {}.getType();
                    List<OrderStatusEntity> list= (List<OrderStatusEntity>) JsonParse.parseList(result,type);
                    SharedPreferencesManage.setOrderStatus(list);
                    checkInitStatus(1);
                }else{
                    showToast(baseEntity.getMessage());
                    checkInitStatus(0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
                checkInitStatus(0);
            }
        });
        sendRequest(requestParam,false);
    }
    //初始化投诉类型
    private void initComplainType(){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/complaintType/pageByCondition");
        requestParam.setMethod(HttpMethod.Get);
        Map<String,String> map=new HashMap<>();
        map.put("pageNo","1");
        map.put("pageSize","100");
        requestParam.setGetRequestMap(map);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity<OrderTypeListEntity> baseEntity= JsonParse.parse(result,OrderTypeListEntity.class);
                if(baseEntity.isSuccess()){
                    SharedPreferencesManage.setComplainType(baseEntity.getResult().getData());
                    checkInitStatus(1);
                }else{
                    showToast(baseEntity.getMessage());
                    checkInitStatus(0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
                checkInitStatus(0);
            }
        });
        sendRequest(requestParam,false);
    }
    //初始化投诉状态
    private void initComplainStatus(){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/complaintStatus/complaintStatusList");
        requestParam.setMethod(HttpMethod.Get);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity baseEntity=JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    Type type = new TypeToken<List<OrderStatusEntity>>() {}.getType();
                    List<OrderStatusEntity> list= (List<OrderStatusEntity>) JsonParse.parseList(result,type);
                    SharedPreferencesManage.setComplainStatus(list);
                    checkInitStatus(1);
                }else{
                    showToast(baseEntity.getMessage());
                    checkInitStatus(0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
                checkInitStatus(0);
            }
        });
        sendRequest(requestParam,false);
    }

}
