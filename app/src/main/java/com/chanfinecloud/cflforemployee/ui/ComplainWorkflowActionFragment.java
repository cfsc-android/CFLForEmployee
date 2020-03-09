package com.chanfinecloud.cflforemployee.ui;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.base.BaseFragment;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.EventBusMessage;
import com.chanfinecloud.cflforemployee.entity.OperationInfoEntity;
import com.chanfinecloud.cflforemployee.entity.UserEntity;
import com.chanfinecloud.cflforemployee.entity.UserListEntity;
import com.chanfinecloud.cflforemployee.entity.WorkflowComplainActionType;
import com.chanfinecloud.cflforemployee.entity.WorkflowOrderActionType;
import com.chanfinecloud.cflforemployee.util.AnimationUtil;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.util.http.HttpMethod;
import com.chanfinecloud.cflforemployee.util.http.JsonParse;
import com.chanfinecloud.cflforemployee.util.http.MyCallBack;
import com.chanfinecloud.cflforemployee.util.http.RequestParam;
import com.chanfinecloud.cflforemployee.weidgt.imagepreview.ImagePreviewListAdapter;
import com.chanfinecloud.cflforemployee.weidgt.imagepreview.ImageViewInfo;
import com.chanfinecloud.cflforemployee.weidgt.photopicker.PhotoPicker;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.chanfinecloud.cflforemployee.base.Config.BASE_URL;
import static com.chanfinecloud.cflforemployee.ui.OrderDetailActivity.REQUEST_CODE_CHOOSE;

/**
 * Created by Loong on 2020/2/21.
 * Version: 1.0
 * Describe:
 */

@ContentView(R.layout.workflow_action_layout)
public class ComplainWorkflowActionFragment extends BaseFragment {
    public ComplainWorkflowActionFragment() {
    }
    public ComplainWorkflowActionFragment newInstance(Bundle bundle) {
        ComplainWorkflowActionFragment fragment = new ComplainWorkflowActionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @ViewInject(R.id.workflow_action_name)
    private TextView workflow_action_name;
    @ViewInject(R.id.workflow_action_toggle_icon)
    private ImageView workflow_action_toggle_icon;
    @ViewInject(R.id.workflow_action_content)
    private FrameLayout workflow_action_content;

    //客服检视投诉
    @ViewInject(R.id.workflow_action_customer_check_complain)
    private LinearLayout workflow_action_customer_check_complain;
    @ViewInject(R.id.customer_check_complain_remark)
    private EditText customer_check_complain_remark;

    //客服电话沟通业主
    @ViewInject(R.id.workflow_action_customer_contact_household)
    private LinearLayout workflow_action_customer_contact_household;
    @ViewInject(R.id.customer_contact_household_remark)
    private EditText customer_contact_household_remark;

    //客服指派职能主管
    @ViewInject(R.id.workflow_action_customer_dispatch_director_c)
    private LinearLayout workflow_action_customer_dispatch_director_c;
    @ViewInject(R.id.customer_dispatch_director_c_director)
    private MaterialSpinner customer_dispatch_director_c_director;
    @ViewInject(R.id.customer_dispatch_director_c_remark)
    private EditText customer_dispatch_director_c_remark;

    //职能主管现场查看
    @ViewInject(R.id.workflow_action_director_photo)
    private LinearLayout workflow_action_director_photo;
    @ViewInject(R.id.director_photo_pic)
    private RecyclerView director_photo_pic;
    @ViewInject(R.id.director_photo_remark)
    private EditText director_photo_remark;

    //职能主管填写解决方案
    @ViewInject(R.id.workflow_action_director_write_plan)
    private LinearLayout workflow_action_director_write_plan;
    @ViewInject(R.id.director_write_plan_remark)
    private EditText director_write_plan_remark;

    //主管分发员工
    @ViewInject(R.id.workflow_action_director_dispatch_employee_c)
    private LinearLayout workflow_action_director_dispatch_employee_c;
    @ViewInject(R.id.director_dispatch_employee_c_employee)
    private MaterialSpinner director_dispatch_employee_c_employee;
    @ViewInject(R.id.director_dispatch_employee_c_remark)
    private EditText director_dispatch_employee_c_remark;

    //员工实施投诉解决方案
    @ViewInject(R.id.workflow_action_employee_finish_c)
    private LinearLayout workflow_action_employee_finish_c;
    @ViewInject(R.id.employee_finish_c_pic)
    private RecyclerView employee_finish_c_pic;
    @ViewInject(R.id.employee_finish_c_remark)
    private EditText employee_finish_c_remark;

    //实施投诉解决方案是否完成
    @ViewInject(R.id.workflow_action_plan_finish)
    private LinearLayout workflow_action_plan_finish;


    private Activity context;
    private String action,complaintId,operationName;
    private boolean permission;
    private List<OperationInfoEntity> operationInfoEntities;
    private WorkflowComplainActionType actionType;
    private boolean toggle=true;

    private List<UserEntity> userList=new ArrayList<>();
    private String directorValue,employeeValue;

    private List<ImageViewInfo> picList=new ArrayList<>();
    private ImagePreviewListAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        permission=getArguments().getBoolean("permission");
        action = getArguments().getString("action");
        complaintId = getArguments().getString("complaintId");
        operationName = getArguments().getString("operationName");
        operationInfoEntities = (List<OperationInfoEntity>) getArguments().getSerializable("operationInfos");
        WorkflowComplainActionType[] types=WorkflowComplainActionType.values();
        for (int i = 0; i < types.length; i++) {
            if(action.equals(types[i].name())){
                actionType=types[i];
            }
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        workflow_action_name.setText(action);
        switch (actionType){
            case 检视投诉单:
                workflow_action_customer_check_complain.setVisibility(View.VISIBLE);
                break;
            case 电话沟通业主:
                workflow_action_customer_contact_household.setVisibility(View.VISIBLE);
                break;
            case 分发职能主管:
                workflow_action_customer_dispatch_director_c.setVisibility(View.VISIBLE);
                break;
            case 到场查看:
                workflow_action_director_photo.setVisibility(View.VISIBLE);
                break;
            case 已填投诉解决方案:
                workflow_action_director_write_plan.setVisibility(View.VISIBLE);
                break;
            case 分发员工实施:
                workflow_action_director_dispatch_employee_c.setVisibility(View.VISIBLE);
                break;
            case 实施投诉解决方案:
                workflow_action_employee_finish_c.setVisibility(View.VISIBLE);
                break;
            case 方案是否完成:
                workflow_action_plan_finish.setVisibility(View.VISIBLE);
                break;
        }
        getUserData();
        initRecycler();
    }

    private void getUserData(){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"sys/user/list");
        requestParam.setMethod(HttpMethod.Get);
        Map<String,String> requestMap=new HashMap<>();
        requestMap.put("pageNo","1");
        requestMap.put("pageSize","100");
        requestParam.setGetRequestMap(requestMap);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity<UserListEntity> baseEntity= JsonParse.parse(result,UserListEntity.class);
                if(baseEntity.isSuccess()){
                    userList.addAll(baseEntity.getResult().getData());
                    initSpinner();
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
        sendRequest(requestParam,true);
    }

    private void initSpinner(){
        List<String> adapterData=new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            adapterData.add(userList.get(i).getRealName());
        }
        directorValue=userList.get(0).getId();
        customer_dispatch_director_c_director.setItems(adapterData);
        customer_dispatch_director_c_director.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                directorValue=userList.get(position).getId();
            }
        });
        employeeValue=userList.get(0).getId();
        director_dispatch_employee_c_employee.setItems(adapterData);
        director_dispatch_employee_c_employee.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                employeeValue=userList.get(position).getId();
            }
        });
    }

    private void initRecycler(){
        picList.add(new ImageViewInfo("plus"));
        adapter=new ImagePreviewListAdapter(context,R.layout.item_workflow_image_perview_list,picList);
        director_photo_pic.setLayoutManager(new GridLayoutManager(context,4));
        director_photo_pic.setAdapter(adapter);
        director_photo_pic.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(position==picList.size()-1){
                    if(permission){
                        PhotoPicker.pick(context,10,true,REQUEST_CODE_CHOOSE);
                    }else{
                        showToast("相机或读写手机存储的权限被禁止！");
                    }
                }
            }
        });
        employee_finish_c_pic.setLayoutManager(new GridLayoutManager(context,4));
        employee_finish_c_pic.setAdapter(adapter);
        employee_finish_c_pic.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(position==picList.size()-1){
                    if(permission){
                        PhotoPicker.pick(context,10,true,REQUEST_CODE_CHOOSE);
                    }else{
                        showToast("相机或读写手机存储的权限被禁止！");
                    }
                }
            }
        });
    }

    public void setPicData(ImageViewInfo imageViewInfo){
        picList.add(picList.size()-1,imageViewInfo);
        adapter.notifyDataSetChanged();
    }

    @Event({R.id.customer_check_complain_submit,R.id.customer_contact_household_submit,R.id.customer_dispatch_director_c_submit,R.id.director_photo_submit,
            R.id.director_write_plan_submit,R.id.director_dispatch_employee_c_submit,R.id.employee_finish_c_remark,R.id.plan_finish_reject,R.id.plan_finish_confirm,
            R.id.workflow_action_toggle})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.customer_check_complain_submit:
                customerCheckComplain();
                break;
            case R.id.customer_contact_household_submit:
                customerContactHousehold();
                break;
            case R.id.customer_dispatch_director_c_submit:
                customerDispatchDirector();
                break;
            case R.id.director_photo_submit:
                directorPhoto();
                break;
            case R.id.director_write_plan_submit:
                directorWritePlan();
                break;
            case R.id.director_dispatch_employee_c_submit:
                directorDispatchEmployee();
                break;
            case R.id.employee_finish_c_remark:
                employeeFinish();
                break;
            case R.id.plan_finish_reject:
                planFinish(0);
                break;
            case R.id.plan_finish_confirm:
                planFinish(1);
                break;
            case R.id.workflow_action_toggle:
                toggle=!toggle;
                int width = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                int height = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                workflow_action_content.measure(width, height);
                if(toggle){
                    workflow_action_content.setVisibility(View.VISIBLE);
                    AnimationUtil.startTransYAnimation(workflow_action_content,workflow_action_content.getMeasuredHeight(),0,200,new AnimationUtil.AnimationListener(){
                        @Override
                        public void onAnimationStart(Animator animation, boolean isReverse) {
                            super.onAnimationStart(animation, isReverse);
                            AnimationUtil.startRotateAnimation(workflow_action_toggle_icon,180,0,200);
                        }
                    });
                }else{
                    AnimationUtil.startTransYAnimation(workflow_action_content,0,workflow_action_content.getMeasuredHeight(),200,new AnimationUtil.AnimationListener(){
                        @Override
                        public void onAnimationStart(Animator animation, boolean isReverse) {
                            super.onAnimationStart(animation, isReverse);
                            AnimationUtil.startRotateAnimation(workflow_action_toggle_icon,0,180,200);

                        }

                        @Override
                        public void onAnimationEnd(Animator animation, boolean isReverse) {
                            super.onAnimationEnd(animation, isReverse);
                            workflow_action_content.setVisibility(View.GONE);
                        }
                    });
                }
                break;
        }
    }

    //客服检视投诉
    private void customerCheckComplain(){
        if(TextUtils.isEmpty(customer_check_complain_remark.getText())){
            showToast("请输入检视备注");
            return;
        }
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/complaint/accept");
        requestParam.setMethod(HttpMethod.PostJson);
        Map<String,Object> map=new HashMap<>();
        map.put("operationId",operationInfoEntities.get(0).getId());
        map.put("operationName",operationName);
        map.put("complaintId",complaintId);
        map.put("chatContent",customer_check_complain_remark.getText().toString());
        requestParam.setPostJsonRequest(map);
        requestParam.setCallback(myCallBack);
        sendRequest(requestParam,true);
    }
    //客服电话联系业主
    private void customerContactHousehold(){
        if(TextUtils.isEmpty(customer_contact_household_remark.getText())){
            showToast("请输入沟通备注");
            return;
        }
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/complaintOwner/communicateOwner");
        requestParam.setMethod(HttpMethod.PostJson);
        Map<String,Object> map=new HashMap<>();
        map.put("operationId",operationInfoEntities.get(0).getId());
        map.put("operationName",operationName);
        map.put("complaintId",complaintId);
        map.put("chatContent",customer_contact_household_remark.getText().toString());
        requestParam.setPostJsonRequest(map);
        requestParam.setCallback(myCallBack);
        sendRequest(requestParam,true);
    }
    //客服指派职能主管
    private void customerDispatchDirector(){

    }
    //职能主管现场查看
    private void directorPhoto(){

    }
    //职能主管填写解决方案
    private void directorWritePlan(){

    }
    //职能主管指派员工
    private void directorDispatchEmployee(){

    }
    //员工完成实施
    private void employeeFinish(){

    }
    //是否完成
    private void planFinish(int result){

    }

    private MyCallBack myCallBack=new MyCallBack<String>(){
        @Override
        public void onSuccess(String result) {
            super.onSuccess(result);
            LogUtils.d("result",result);
            BaseEntity baseEntity= JsonParse.parse(result);
            if(baseEntity.isSuccess()){
                EventBus.getDefault().post(new EventBusMessage<>("ComplainDetailRefresh"));
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
    };
}
