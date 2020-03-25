package com.chanfinecloud.cflforemployee.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.base.BaseActivity;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.LoginEntity;
import com.chanfinecloud.cflforemployee.entity.OrderStatusEntity;
import com.chanfinecloud.cflforemployee.entity.OrderTypeListEntity;
import com.chanfinecloud.cflforemployee.entity.TokenEntity;
import com.chanfinecloud.cflforemployee.entity.UserInfoAllEntity;
import com.chanfinecloud.cflforemployee.entity.UserInfoEntity;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.util.SharedPreferencesManage;
import com.chanfinecloud.cflforemployee.util.http.HttpMethod;
import com.chanfinecloud.cflforemployee.util.http.JsonParse;
import com.chanfinecloud.cflforemployee.util.http.MyCallBack;
import com.chanfinecloud.cflforemployee.util.http.RequestParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chanfinecloud.cflforemployee.base.Config.BASE_URL;


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

    private void login(){
        if(TextUtils.isEmpty(login_et_name.getText())){
            showToast("请填写用户名");
            return;
        }
        if(TextUtils.isEmpty(login_et_password.getText())){
            showToast("请填写密码");
            return;
        }

        RequestParam requestParam=new RequestParam(BASE_URL+"api-auth/oauth/token",HttpMethod.Post);
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

                initData();

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


    private List<Integer> initStatus=new ArrayList<>();

    private void checkInitStatus(int status){
        initStatus.add(status);
        if(initStatus.indexOf(0)==-1&&initStatus.size()==5){
            startActivity(MainActivity.class);
            finish();
        }
    }

    private void initData(){
        initOrderType();
        initOrderStatus();
        initComplainType();
        initComplainStatus();
        getUserInfo();
    }

    //初始化工单类型
    private void initOrderType(){
        RequestParam requestParam=new RequestParam(BASE_URL+"work/orderType/pageByCondition",HttpMethod.Get);
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
        RequestParam requestParam=new RequestParam(BASE_URL+"work/orderStatus/selectWorkorderStatus",HttpMethod.Get);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                Type type = new TypeToken<List<OrderStatusEntity>>() {}.getType();
                BaseEntity<List<OrderStatusEntity>> baseEntity=JsonParse.parse(result,type);
                if(baseEntity.isSuccess()){
                    SharedPreferencesManage.setOrderStatus(baseEntity.getResult());
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
        RequestParam requestParam=new RequestParam(BASE_URL+"work/complaintType/pageByCondition",HttpMethod.Get);
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
        RequestParam requestParam=new RequestParam(BASE_URL+"work/complaintStatus/complaintStatusList",HttpMethod.Get);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                Type type = new TypeToken<List<OrderStatusEntity>>() {}.getType();
                BaseEntity<List<OrderStatusEntity>> baseEntity=JsonParse.parse(result,type);
                if(baseEntity.isSuccess()){
                    SharedPreferencesManage.setComplainStatus(baseEntity.getResult());
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
    //获取用户信息
    private void getUserInfo(){
        RequestParam requestParam=new RequestParam(BASE_URL+"api-auth/oauth/userinfo",HttpMethod.Get);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                Gson gson = new Gson();
                UserInfoAllEntity userInfoAllEntity=gson.fromJson(result, UserInfoAllEntity.class);
                getUserInfo(userInfoAllEntity.getUser().getId());
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

    //获取用户信息
    private void getUserInfo(String userId){
        RequestParam requestParam=new RequestParam(BASE_URL+"sys/user/"+userId,HttpMethod.Get);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity<UserInfoEntity> baseEntity=JsonParse.parse(result,UserInfoEntity.class);
                if(baseEntity.isSuccess()){
                    SharedPreferencesManage.setUserInfo(baseEntity.getResult());
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
