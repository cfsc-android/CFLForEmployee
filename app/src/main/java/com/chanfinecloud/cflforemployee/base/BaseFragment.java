package com.chanfinecloud.cflforemployee.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chanfinecloud.cflforemployee.base.BaseActivity;
import com.chanfinecloud.cflforemployee.base.BaseHandler;
import com.chanfinecloud.cflforemployee.entity.Transition;
import com.chanfinecloud.cflforemployee.util.AtyTransitionUtil;
import com.chanfinecloud.cflforemployee.util.http.RequestParam;
import com.chanfinecloud.cflforemployee.weidgt.ProgressDialogView;

import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

import static com.chanfinecloud.cflforemployee.CFLApplication.activityTrans;
import static com.chanfinecloud.cflforemployee.base.BaseHandler.HTTP_REQUEST;

/**
 * Created by Loong on 2020/2/12.
 * Version: 1.0
 * Describe: Fragment基础类
 */
public class BaseFragment extends Fragment {

    private Context context;// 上下文
    private boolean injected = false;
    private ProgressDialogView progressDialogView = null;
    private static List<Callback.Cancelable> taskList;
    protected static BaseHandler handler;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        taskList=new ArrayList<>();
        handler=new BaseHandler(getActivity(),taskList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialogView != null) {
            progressDialogView.stopLoad();
            progressDialogView = null;
        }
        for (int i = 0; i < taskList.size(); i++) {
            if(!taskList.get(i).isCancelled())
                taskList.get(i).cancel();
        }
    }

    protected void sendRequest(RequestParam requestParam, boolean showProgressDialog){
        if(((BaseActivity)getActivity()).isNetConnect){
            if(showProgressDialog){
                startProgressDialog();
            }
            Message message=new Message();
            message.what=HTTP_REQUEST;
            Bundle bundle=new Bundle();
            bundle.putSerializable("request",requestParam);
            message.setData(bundle);
            handler.sendMessage(message);
        }else{
            showToast("没有网络，请前往网络设置检查");
        }
    }

    /**
     * 启动新的Activity 默认Trans-RightIn
     * @param clazz
     */
    public void startActivity(Class clazz){
        Transition transition= Transition.RightIn;
        activityTrans.put(clazz,transition);
        startActivity(new Intent(getActivity(), clazz));
        executeTransition(transition);
    }

    /**
     * 启动新的Activity 默认Trans-RightIn
     * @param clazz
     * @param bundle
     */
    public void startActivity(Class clazz,Bundle bundle){
        Transition transition=Transition.RightIn;
        activityTrans.put(clazz,transition);
        Intent intent = new Intent();
        intent.setClass(getActivity(), clazz);
        intent.putExtras(bundle);
        startActivity(intent);
        executeTransition(transition);
    }

    /**
     * 启动新的Activity
     * @param clazz
     * @param transition  转场动画
     */
    public void startActivity(Class clazz, Transition transition){
        activityTrans.put(clazz,transition);
        startActivity(new Intent(getActivity(), clazz));
        executeTransition(transition);
    }

    /**
     * 启动新的Activity
     * @param clazz
     * @param bundle
     * @param transition 转场动画
     */
    public void startActivity(Class clazz, Bundle bundle,Transition transition){
        activityTrans.put(clazz,transition);
        Intent intent = new Intent();
        intent.setClass(getActivity(), clazz);
        intent.putExtras(bundle);
        startActivity(intent);
        executeTransition(transition);
    }

    /**
     * 启动新的Activity
     * @param clazz
     * @param bundle
     * @param requestCode
     * @param transition 转场动画
     */
    public void startActivityForResult( Class clazz, Bundle bundle, int requestCode,Transition transition) {
        activityTrans.put(clazz,transition);
        Intent intent = new Intent();
        intent.setClass(getActivity(), clazz);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
        executeTransition(transition);
    }

    //执行转场
    private void executeTransition(Transition transition){
        switch (transition){
            case TopIn:
                AtyTransitionUtil.enterFromTop(getActivity());
                break;
            case TopOut:
                AtyTransitionUtil.exitToTop(getActivity());
                break;
            case LeftIn:
                AtyTransitionUtil.enterFromLeft(getActivity());
                break;
            case LeftOut:
                AtyTransitionUtil.exitToLeft(getActivity());
                break;
            case BottomIn:
                AtyTransitionUtil.enterFromBottom(getActivity());
                break;
            case BottomOut:
                AtyTransitionUtil.exitToBottom(getActivity());
                break;
            case RightIn:
                AtyTransitionUtil.enterFromRight(getActivity());
                break;
            case RightOut:
                AtyTransitionUtil.exitToRight(getActivity());
                break;
        }
    }


    //启动加载框
    protected void startProgressDialog() {
        if (progressDialogView == null) {
            progressDialogView = new ProgressDialogView();
        }
        progressDialogView.startLoad(context, "",false);
    }
    //启动加载框
    protected void startProgressDialog(String msg) {
        if (progressDialogView == null) {
            progressDialogView = new ProgressDialogView();
        }
        progressDialogView.startLoad(context, msg,false);
    }
    //启动加载框
    protected void startProgressDialog(boolean cancelable) {
        if (progressDialogView == null) {
            progressDialogView = new ProgressDialogView();
        }
        progressDialogView.startLoad(context, "",cancelable);
    }
    //启动加载框
    protected void startProgressDialog(String msg,boolean cancelable) {
        if (progressDialogView == null) {
            progressDialogView = new ProgressDialogView();
        }
        progressDialogView.startLoad(context, msg,cancelable);
    }
    //关闭加载框
    protected void stopProgressDialog() {
        if (progressDialogView != null) {
            progressDialogView.stopLoad();
        }
    }
    //显示Toast
    protected void showToast(String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
}