package com.chanfinecloud.cflforemployee.ui.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.ui.base.BaseActivity;
import com.chanfinecloud.cflforemployee.util.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;


@ContentView(R.layout.activity_setting_about)
public class SettingAboutActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_tv_title)
    private TextView toolbar_tv_title;
    @ViewInject(R.id.tv_setting_about_version)
    private TextView tv_setting_about_version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar_tv_title.setText("关于长房里");
        tv_setting_about_version.setText("Version "+ Utils.getCurrentVersion());
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
