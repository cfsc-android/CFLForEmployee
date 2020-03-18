package com.chanfinecloud.cflforemployee.ui.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;

import static android.view.View.VISIBLE;

@ContentView(R.layout.activity_setting_detail)
public class SettingDetailActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_tv_title)
    private TextView toolbar_tv_title;
    @ViewInject(R.id.ll_setting_detail_notice)
    private LinearLayout ll_setting_detail_notice;
    @ViewInject(R.id.ll_setting_detail_cache)
    private LinearLayout ll_setting_detail_cache;
    @ViewInject(R.id.s_setting_detail_notice)
    private Switch s_setting_detail_notice;


    private static final String LOCAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "xiandao";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String type=getIntent().getExtras().getString("type");
        if("0".equals(type)){
            ll_setting_detail_notice.setVisibility(VISIBLE);
        }else if("1".equals(type)){
            ll_setting_detail_cache.setVisibility(VISIBLE);
        }
        toolbar_tv_title.setText(getIntent().getExtras().getString("title"));

        //暂时未接入极光推送，所以暂时未设置=================================
//        if("0".equals(u.getNoticeFlag())){
//            s_setting_detail_notice.setChecked(false);
//        }else{
//            s_setting_detail_notice.setChecked(true);
//        }
//        s_setting_detail_notice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Log.e("isChecked",isChecked+"");
//                if(isChecked){
//                   FileManagement.setNoticeFlag("1");
//                    JPushInterface.resumePush(getApplicationContext());
//                }else{
//                    //FileManagement.setNoticeFlag("0");
//                    //JPushInterface.stopPush(getApplicationContext());
//
//                }
//            }
//        });
    }

    @Event({R.id.toolbar_btn_back,R.id.ll_setting_detail_cache_cache,R.id.ll_setting_detail_cache_file})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                finish();
                break;
            case R.id.ll_setting_detail_cache_cache:
                String fileSize="0KB";
                try {
                   // fileSize=DataCleanManager.getCacheSize(getApplicationContext().getExternalCacheDir());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new AlertDialog.Builder(SettingDetailActivity.this)
                        .setTitle("清理缓存")
                        .setMessage("发现可清理缓存"+fileSize)
                        .setNegativeButton("确认清理",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //DataCleanManager.cleanExternalCache(getApplicationContext());
                            }
                        }).
                        setNeutralButton("暂不清理", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.ll_setting_detail_cache_file:
                new AlertDialog.Builder(SettingDetailActivity.this)
                        .setTitle("清理文件")
                        .setMessage("发现可清理文件"
                                //+FileSizeUtil.getAutoFileOrFilesSize(LOCAL_PATH)
                                )
                        .setNegativeButton("确认清理",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteFile(new File(LOCAL_PATH));
                                showToast("完成文件清理");
                            }
                        }).
                        setNeutralButton("暂不清理", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
        }
    }

    private void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            //file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }
}
