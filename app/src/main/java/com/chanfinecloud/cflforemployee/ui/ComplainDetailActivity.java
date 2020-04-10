package com.chanfinecloud.cflforemployee.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.ui.base.BaseActivity;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.ComplainDetailsEntity;
import com.chanfinecloud.cflforemployee.entity.EventBusMessage;
import com.chanfinecloud.cflforemployee.entity.ResourceEntity;
import com.chanfinecloud.cflforemployee.entity.WorkflowProcessesEntity;
import com.chanfinecloud.cflforemployee.entity.WorkflowType;
import com.chanfinecloud.cflforemployee.util.FilePathUtil;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.util.SharedPreferencesManage;
import com.chanfinecloud.cflforemployee.http.HttpMethod;
import com.chanfinecloud.cflforemployee.http.JsonParse;
import com.chanfinecloud.cflforemployee.http.MyCallBack;
import com.chanfinecloud.cflforemployee.http.RequestParam;
import com.chanfinecloud.cflforemployee.weidgt.NoUnderlineSpan;
import com.chanfinecloud.cflforemployee.weidgt.imagepreview.ImagePreviewListAdapter;
import com.chanfinecloud.cflforemployee.weidgt.imagepreview.ImageViewInfo;
import com.chanfinecloud.cflforemployee.weidgt.imagepreview.PreviewBuilder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

@ContentView(R.layout.activity_complain_detail)
public class ComplainDetailActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_tv_title)
    TextView toolbar_title;
    @ViewInject(R.id.toolbar_tv_action)
    TextView toolbar_tv_action;
    @ViewInject(R.id.toolbar_btn_action)
    ImageButton toolbar_btn_action;

    @ViewInject(R.id.complain_detail_user_avatar)
    private ImageView complain_detail_user_avatar;
    @ViewInject(R.id.complain_detail_user_name)
    private TextView complain_detail_user_name;
    @ViewInject(R.id.complain_detail_user_room)
    private TextView complain_detail_user_room;
    @ViewInject(R.id.complain_detail_complain_type)
    private TextView complain_detail_complain_type;
    @ViewInject(R.id.complain_detail_contact_tel)
    private TextView complain_detail_contact_tel;
    @ViewInject(R.id.complain_detail_remark_text)
    private TextView complain_detail_remark_text;
    @ViewInject(R.id.complain_detail_remark_rlv)
    private RecyclerView complain_detail_remark_rlv;
    @ViewInject(R.id.complain_detail_remark_time)
    private TextView complain_detail_remark_time;


    @ViewInject(R.id.complain_detail_workflow_ll)
    private LinearLayout complain_detail_workflow_ll;

    @ViewInject(R.id.complain_detail__srl)
    private SmartRefreshLayout complain_detail__srl;

    private String complainId;
    private NoUnderlineSpan mNoUnderlineSpan;
    private List<WorkflowProcessesEntity> data=new ArrayList<>();
    private FragmentManager fragmentManager;
    private WorkflowActionFragment workflowActionFragment;
    public static final int REQUEST_CODE_CHOOSE=0x002;
    public String resourceKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkAppPermission();
        super.onCreate(savedInstanceState);
        toolbar_title.setText("投诉详情");
        toolbar_btn_action.setVisibility(View.GONE);
        toolbar_tv_action.setText("进度");
        toolbar_tv_action.setVisibility(View.VISIBLE);
        fragmentManager=getSupportFragmentManager();
        complainId=getIntent().getExtras().getString("complain_id");
        getData();
        mNoUnderlineSpan = new NoUnderlineSpan();
        EventBus.getDefault().register(this);

        resourceKey= UUID.randomUUID().toString().replaceAll("-","");

        complain_detail__srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getData();
            }
        });
    }

    @Event({R.id.toolbar_btn_back,R.id.toolbar_tv_action})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                finish();
                break;
            case R.id.toolbar_tv_action:
                Bundle bundle=new Bundle();
                bundle.putSerializable("workflowProcessesList", (Serializable) data);
                startActivity(WorkflowStepActivity.class,bundle);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage message){
        if("WorkflowActionRefresh".equals(message.getMessage())){
            getData();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    /**
     * 获取投诉详情数据
     */
    private void getData(){
        RequestParam requestParam=new RequestParam(BASE_URL+WORKORDER+"workflow/api/detail/"+complainId,HttpMethod.Get);
        Map<String,String> map=new HashMap<>();
        map.put("type", WorkflowType.Complain.getType());
        requestParam.setRequestMap(map);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity<ComplainDetailsEntity> baseEntity= JsonParse.parse(result,ComplainDetailsEntity.class);
                if(baseEntity.isSuccess()){
                    initView(baseEntity.getResult());
                    initAction(baseEntity.getResult());
                    List<WorkflowProcessesEntity> workflowList=baseEntity.getResult().getProcesses();
                    WorkflowProcessesEntity lastWorkflow=workflowList.get(workflowList.size()-1);
                    if(lastWorkflow.getOperationInfos()!=null&&lastWorkflow.getOperationInfos().size()>0){
                        workflowList.remove(workflowList.size()-1);
                    }
                    initWorkFlow(workflowList);
                    data.clear();
                    data.addAll(workflowList);
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
                complain_detail__srl.finishRefresh();
            }
        });
        sendRequest(requestParam,true);
    }

    /**
     * 初始化主视图
     * @param complainEntity 投诉详情实体
     */
    private void initView(ComplainDetailsEntity complainEntity){
        if(TextUtils.isEmpty(complainEntity.getCreatorAvatarUrl())){
            Glide.with(this)
                    .load(R.drawable.icon_user_default)
                    .error(R.drawable.ic_no_img)
                    .circleCrop()
                    .into(complain_detail_user_avatar);
        }else{
            Glide.with(this)
                    .load(complainEntity.getCreatorAvatarUrl())
                    .error(R.drawable.ic_no_img)
                    .circleCrop()
                    .into(complain_detail_user_avatar);
        }
        complain_detail_user_name.setText(complainEntity.getHouseholdName());
        complain_detail_user_room.setText(complainEntity.getBriefDesc());
        complain_detail_complain_type.setText(complainEntity.getComplaintTypeName());
        if (!TextUtils.isEmpty(complainEntity.getHouseholdMobile())) {
            complain_detail_contact_tel.setText(complainEntity.getHouseholdMobile());
        } else {
            complain_detail_contact_tel.setVisibility(View.GONE);
        }
        if (complain_detail_contact_tel.getText() instanceof Spannable) {
            Spannable s = (Spannable) complain_detail_contact_tel.getText();
            s.setSpan(mNoUnderlineSpan, 0, s.length(), Spanned.SPAN_MARK_MARK);
        }
        complain_detail_remark_text.setText(complainEntity.getProblemDesc());
        complain_detail_remark_time.setText(complainEntity.getCreateTime());
        List<ResourceEntity> picData=complainEntity.getProblemResourceValue();
        if(picData!=null&&picData.size()>0) {
            final List<ImageViewInfo> data = new ArrayList<>();
            for (int j = 0; j < picData.size(); j++) {
                data.add(new ImageViewInfo(picData.get(j).getUrl()));
            }
            final ImagePreviewListAdapter imageAdapter=new ImagePreviewListAdapter(this,R.layout.item_workflow_image_perview_list,data);
            final GridLayoutManager mGridLayoutManager = new GridLayoutManager(this,4);
            complain_detail_remark_rlv.setLayoutManager(mGridLayoutManager);
            complain_detail_remark_rlv.setAdapter(imageAdapter);
            complain_detail_remark_rlv.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    for (int k = mGridLayoutManager.findFirstVisibleItemPosition(); k < adapter.getItemCount(); k++) {
                        View itemView = mGridLayoutManager.findViewByPosition(k);
                        Rect bounds = new Rect();
                        if (itemView != null) {
                            ImageView imageView = itemView.findViewById(R.id.iiv_item_image_preview);
                            imageView.getGlobalVisibleRect(bounds);
                        }
                        //计算返回的边界
                        imageAdapter.getItem(k).setBounds(bounds);
                    }
                    PreviewBuilder.from(ComplainDetailActivity.this)
                            .setImgs(data)
                            .setCurrentIndex(position)
                            .setSingleFling(true)
                            .setType(PreviewBuilder.IndicatorType.Number)
                            .start();
                }
            });
        }
    }
    /**
     * 初始化流程视图
     * @param list 流程处理list
     */
    private void initWorkFlow(List<WorkflowProcessesEntity> list){
        if(list.size()>0){
            complain_detail_workflow_ll.removeAllViews();
            for (int i = 0; i < list.size(); i++) {
                View v = LayoutInflater.from(this).inflate(R.layout.item_workflow_list,null);
                ImageView item_workflow_avatar=v.findViewById(R.id.item_workflow_avatar);
                TextView item_workflow_user_name=v.findViewById(R.id.item_workflow_user_name);
                TextView item_workflow_user_role=v.findViewById(R.id.item_workflow_user_role);
                TextView item_workflow_tel=v.findViewById(R.id.item_workflow_tel);
                TextView item_workflow_content=v.findViewById(R.id.item_workflow_content);
                RecyclerView item_workflow_pic=v.findViewById(R.id.item_workflow_pic);
                TextView item_workflow_node=v.findViewById(R.id.item_workflow_node);
                TextView item_workflow_time=v.findViewById(R.id.item_workflow_time);
                WorkflowProcessesEntity item=list.get(i);
                if(TextUtils.isEmpty(item.getAvatarUrl())){
                    Glide.with(this)
                            .load(R.drawable.ic_launcher)
                            .circleCrop()
                            .error(R.drawable.ic_no_img)
                            .into(item_workflow_avatar);
                }else{
                    Glide.with(this)
                            .load(item.getAvatarUrl())
                            .circleCrop()
                            .error(R.drawable.ic_no_img)
                            .into(item_workflow_avatar);
                }
                item_workflow_user_name.setText(item.getHandlerName());
                item_workflow_user_role.setText(item.getBriefDesc());
                if (!TextUtils.isEmpty(item.getHandlerMobile())) {
                    item_workflow_tel.setText(item.getHandlerMobile());
                } else {
                    item_workflow_tel.setVisibility(View.GONE);
                }
                if (item_workflow_tel.getText() instanceof Spannable) {
                    Spannable s = (Spannable) item_workflow_tel.getText();
                    s.setSpan(mNoUnderlineSpan, 0, s.length(), Spanned.SPAN_MARK_MARK);
                }
                item_workflow_content.setText(item.getRemark());
                item_workflow_node.setText(item.getNodeName());
                item_workflow_time.setText(item.getCreateTime());
                List<ResourceEntity> picData=item.getResourceValues();
                if(picData!=null&&picData.size()>0){
                    final List<ImageViewInfo> data=new ArrayList<>();
                    for (int j = 0; j < picData.size(); j++) {
                        data.add(new ImageViewInfo(picData.get(j).getUrl()));
                    }
                    final ImagePreviewListAdapter imageAdapter=new ImagePreviewListAdapter(this,R.layout.item_workflow_image_perview_list,data);
                    final GridLayoutManager mGridLayoutManager = new GridLayoutManager(this,4);
                    item_workflow_pic.setLayoutManager(mGridLayoutManager);
                    item_workflow_pic.setAdapter(imageAdapter);
                    item_workflow_pic.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
                        @Override
                        public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                            for (int k = mGridLayoutManager.findFirstVisibleItemPosition(); k < adapter.getItemCount(); k++) {
                                View itemView = mGridLayoutManager.findViewByPosition(k);
                                Rect bounds = new Rect();
                                if (itemView != null) {
                                    ImageView imageView = itemView.findViewById(R.id.iiv_item_image_preview);
                                    imageView.getGlobalVisibleRect(bounds);
                                }
                                //计算返回的边界
                                imageAdapter.getItem(k).setBounds(bounds);
                            }
                            PreviewBuilder.from(ComplainDetailActivity.this)
                                    .setImgs(data)
                                    .setCurrentIndex(position)
                                    .setSingleFling(true)
                                    .setType(PreviewBuilder.IndicatorType.Number)
                                    .start();
                        }
                    });
                }
                complain_detail_workflow_ll.addView(v);
            }
            complain_detail_workflow_ll.setVisibility(View.VISIBLE);
        }else{
            complain_detail_workflow_ll.setVisibility(View.GONE);
        }
    }
    /**
     * 初始化流程处理视图
     * @param complainDetailsEntity ComplainDetailsEntity流程实体
     */
    private void initAction(ComplainDetailsEntity complainDetailsEntity){
        List<WorkflowProcessesEntity> workflowList=complainDetailsEntity.getProcesses();
        WorkflowProcessesEntity lastWorkflow=workflowList.get(workflowList.size()-1);
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        List<String> departIds=SharedPreferencesManage.getUserInfo().getDepartId();
        String departIdStr="";
        if(departIds!=null){
            for (int i = 0; i < departIds.size(); i++) {
                departIdStr+=departIds.get(i)+",";
            }
        }
        if((lastWorkflow.getAssigneeId().equals(SharedPreferencesManage.getUserInfo().getId())
                ||departIdStr.contains(lastWorkflow.getAssigneeId()))
                &&(lastWorkflow.getOperationInfos()!=null&&lastWorkflow.getOperationInfos().size()>0)) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("permission", permission);
            bundle.putString("businessId", complainId);
            bundle.putString("action", lastWorkflow.getNodeName());
            bundle.putSerializable("workflowType", WorkflowType.Complain);
            bundle.putSerializable("workflowProcesses", lastWorkflow);
            bundle.putSerializable("complainDetail", complainDetailsEntity);
            if(workflowActionFragment !=null){
                workflowActionFragment =new WorkflowActionFragment().newInstance(bundle);
                transaction.replace(R.id.complain_detail_workflow_action_fl, workflowActionFragment).commit();
            }else{
                workflowActionFragment =new WorkflowActionFragment().newInstance(bundle);
                transaction.add(R.id.complain_detail_workflow_action_fl, workflowActionFragment).commit();
            }
        }else{
            if(workflowActionFragment !=null){
                transaction.remove(workflowActionFragment).commit();
            }
        }
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

    /**
     * 压缩图片
     * @param list 图片路径list
     */
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

    /**
     * 上传照片
     * @param path 图片路径
     */
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
                workflowActionFragment.setPicData(new ImageViewInfo(path));
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
