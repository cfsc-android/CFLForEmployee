package com.chanfinecloud.cflforemployee.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.ui.base.BaseActivity;
import com.chanfinecloud.cflforemployee.entity.OrderTypeEntity;
import com.chanfinecloud.cflforemployee.util.FilePathUtil;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.util.SharedPreferencesManage;
import com.chanfinecloud.cflforemployee.http.HttpMethod;
import com.chanfinecloud.cflforemployee.http.MyCallBack;
import com.chanfinecloud.cflforemployee.http.ParamType;
import com.chanfinecloud.cflforemployee.http.RequestParam;
import com.chanfinecloud.cflforemployee.weidgt.imagepreview.ImagePreviewListAdapter;
import com.chanfinecloud.cflforemployee.weidgt.imagepreview.ImageViewInfo;
import com.chanfinecloud.cflforemployee.weidgt.imagepreview.PreviewBuilder;
import com.chanfinecloud.cflforemployee.weidgt.photopicker.PhotoPicker;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter;
import com.zhihu.matisse.Matisse;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.chanfinecloud.cflforemployee.config.Config.BASE_URL;
import static com.chanfinecloud.cflforemployee.config.Config.FILE;
import static com.chanfinecloud.cflforemployee.config.Config.PHOTO_DIR_NAME;
import static com.chanfinecloud.cflforemployee.config.Config.SD_APP_DIR_NAME;
import static com.chanfinecloud.cflforemployee.config.Config.WORKORDER;

@ContentView(R.layout.activity_add_order)
public class AddOrderActivity extends BaseActivity {
    private static final int REQUEST_CODE_CHOOSE=0x001;

    @ViewInject(R.id.toolbar_tv_title)
    TextView toolbar_title;
    @ViewInject(R.id.add_order_et_address)
    private EditText add_order_et_address;
    @ViewInject(R.id.add_order_et_contact)
    private EditText add_order_et_contact;
    @ViewInject(R.id.add_order_et_contact_tel)
    private EditText add_order_et_contact_tel;
    @ViewInject(R.id.add_order_et_remark)
    private EditText add_order_et_remark;
    @ViewInject(R.id.add_order_rlv_pic)
    private RecyclerView add_order_rlv_pic;
    @ViewInject(R.id.add_order_ms_project_type)
    private MaterialSpinner add_order_ms_project_type;
    @ViewInject(R.id.add_order_ms_problem_type)
    private MaterialSpinner add_order_ms_problem_type;

    private List<ImageViewInfo> dataList=new ArrayList<>();
    private GridLayoutManager mGridLayoutManager;
    private ImagePreviewListAdapter adapter;
    private List<OrderTypeEntity> orderTypeEntityList;
    private List<String> problemData=new ArrayList<>();
    private List<OrderTypeEntity> problemTypeData=new ArrayList<>();
    private List<String> projectData=new ArrayList<>();
    private List<OrderTypeEntity> projectTypeData=new ArrayList<>();
    private int problemValue,projectValue;
    private String resourceKey;
    private MaterialSpinnerAdapter projectDataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkAppPermission();
        super.onCreate(savedInstanceState);
        toolbar_title.setText("创建工单");
        //默认不弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dataList.add(new ImageViewInfo("plus"));
        add_order_rlv_pic.setLayoutManager(mGridLayoutManager = new GridLayoutManager(this,4));
        adapter=new ImagePreviewListAdapter(this,R.layout.item_workflow_image_perview_list,dataList);
        add_order_rlv_pic.setAdapter(adapter);
        add_order_rlv_pic.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(position==dataList.size()-1){
                    if(permission){
                        PhotoPicker.pick(AddOrderActivity.this,1,true,REQUEST_CODE_CHOOSE);
                    }else{
                        showToast("相机或读写手机存储的权限被禁止！");
                    }
                }else{
                    List<ImageViewInfo> data=new ArrayList<>();
                    for (int i = 0; i < dataList.size()-1; i++) {
                        data.add(dataList.get(i));
                    }
                    computeBoundsBackward(mGridLayoutManager.findFirstVisibleItemPosition());
                    PreviewBuilder.from(AddOrderActivity.this)
                            .setImgs(data)
                            .setCurrentIndex(position)
                            .setSingleFling(true)
                            .setType(PreviewBuilder.IndicatorType.Number)
                            .start();
                }
            }
        });

        orderTypeEntityList= SharedPreferencesManage.getOrderType();
        initProblemSpinner();
        resourceKey=UUID.randomUUID().toString().replaceAll("-","");
    }

    private void initProblemSpinner(){
        for (int i = 0; i < orderTypeEntityList.size(); i++) {
            if(orderTypeEntityList.get(i).getParentId()==0){
                problemData.add(orderTypeEntityList.get(i).getName());
                problemTypeData.add(orderTypeEntityList.get(i));
            }
        }
        problemValue=problemTypeData.get(0).getId();
        add_order_ms_problem_type.setItems(problemData);
        add_order_ms_problem_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                LogUtils.d(position+"_"+id+"_"+item.toString());
                problemValue=problemTypeData.get(position).getId();
                initProjectSpinner();
            }
        });
        initProjectSpinner();
    }

    private void initProjectSpinner(){
        projectData.clear();
        projectTypeData.clear();
        for (int i = 0; i < orderTypeEntityList.size(); i++) {
            if(orderTypeEntityList.get(i).getParentId()==problemValue){
                projectData.add(orderTypeEntityList.get(i).getName());
                projectTypeData.add(orderTypeEntityList.get(i));
                projectValue=orderTypeEntityList.get(i).getId();
            }
        }
        projectValue=projectTypeData.get(0).getId();
        if(projectDataAdapter==null){
            projectDataAdapter=new MaterialSpinnerAdapter(this,projectData);
            add_order_ms_project_type.setAdapter(projectDataAdapter);
            add_order_ms_project_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    LogUtils.d(position+"_"+id+"_"+item.toString());
                    projectValue=projectTypeData.get(position).getId();
                }
            });
        }else{
            projectDataAdapter.notifyDataSetChanged();
            add_order_ms_project_type.setSelectedIndex(0);
        }

    }

    //计算返回的边界
    private void computeBoundsBackward(int firstCompletelyVisiblePos) {
        for (int i = firstCompletelyVisiblePos; i < adapter.getItemCount(); i++) {
            View itemView = mGridLayoutManager.findViewByPosition(i);
            Rect bounds = new Rect();
            if (itemView != null) {
                ImageView imageView = itemView.findViewById(R.id.iiv_item_image_preview);
                imageView.getGlobalVisibleRect(bounds);
            }
            adapter.getItem(i).setBounds(bounds);
        }
    }

    @Event({R.id.toolbar_btn_back,R.id.login_btn_login})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                finish();
                break;
            case R.id.login_btn_login:
                addOrderSubmit();
                break;
        }
    }

    private void addOrderSubmit(){
        if(TextUtils.isEmpty(add_order_et_address.getText())){
            showToast("请输入维修地点");
            return;
        }
        if(TextUtils.isEmpty(add_order_et_contact.getText())){
            showToast("请输入联系人");
            return;
        }
        if(TextUtils.isEmpty(add_order_et_contact_tel.getText())){
            showToast("请输入联系电话");
            return;
        }
        if(TextUtils.isEmpty(add_order_et_remark.getText())){
            showToast("请输入问题描述");
            return;
        }
        RequestParam requestParam=new RequestParam(BASE_URL+WORKORDER+"work/order/api/user/addOrder",HttpMethod.Post);
        Map<String,Object> map=new HashMap<>();
        map.put("address",add_order_et_address.getText().toString());
        map.put("typeId",projectValue);
        map.put("projectId","16f274b06fb2f8c87eb107841bfbbc05");
        map.put("roomId","16fa72dd4e7034b8aa5a2af40d1972ce");
        map.put("householdId","16fa73e5607ee21f4ff5e0842e7b5d4c");
        map.put("linkMan",add_order_et_contact.getText().toString());
        map.put("mobile",add_order_et_contact_tel.getText().toString());
        map.put("problemDesc",add_order_et_remark.getText().toString());
        if(dataList.size()>1)
            map.put("resourcekey",resourceKey);
        requestParam.setRequestMap(map);
        requestParam.setParamType(ParamType.Json);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);

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
        sendRequest(requestParam,true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        RequestParam requestParam=new RequestParam(BASE_URL+FILE+"files-anon",HttpMethod.Upload);
        Map<String,Object> map=new HashMap<>();
        map.put("UploadFile",new File(path));
        map.put("resourceKey",resourceKey);
        requestParam.setRequestMap(map);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                dataList.add(dataList.size()-1,new ImageViewInfo(path));
                adapter.notifyDataSetChanged();
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
