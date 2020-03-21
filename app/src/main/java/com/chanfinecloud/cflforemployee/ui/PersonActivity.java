package com.chanfinecloud.cflforemployee.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.base.BaseActivity;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.EventBusMessage;
import com.chanfinecloud.cflforemployee.entity.FileEntity;
import com.chanfinecloud.cflforemployee.entity.UserInfoAllEntity;
import com.chanfinecloud.cflforemployee.entity.UserInfoEntity;
import com.chanfinecloud.cflforemployee.util.FilePathUtil;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.util.SharedPreferencesManage;
import com.chanfinecloud.cflforemployee.util.http.HttpMethod;
import com.chanfinecloud.cflforemployee.util.http.JsonParse;
import com.chanfinecloud.cflforemployee.util.http.MyCallBack;
import com.chanfinecloud.cflforemployee.util.http.RequestParam;
import com.chanfinecloud.cflforemployee.weidgt.photopicker.PhotoPicker;
import com.chanfinecloud.cflforemployee.weidgt.wheelview.BirthWheelDialog;
import com.google.gson.Gson;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.chanfinecloud.cflforemployee.base.Config.BASE_URL;
import static com.chanfinecloud.cflforemployee.base.Config.PHOTO_DIR_NAME;
import static com.chanfinecloud.cflforemployee.base.Config.SD_APP_DIR_NAME;

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

    public static final int REQUEST_CODE_CHOOSE=0x001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkAppPermission();
        super.onCreate(savedInstanceState);
        toolbar_title.setText("个人资料");
        initView();
    }

    private void initView(){
        userInfoEntity = SharedPreferencesManage.getUserInfo();
        person_tv_name.setText(userInfoEntity.getUsername());
        person_tv_depart.setText(userInfoEntity.getDepartment());
        person_tv_no.setText(userInfoEntity.getWorkNo());
        sex =userInfoEntity.getGender();
        if (sex == 0){
            person_tv_gender.setText("男");
        }else if (sex == 1){
            person_tv_gender.setText("女");
        }else{
            person_tv_gender.setText("未知");
        }
        birthday = userInfoEntity.getBirthday();
        if(!TextUtils.isEmpty(birthday)){
            person_tv_birth.setText(birthday.substring(0,10));
        }else{
            person_tv_birth.setText("请选择出生日期");
        }
        if(!TextUtils.isEmpty(userInfoEntity.getAvatarId())){
            Glide.with(this)
                    .load(userInfoEntity.getAvatarResuorce().getUrl())
                    .circleCrop()
                    .into(person_iv_avatar);

        }
    }

    @Event({R.id.toolbar_btn_back,R.id.person_ll_gender,R.id.person_ll_birth,R.id.person_iv_avatar})
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
                        Map<String,Object> map=new HashMap<>();
                        map.put("birthday",dateText+" 00:00:00");
                        updateUser(map);
                        wheelDialog.cancel();
                    }
                });
                wheelDialog.show();
                if(!TextUtils.isEmpty(birthday)){
                    wheelDialog.setBirth(userInfoEntity.getBirthday());
                }else{
                    wheelDialog.setBirth("2000-1-1");
                }
                break;
            case R.id.person_iv_avatar:
                if(permission){
                    PhotoPicker.pick(PersonActivity.this,1,true,REQUEST_CODE_CHOOSE);
                }else{
                    showToast("相机或读写手机存储的权限被禁止！");
                }
                break;
        }
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
                sex = which;
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String,Object> map=new HashMap<>();
                map.put("gender",sex);
                updateUser(map);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();  //创建AlertDialog对象
        dialog.show();
    }

    /**
     * 更新用户信息
     * @param map
     */
    private void updateUser(Map<String,Object> map){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"/sys/user/updateUser");
        requestParam.setMethod(HttpMethod.Put);
        map.put("id", userInfoEntity.getId());
        requestParam.setPostJsonRequest(map);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    getUserInfo();
                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
                stopProgressDialog();
            }
        });
        sendRequest(requestParam,true);
    }


    //获取用户信息
    private void getUserInfo(){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"sys/user/"+SharedPreferencesManage.getUserInfo().getId());
        requestParam.setMethod(HttpMethod.Get);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity<UserInfoEntity> baseEntity=JsonParse.parse(result,UserInfoEntity.class);
                if(baseEntity.isSuccess()){
                    SharedPreferencesManage.setUserInfo(baseEntity.getResult());
                    initView();
                    EventBus.getDefault().post(new EventBusMessage<>("refresh"));
                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }

            @Override
            public void onFinished() {
                super.onFinished();
                stopProgressDialog();
            }
        });
        sendRequest(requestParam,false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_CHOOSE&&resultCode==RESULT_OK){
            //图片路径 同样视频地址也是这个 根据requestCode
            List<Uri> pathList = Matisse.obtainResult(data);
            List<String> _List = new ArrayList<>();
            for (Uri _Uri : pathList)
            {
                String _Path = FilePathUtil.getPathByUri(this,_Uri);
                File _File = new File(_Path);
                LogUtil.d("压缩前图片大小->" + _File.length() / 1024 + "k");
                _List.add(_Path);
            }
            compress(_List);
        }
    }

    //压缩图片
    private void compress(List<String> list){
        String _Path = FilePathUtil.createPathIfNotExist("/" + SD_APP_DIR_NAME + "/" + PHOTO_DIR_NAME);
        LogUtil.d("_Path->" + _Path);
        Luban.with(this)
                .load(list)
                .ignoreBy(100)
                .setTargetDir(_Path)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        LogUtil.d(" 压缩开始前调用，可以在方法内启动 loading UI");
                    }

                    @Override
                    public void onSuccess(File file) {
                        LogUtil.d(" 压缩成功后调用，返回压缩后的图片文件");
                        LogUtil.d("压缩后图片大小->" + file.length() / 1024 + "k");
                        LogUtil.d("getAbsolutePath->" + file.getAbsolutePath());
                        uploadPic(file.getAbsolutePath());
//                        mUploadPic(file.getAbsolutePath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                }).launch();
    }

    //上传照片
    private void uploadPic(final String path){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"file-manager-ms/files-anon");
        requestParam.setMethod(HttpMethod.Upload);
        Map<String,Object> map=new HashMap<>();
        map.put("UploadFile",new File(path));
        requestParam.setPostRequestMap(map);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<FileEntity> baseEntity= JsonParse.parse(result,FileEntity.class);
                if(baseEntity.isSuccess()){
                    Map<String,Object> map=new HashMap<>();
                    map.put("avatarId",baseEntity.getResult().getId());
                    updateUser(map);
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

}
