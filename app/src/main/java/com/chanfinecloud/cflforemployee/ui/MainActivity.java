package com.chanfinecloud.cflforemployee.ui;


import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.ui.base.BaseActivity;
import com.chanfinecloud.cflforemployee.entity.EventBusMessage;
import com.chanfinecloud.cflforemployee.entity.LoginEntity;
import com.chanfinecloud.cflforemployee.entity.TokenEntity;
import com.chanfinecloud.cflforemployee.util.LynActivityManager;
import com.chanfinecloud.cflforemployee.util.SharedPreferencesManage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cn.jpush.android.api.JPushInterface;


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
        checkAppPermission();
        super.onCreate(savedInstanceState);
        context=this;
        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);
        EventBus.getDefault().register(this);
        setAliasAndTag();
    }


    /**
     * 设置极光推送的alias（别名）和tag(标签)
     */
    private void setAliasAndTag(){
        JPushInterface.setAlias(this,0x01,"KSS");
        Set<String> tagSet = new LinkedHashSet<>();
        tagSet.add("YG");
        tagSet.add("CFJT");
        JPushInterface.setTags(this,0x02,tagSet);
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
                LynActivityManager.getInstance().exit();
            }else{
                showToast("再按一次退出");
                time=new Date().getTime();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
