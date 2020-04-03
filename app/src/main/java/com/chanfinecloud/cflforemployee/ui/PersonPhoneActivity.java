package com.chanfinecloud.cflforemployee.ui;


import android.opengl.Visibility;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.ui.base.BaseActivity;
import com.chanfinecloud.cflforemployee.weidgt.photopicker.PhotoPicker;
import com.chanfinecloud.cflforemployee.weidgt.wheelview.BirthWheelDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;



@ContentView(R.layout.activity_person_phone)
public class PersonPhoneActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_tv_title)
    private TextView toolbar_title;
    @ViewInject(R.id.toolbar_tv_action)
    private TextView toolbar_tv_action;
    @ViewInject(R.id.toolbar_btn_action)
    private ImageButton toolbar_btn_action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar_title.setText("编辑电话");
        toolbar_btn_action.setVisibility(View.GONE);
        toolbar_tv_action.setVisibility(View.VISIBLE);
        toolbar_tv_action.setText("确定");
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
