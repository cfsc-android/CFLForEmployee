package com.chanfinecloud.cflforemployee.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.base.BaseActivity;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.UserInfoEntity;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.util.SharedPreferencesManage;
import com.chanfinecloud.cflforemployee.util.http.HttpMethod;
import com.chanfinecloud.cflforemployee.util.http.JsonParse;
import com.chanfinecloud.cflforemployee.util.http.MyCallBack;
import com.chanfinecloud.cflforemployee.util.http.RequestParam;
import com.chanfinecloud.cflforemployee.weidgt.wheelview.BirthWheelDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

import static com.chanfinecloud.cflforemployee.base.Config.BASE_URL;

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
    @ViewInject(R.id.person_iv_avatar)
    private ImageView person_iv_avatar;
    private BirthWheelDialog wheelDialog;

    private int sex;
    private String birthday;

    private UserInfoEntity userInfoEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar_title.setText("个人资料");
        userInfoEntity= SharedPreferencesManage.getUserInfo();
        person_tv_name.setText(userInfoEntity.getUser().getUsername());
        person_tv_depart.setText(userInfoEntity.getUser().getDepartment());
        person_tv_no.setText(userInfoEntity.getUser().getWorkNo());
        sex = userInfoEntity.getUser().getSex();
        if (sex == 0){
            person_tv_gender.setText("男");
        }else if (sex == 1){
            person_tv_gender.setText("女");
        }else{
            person_tv_gender.setText("未知");
        }
        birthday = userInfoEntity.getUser().getBirthday();
        if(!TextUtils.isEmpty(birthday)){
            person_tv_birth.setText(birthday.substring(0,10));
        }else{
            person_tv_birth.setText("请填写出生日期");
        }
        if(!TextUtils.isEmpty(userInfoEntity.getUser().getAvatarUrl())){
            Glide.with(this)
                    .load(userInfoEntity.getUser().getAvatarUrl())
                    .circleCrop()
                    .into(person_iv_avatar);

        }
    }

    @Event({R.id.toolbar_btn_back,R.id.person_ll_gender,R.id.person_ll_birth})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                finish();
                break;
            case R.id.person_ll_gender:
                singleChoiceSex();
                break;
            case R.id.person_ll_birth:
                wheelDialog = new BirthWheelDialog(this, R.style.Dialog_Floating, new BirthWheelDialog.OnDateTimeConfirm() {
                    @Override
                    public void returnData(String dateText, String dateValue) {
                        wheelDialog.cancel();
                        reloadBirthday(dateText);
                    }
                });
                wheelDialog.show();
                if(!TextUtils.isEmpty(birthday)){
                    wheelDialog.setBirth(userInfoEntity.getUser().getBirthday());
                }else{
                    wheelDialog.setBirth("2000-1-1");
                }
                break;

        }
    }

    /**
     * 更新用户生日
     * @param date
     */
    private void reloadBirthday(final String date){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"/sys/user/updateUser");
        requestParam.setMethod(HttpMethod.Put);
        Map<String,Object> map=new HashMap<>();
        map.put("id",userInfoEntity.getUser().getId());
        map.put("birthday",date+" 00:00:00");
        requestParam.setPostJsonRequest(map);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    userInfoEntity.getUser().setBirthday(date+" 00:00:00");
                    SharedPreferencesManage.setUserInfo(userInfoEntity);
                    person_tv_birth.setText(date);
                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                sex = userInfoEntity.getUser().getSex();
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }
        });
        sendRequest(requestParam,false);
    }

    /**
     * 选择男女
     */
    private void singleChoiceSex() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonActivity.this);
        builder.setTitle("请选择性别：");
        final String[] cities = {"男", "女"};
        int checkedItem;
        if (sex == 0){
            checkedItem = 0;
        }else if (sex == 1){
            checkedItem = 1;
        }else{
            checkedItem = -1;
        }

        builder.setSingleChoiceItems(cities, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "你选择了" + cities[which], Toast.LENGTH_SHORT).show();
                sex = which;
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reloadSex();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sex = userInfoEntity.getUser().getSex();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();  //创建AlertDialog对象
        dialog.show();
    }

    /**
     * 更新用户性别
     */
    private void reloadSex(){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"/sys/user/updateUser");
        requestParam.setMethod(HttpMethod.Put);
        Map<String,Object> map=new HashMap<>();
        map.put("id",userInfoEntity.getUser().getId());
        map.put("sex",sex);
        requestParam.setPostJsonRequest(map);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    userInfoEntity.getUser().setSex(sex);
                    SharedPreferencesManage.setUserInfo(userInfoEntity);
                    if (sex == 0){
                        person_tv_gender.setText("男");
                    }else if (sex == 1){
                        person_tv_gender.setText("女");
                    }else{
                        person_tv_gender.setText("未知");
                    }
                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                sex = userInfoEntity.getUser().getSex();
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }
        });
        sendRequest(requestParam,false);
    }

}
