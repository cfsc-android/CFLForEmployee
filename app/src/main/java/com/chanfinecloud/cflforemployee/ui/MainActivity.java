package com.chanfinecloud.cflforemployee.ui;


import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.base.BaseActivity;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.EventBusMessage;
import com.chanfinecloud.cflforemployee.entity.LoginEntity;
import com.chanfinecloud.cflforemployee.entity.OrderStatusEntity;
import com.chanfinecloud.cflforemployee.entity.OrderTypeListEntity;
import com.chanfinecloud.cflforemployee.entity.TokenEntity;
import com.chanfinecloud.cflforemployee.util.ExitAppUtils;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.util.SharedPreferencesManage;
import com.chanfinecloud.cflforemployee.util.http.HttpMethod;
import com.chanfinecloud.cflforemployee.util.http.JsonParse;
import com.chanfinecloud.cflforemployee.util.http.MyCallBack;
import com.chanfinecloud.cflforemployee.util.http.RequestParam;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.chanfinecloud.cflforemployee.base.Config.BASE_URL;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);
        initData();
        EventBus.getDefault().register(this);
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
                startActivity(LoginActivity.class);
                ExitAppUtils.getInstance().exit();
                break;
            case R.id.person_ll_logout_out:
                ExitAppUtils.getInstance().exit();
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
     * @return
     */
    private AlphaAnimation alphaAnimation(){
        AlphaAnimation animation=new AlphaAnimation(0,1);
        animation.setDuration(200);
        return animation;
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
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
                ExitAppUtils.getInstance().exit();
            }else{
                showToast("再按一次退出");
                time=new Date().getTime();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
