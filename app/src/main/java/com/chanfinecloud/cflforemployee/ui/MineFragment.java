package com.chanfinecloud.cflforemployee.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.base.BaseFragment;
import com.chanfinecloud.cflforemployee.entity.UserInfoEntity;
import com.chanfinecloud.cflforemployee.ui.setting.SettingActivity;
import com.chanfinecloud.cflforemployee.util.SharedPreferencesManage;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import androidx.annotation.Nullable;

/**
 * Created by Loong on 2020/2/12.
 * Version: 1.0
 * Describe: 我的
 */
@ContentView(R.layout.fragment_mine)
public class MineFragment extends BaseFragment {
    @ViewInject(R.id.mine_iv_user_avatar)
    private ImageView mine_iv_user_avatar;
    @ViewInject(R.id.mine_tv_user_name)
    private TextView mine_tv_user_name;
    @ViewInject(R.id.mine_tv_user_depart)
    private TextView mine_tv_user_depart;


    private UserInfoEntity userInfo;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userInfo= SharedPreferencesManage.getUserInfo();
        mine_tv_user_name.setText(userInfo.getUser().getUsername());
        mine_tv_user_depart.setText(userInfo.getUser().getDepartment());
        if(!TextUtils.isEmpty(userInfo.getUser().getAvatarUrl())){
            Glide.with(this)
                    .load(userInfo.getUser().getAvatarUrl())
                    .circleCrop()
                    .into(mine_iv_user_avatar);
        }
    }


    @Event({R.id.mine_ll_user,R.id.mine_tv_person,R.id.mine_tv_sign,R.id.mine_tv_setting})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.mine_ll_user:
                startActivity(PersonActivity.class);
                break;
            case R.id.mine_tv_person:
                startActivity(PersonActivity.class);
                break;
            case R.id.mine_tv_sign:
                startActivity(AttendanceActivity.class);
                break;
            case R.id.mine_tv_setting:
                //EventBus.getDefault().post(new EventBusMessage<>("Quit"));
                startActivity(SettingActivity.class);
                break;

        }
    }


}
