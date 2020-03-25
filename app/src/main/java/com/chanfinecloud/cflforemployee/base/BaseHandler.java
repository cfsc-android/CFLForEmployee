package com.chanfinecloud.cflforemployee.base;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.chanfinecloud.cflforemployee.util.http.HttpMethod;
import com.chanfinecloud.cflforemployee.util.http.RequestParam;
import com.chanfinecloud.cflforemployee.util.http.XHttp;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loong on 2020/2/6.
 * Version: 1.0
 * Describe: Handler基类
 */
public class BaseHandler extends Handler {
    public final static int HTTP_LOGIN = 0x001;
    public final static int HTTP_REQUEST = 0x002;
    public final static int HTTP_CANCEL = 0x003;

    private List<org.xutils.common.Callback.Cancelable> taskList=new ArrayList<>();

    private final WeakReference<Activity> mActivity;

    public BaseHandler(Activity activity) {
        mActivity = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(final Message msg) {
        final Activity activity = mActivity.get();
        if (activity != null) {
            if(msg.what == HTTP_LOGIN){
                doRequest(msg);
            }else if(msg.what==HTTP_REQUEST){
                doRequest(msg);
            }else if(msg.what == HTTP_CANCEL){
                cancelRequest();
            }
        }
    }

    private void doRequest(Message msg){
        RequestParam requestParam= (RequestParam) msg.getData().getSerializable("request");
        if(requestParam!=null){
            HttpMethod httpMethod=requestParam.getMethod();
            switch (httpMethod){
                case Get:
                    taskList.add(XHttp.Get(requestParam.getUrl(),requestParam.getRequestMap(),requestParam.getCallback(),requestParam.isAuthorization()));
                    break;
                case Post:
                    taskList.add(XHttp.Post(requestParam.getUrl(),requestParam.getRequestMap(),requestParam.getCallback(),requestParam.getParamType(), requestParam.isAuthorization()));
                    break;
                case Put:
                    taskList.add(XHttp.Put(requestParam.getUrl(),requestParam.getRequestMap(),requestParam.getCallback(),requestParam.getParamType(),requestParam.isAuthorization()));
                    break;
                case Delete:
                    taskList.add(XHttp.Delete(requestParam.getUrl(),requestParam.getRequestMap(),requestParam.getCallback(),requestParam.getParamType(),requestParam.isAuthorization()));
                    break;
                case Download:
                    taskList.add(XHttp.DownLoadFile(requestParam.getUrl(),requestParam.getFilepath(),requestParam.getProgressCallback(),requestParam.isAuthorization()));
                    break;
                case Upload:
                    taskList.add(XHttp.UpLoadFile(requestParam.getUrl(),requestParam.getRequestMap(),requestParam.getCallback(),requestParam.isAuthorization()));
                    break;
            }
        }

    }

    private void cancelRequest(){
        for (int i = 0; i < taskList.size(); i++) {
            if(!taskList.get(i).isCancelled())
                taskList.get(i).cancel();
        }
    }
}