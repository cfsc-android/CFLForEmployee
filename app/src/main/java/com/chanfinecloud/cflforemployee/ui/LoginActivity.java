package com.chanfinecloud.cflforemployee.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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
import com.google.gson.Gson;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.chanfinecloud.cflforemployee.config.Config.AUTH;
import static com.chanfinecloud.cflforemployee.config.Config.BASE_URL;
import static com.chanfinecloud.cflforemployee.config.Config.FILE;
import static com.chanfinecloud.cflforemployee.config.Config.USER;


/**
 * Created by Loong on 2020/2/10.
 * Version: 1.0
 * Describe: 登录页
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.login_et_name)
    private TextView login_et_name;
    @ViewInject(R.id.login_et_password)
    private TextView login_et_password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 登录
     */
    private void login(){
        if(TextUtils.isEmpty(login_et_name.getText())){
            showToast("请填写用户名");
            return;
        }
        if(TextUtils.isEmpty(login_et_password.getText())){
            showToast("请填写密码");
            return;
        }

        RequestParam requestParam=new RequestParam(BASE_URL+AUTH+"oauth/token",HttpMethod.Post);
        Map<String,Object> params=new HashMap<>();
        params.put("username",login_et_name.getText().toString());
        params.put("password",login_et_password.getText().toString());
//        params.put("client_id","mobile");
//        params.put("client_secret","mobile");
        params.put("client_id","webApp");
        params.put("client_secret","webApp");
        params.put("grant_type","password");
        params.put("validCode","b2bd");

        requestParam.setRequestMap(params);
        requestParam.setAuthorization(false);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);

                SharedPreferencesManage.setLoginInfo(new LoginEntity(login_et_name.getText().toString(),login_et_password.getText().toString()));
                Gson gson = new Gson();
                TokenEntity token=gson.fromJson(result,TokenEntity.class);
                token.setInit_time(new Date().getTime()/1000);
                SharedPreferencesManage.setToken(token);
                getUserInfo();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
                stopProgressDialog();
            }
        });
        sendRequest(requestParam,true);
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
                Gson gson = new Gson();
                UserInfoEntity userInfo=gson.fromJson(result, UserInfoEntity.class);
                SharedPreferencesManage.setUserInfo(userInfo);//缓存用户信息
                if(!TextUtils.isEmpty(userInfo.getAvatarId())){
                    initAvatarResource();
                }else{
                    startActivity(MainActivity.class);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
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
                stopProgressDialog();
                startActivity(MainActivity.class);
            }
        });
        sendRequest(requestParam,false);
    }

    /**
     * 忘记密码dialog
     */
    private void forgetPassword(){
        new AlertDialog.Builder(this)
                .setTitle("忘记密码了？")
                .setMessage("请联系系统管理员，在后台重置为员工默认密码，首次登录后请更改密码")
                .setCancelable(true)
                .setNegativeButton(
                        "我知道了",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    @Event({R.id.login_btn_login,R.id.login_tv_forget_password})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.login_btn_login:
                login();
                break;
            case R.id.login_tv_forget_password:
                forgetPassword();
                break;
        }
    }
}
