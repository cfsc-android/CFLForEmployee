package com.chanfinecloud.cflforemployee.ui;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.FileEntity;
import com.chanfinecloud.cflforemployee.entity.LoginEntity;
import com.chanfinecloud.cflforemployee.entity.ResourceEntity;
import com.chanfinecloud.cflforemployee.entity.TokenEntity;
import com.chanfinecloud.cflforemployee.entity.UserInfoEntity;
import com.chanfinecloud.cflforemployee.http.HttpMethod;
import com.chanfinecloud.cflforemployee.http.JsonParse;
import com.chanfinecloud.cflforemployee.http.MyCallBack;
import com.chanfinecloud.cflforemployee.http.RequestParam;
import com.chanfinecloud.cflforemployee.ui.base.BaseActivity;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.util.SharedPreferencesManage;
import com.chanfinecloud.cflforemployee.util.Utils;
import com.google.gson.Gson;
import com.pgyersdk.update.DownloadFileListener;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.update.javabean.AppBean;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

import static com.chanfinecloud.cflforemployee.config.Config.AUTH;
import static com.chanfinecloud.cflforemployee.config.Config.BASE_URL;
import static com.chanfinecloud.cflforemployee.config.Config.FILE;
import static com.chanfinecloud.cflforemployee.config.Config.USER;


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
        tv_loading_version.setText("v-"+ Utils.getCurrentVersion()+" : "+Utils.getCurrentBuild());
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
                        if(Utils.getCurrentVersion().equals(appBean.getVersionName())){
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
        TokenEntity token= SharedPreferencesManage.getToken();
        if(token!=null){
            LogUtils.d(token.getAccess_token());
            long time=new Date().getTime()/1000 - token.getInit_time();
            if(token.getExpires_in()-time>3){
                refreshToken();
            }else{
                startActivity(LoginActivity.class);
                finish();
            }
        }else{
            startActivity(LoginActivity.class);
            finish();
        }
    }

    private void refreshToken(){
        RequestParam requestParam=new RequestParam(BASE_URL+AUTH+"oauth/refresh/token",HttpMethod.Post);
        Map<String,Object> map=new HashMap<>();
        map.put("access_token",SharedPreferencesManage.getToken().getAccess_token());
        requestParam.setRequestMap(map);
        requestParam.setAuthorization(false);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                Gson gson = new Gson();
                TokenEntity token=gson.fromJson(result,TokenEntity.class);
                token.setInit_time(new Date().getTime()/1000);
                SharedPreferencesManage.setToken(token);
                getUserInfo();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                startActivity(LoginActivity.class);
            }
        });
        sendRequest(requestParam,false);
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo(){
        RequestParam requestParam=new RequestParam(BASE_URL+USER+"sys/user/users/current",HttpMethod.Get);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity<UserInfoEntity> baseEntity= JsonParse.parse(result,UserInfoEntity.class);
                if(baseEntity.isSuccess()){
                    SharedPreferencesManage.setUserInfo(baseEntity.getResult());//缓存用户信息
                    if(!TextUtils.isEmpty(baseEntity.getResult().getAvatarId())){
                        initAvatarResource();
                    }else{
                        startActivity(MainActivity.class);
                    }
                }else{
                    showToast(baseEntity.getMessage());
                    stopProgressDialog();
                    startActivity(LoginActivity.class);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                stopProgressDialog();
                startActivity(LoginActivity.class);
            }
        });
        sendRequest(requestParam,false);
    }

    /**
     * 缓存用户头像信息
     */
    private void initAvatarResource(){
        final UserInfoEntity userInfo=SharedPreferencesManage.getUserInfo();
        RequestParam requestParam=new RequestParam(BASE_URL+FILE+"files/byid/"+userInfo.getAvatarId(), HttpMethod.Get);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity<FileEntity> baseEntity= JsonParse.parse(result,FileEntity.class);
                if(baseEntity.isSuccess()){
                    ResourceEntity resourceEntity=new ResourceEntity();
                    resourceEntity.setId(baseEntity.getResult().getId());
                    resourceEntity.setContentType(baseEntity.getResult().getContentType());
                    resourceEntity.setCreateTime(baseEntity.getResult().getCreateTime());
                    resourceEntity.setName(baseEntity.getResult().getName());
                    resourceEntity.setUrl(baseEntity.getResult().getDomain()+baseEntity.getResult().getUrl());
                    userInfo.setAvatarResource(resourceEntity);
                    SharedPreferencesManage.setUserInfo(userInfo);//缓存用户信息
                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                startActivity(MainActivity.class);
            }
        });
        sendRequest(requestParam,false);
    }
}
