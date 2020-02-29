package com.chanfinecloud.cflforemployee.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.base.BaseActivity;
import com.chanfinecloud.cflforemployee.entity.UserInfoEntity;
import com.chanfinecloud.cflforemployee.util.SharedPreferencesManage;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_person)
public class PersonActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_tv_title)
    TextView toolbar_title;
    @ViewInject(R.id.person_tv_name)
    private TextView person_tv_name;
    @ViewInject(R.id.person_tv_depart)
    private TextView person_tv_depart;
    @ViewInject(R.id.person_tv_no)
    private TextView person_tv_no;
    @ViewInject(R.id.person_tv_gender)
    private TextView person_tv_gender;
    @ViewInject(R.id.person_tv_birth)
    private TextView person_tv_birth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar_title.setText("个人资料");
        UserInfoEntity userInfoEntity= SharedPreferencesManage.getUserInfo();
        person_tv_name.setText(userInfoEntity.getUser().getUsername());
        person_tv_depart.setText(userInfoEntity.getUser().getSysRoles().get(0).getRoleName());
        person_tv_no.setText("10000001");
        person_tv_gender.setText("未知");
        person_tv_birth.setText("1988-3-31");
    }

    @Event({R.id.toolbar_btn_back})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                finish();
            break;
        }
    }
}
