package com.chanfinecloud.cflforemployee.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;


import com.chanfinecloud.cflforemployee.ui.LoginActivity;
import com.chanfinecloud.cflforemployee.util.ExitAppUtils;
import com.chanfinecloud.cflforemployee.util.TokenUtils;
import com.chanfinecloud.cflforemployee.util.http.RequestParam;
import com.chanfinecloud.cflforemployee.util.http.XHttp;

import org.xutils.common.Callback;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Loong on 2020/2/6.
 * Version: 1.0
 * Describe: Handler基类
 */
public class BaseHandler extends Handler {
    public final static int HTTP_LOGIN = 0x001;
    public final static int HTTP_REQUEST = 0x002;
    private final WeakReference<Activity> mActivity;
    private List<org.xutils.common.Callback.Cancelable> taskList;

    public BaseHandler(Activity activity, List<org.xutils.common.Callback.Cancelable> taskList) {
        mActivity = new WeakReference<>(activity);
        this.taskList = taskList;
    }

    @Override
    public void handleMessage(final Message msg) {
        final Activity activity = mActivity.get();
        if (activity != null) {
            if(msg.what == HTTP_LOGIN){
                doRequest(msg);
            }else if(msg.what==HTTP_REQUEST){
                doRequest(msg);
            }
        }
    }

    private void doRequest(Message msg){
        RequestParam requestParam= (RequestParam) msg.getData().getSerializable("request");
        switch (requestParam.getMethod()){
            case Get:
                taskList.add(XHttp.Get(requestParam.getUrl(),requestParam.getGetRequestMap(),requestParam.getCallback()));
                break;
            case Post:
                taskList.add(XHttp.Post(requestParam.getUrl(),requestParam.getPostRequestMap(),requestParam.getCallback()));
                break;
            case PostJson:
                taskList.add(XHttp.PostJson(requestParam.getUrl(),requestParam.getPostJsonRequest(),requestParam.getCallback()));
                break;
            case Download:
                taskList.add(XHttp.DownLoadFile(requestParam.getUrl(),requestParam.getFilepath(),requestParam.getProgressCallback()));
                break;
            case Upload:
                taskList.add(XHttp.UpLoadFile(requestParam.getUrl(),requestParam.getPostRequestMap(),requestParam.getCallback()));
                break;
            case Put:
                taskList.add(XHttp.Put(requestParam.getUrl(),requestParam.getPostJsonRequest(),requestParam.getCallback()));
                break;
        }
    }
    private void alertLogin(final Activity activity){
        new AlertDialog.Builder(activity)
                .setTitle("重新登录")
                .setMessage("您的登录信息已过期，请重新登录！")
                .setCancelable(false)
                .setNegativeButton(
                        "去登录",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.startActivity(new Intent(activity, LoginActivity.class));
                                ExitAppUtils.getInstance().exit();
                            }
                        }).show();
    }
    private void checkRequest(TokenUtils.RefreshTokenListener listener){
        final Activity activity = mActivity.get();
        if(!TokenUtils.isTokenValid()) {
//            if (TokenUtils.isLoginInfoExist()) {
//                TokenUtils.refreshToken(listener);
//            }else{
//                alertLogin(activity);
//            }
        }else{
            listener.success();
        }
    }
}