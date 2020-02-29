package com.chanfinecloud.cflforemployee.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;


@ContentView(R.layout.activity_attendance)
public class AttendanceActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_tv_title)
    TextView toolbar_title;
    @ViewInject(R.id.attendance_iv)
    private ImageView attendance_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar_title.setText("考勤");
        Glide.with(this)
                .load(R.drawable.attendance)
                .into(attendance_iv);
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
