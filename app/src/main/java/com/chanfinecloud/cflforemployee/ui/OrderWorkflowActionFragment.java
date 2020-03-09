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
public class OrderWorkflowActionFragment extends BaseFragment {
    public OrderWorkflowActionFragment() {
    }
    public OrderWorkflowActionFragment newInstance(Bundle bundle) {
        OrderWorkflowActionFragment fragment = new OrderWorkflowActionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @ViewInject(R.id.workflow_action_name)
    private TextView workflow_action_name;
    @ViewInject(R.id.workflow_action_toggle_icon)
    private ImageView workflow_action_toggle_icon;
    @ViewInject(R.id.workflow_action_content)
    private FrameLayout workflow_action_content;

    //客服接单
    @ViewInject(R.id.workflow_action_customer_accept_order)
    private LinearLayout workflow_action_customer_accept_order;
    @ViewInject(R.id.customer_accept_order_remark)
    private EditText customer_accept_order_remark;

    //客服指派职能主管
    @ViewInject(R.id.workflow_action_customer_dispatch_director)
    private LinearLayout workflow_action_customer_dispatch_director;
    @ViewInject(R.id.customer_dispatch_director_director)
    private MaterialSpinner customer_dispatch_director_director;
    @ViewInject(R.id.customer_dispatch_director_remark)
    private EditText customer_dispatch_director_remark;

    //主管分发员工
    @ViewInject(R.id.workflow_action_director_dispatch_employee)
    private LinearLayout workflow_action_director_dispatch_employee;
    @ViewInject(R.id.director_dispatch_employee_employee)
    private MaterialSpinner director_dispatch_employee_employee;
    @ViewInject(R.id.director_dispatch_employee_remark)
    private EditText director_dispatch_employee_remark;

    //员工检视工单
    @ViewInject(R.id.workflow_action_employee_check_order)
    private LinearLayout workflow_action_employee_check_order;
    @ViewInject(R.id.employee_check_order_remark)
    private EditText employee_check_order_remark;

    //员工现场拍照
    @ViewInject(R.id.workflow_action_employee_photo)
    private LinearLayout workflow_action_employee_photo;
    @ViewInject(R.id.employee_photo_pic)
    private RecyclerView employee_photo_pic;
    @ViewInject(R.id.employee_photo_remark)
    private EditText employee_photo_remark;

    //员工接单/拒接
    @ViewInject(R.id.workflow_action_employee_accept)
    private LinearLayout workflow_action_employee_accept;

    //员工发送费用
    @ViewInject(R.id.workflow_action_employee_send_cost)
    private LinearLayout workflow_action_employee_send_cost;
    @ViewInject(R.id.employee_send_cost_cost)
    private EditText employee_send_cost_cost;
    @ViewInject(R.id.employee_send_cost_remark)
    private EditText employee_send_cost_remark;

    //员工完成工单
    @ViewInject(R.id.workflow_action_employee_finish)
    private LinearLayout workflow_action_employee_finish;
    @ViewInject(R.id.employee_finish_pic)
    private RecyclerView employee_finish_pic;
    @ViewInject(R.id.employee_finish_remark)
    private EditText employee_finish_remark;

    private Activity context;
    private String action,workOrderId,operationName;
    private boolean permission;
    private List<OperationInfoEntity> operationInfoEntities;
    private WorkflowOrderActionType actionType;
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
        workOrderId = getArguments().getString("workOrderId");
        operationName = getArguments().getString("operationName");
        operationInfoEntities = (List<OperationInfoEntity>) getArguments().getSerializable("operationInfos");
        WorkflowOrderActionType[] types=WorkflowOrderActionType.values();
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
            case 指挥中心确认工单:
                workflow_action_customer_accept_order.setVisibility(View.VISIBLE);
                break;
            case 接单工单补充:
                workflow_action_customer_dispatch_director.setVisibility(View.VISIBLE);
                break;
            case 分发工单:
                workflow_action_director_dispatch_employee.setVisibility(View.VISIBLE);
                break;
            case 员工检视工单:
                workflow_action_employee_check_order.setVisibility(View.VISIBLE);
                break;
            case 到场拍照:
                workflow_action_employee_photo.setVisibility(View.VISIBLE);
                break;
            case 员工接工单:
                workflow_action_employee_accept.setVisibility(View.VISIBLE);
                break;
            case 员工发送费用:
                workflow_action_employee_send_cost.setVisibility(View.VISIBLE);
                break;
            case 工作:
                workflow_action_employee_finish.setVisibility(View.VISIBLE);
                break;
            case 接单:
                employeeAcceptOrderBack();
                break;
            case 填报工单:
                customerConfirmOrder();
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
        customer_dispatch_director_director.setItems(adapterData);
        customer_dispatch_director_director.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                directorValue=userList.get(position).getId();
            }
        });
        employeeValue=userList.get(0).getId();
        director_dispatch_employee_employee.setItems(adapterData);
        director_dispatch_employee_employee.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                employeeValue=userList.get(position).getId();
            }
        });
    }

    private void initRecycler(){
        picList.add(new ImageViewInfo("plus"));
        adapter=new ImagePreviewListAdapter(context,R.layout.item_workflow_image_perview_list,picList);
        employee_photo_pic.setLayoutManager(new GridLayoutManager(context,4));
        employee_photo_pic.setAdapter(adapter);
        employee_photo_pic.addOnItemTouchListener(new OnItemClickListener() {
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
        employee_finish_pic.setLayoutManager(new GridLayoutManager(context,4));
        employee_finish_pic.setAdapter(adapter);
        employee_finish_pic.addOnItemTouchListener(new OnItemClickListener() {
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

    @Event({R.id.customer_accept_order_reject,R.id.customer_accept_order_confirm,R.id.customer_dispatch_director_submit,
            R.id.director_dispatch_employee_submit,R.id.employee_check_order_submit,R.id.employee_photo_submit,
            R.id.employee_accept_reject,R.id.employee_accept_confirm,R.id.employee_send_cost_confirm,R.id.employee_finish_confirm,
            R.id.workflow_action_toggle})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.customer_accept_order_reject:
                customerAcceptOrder(0);
                break;
            case R.id.customer_accept_order_confirm:
                customerAcceptOrder(1);
                break;
            case R.id.customer_dispatch_director_submit:
                customerDispatchDirector();
                break;
            case R.id.director_dispatch_employee_submit:
                directorDispatchEmployee();
                break;
            case R.id.employee_check_order_submit:
                employeeCheckOrder();
                break;
            case R.id.employee_photo_submit:
                employeePhoto();
                break;
            case R.id.employee_accept_reject:
                employeeAcceptOrder(1);
                break;
            case R.id.employee_accept_confirm:
                employeeAcceptOrder(0);
                break;
            case R.id.employee_send_cost_confirm:
                sendCost();
                break;
            case R.id.employee_finish_confirm:
                finishConfirm();
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

    //客服确认工单，后台处理了（待优化）
    private void customerConfirmOrder(){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/order/api/user/orderDoOk");
        requestParam.setMethod(HttpMethod.PostJson);
        Map<String,Object> map=new HashMap<>();
        map.put("operationId",operationInfoEntities.get(0).getId());
        map.put("operationName",operationName);
        map.put("workOrderId",workOrderId);
        requestParam.setPostJsonRequest(map);
        requestParam.setCallback(myCallBack);
        sendRequest(requestParam,true);
    }

    //客服接单
    private void customerAcceptOrder(int result){
        if(result==0&&TextUtils.isEmpty(customer_accept_order_remark.getText())){
            showToast("请输入拒接备注");
            return;
        }
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/order/api/user/customerServiceRefuseWorkOrder");
        requestParam.setMethod(HttpMethod.PostJson);
        Map<String,Object> map=new HashMap<>();
        map.put("operationId",operationInfoEntities.get(result).getId());
        map.put("operationName",operationName);
        map.put("workOrderId",workOrderId);
        map.put("rejectedReason",customer_accept_order_remark.getText().toString());
        map.put("remark",customer_accept_order_remark.getText().toString());
        requestParam.setPostJsonRequest(map);
        requestParam.setCallback(myCallBack);
        sendRequest(requestParam,true);
    }

    //客服指派主管
    private void customerDispatchDirector(){
        if(TextUtils.isEmpty(directorValue)){
            showToast("请选择职能主管");
            return;
        }
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/order/api/user/designateWorkOrder");
        requestParam.setMethod(HttpMethod.PostJson);
        Map<String,Object> map=new HashMap<>();
        map.put("operationId",operationInfoEntities.get(0).getId());
        map.put("operationName",operationName);
        map.put("remark",customer_dispatch_director_remark.getText().toString());
        map.put("sendUserId",directorValue);
        map.put("workOrderId",workOrderId);
        requestParam.setPostJsonRequest(map);
        requestParam.setCallback(myCallBack);
        sendRequest(requestParam,true);
    }

    //主管指派员工
    private void directorDispatchEmployee(){
        if(TextUtils.isEmpty(employeeValue)){
            showToast("请选择员工");
            return;
        }
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/order/api/user/sendWorkOrder");
        requestParam.setMethod(HttpMethod.PostJson);
        Map<String,Object> map=new HashMap<>();
        map.put("operationId",operationInfoEntities.get(0).getId());
        map.put("operationName",operationName);
        map.put("remark",director_dispatch_employee_remark.getText().toString());
        map.put("sendUserId",employeeValue);
        map.put("workOrderId",workOrderId);
        requestParam.setPostJsonRequest(map);
        requestParam.setCallback(myCallBack);
        sendRequest(requestParam,true);

    }

    //员工检视工单
    private void employeeCheckOrder(){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/order/api/user/checkWorkOrder");
        requestParam.setMethod(HttpMethod.PostJson);
        Map<String,Object> map=new HashMap<>();
        map.put("operationId",operationInfoEntities.get(0).getId());
        map.put("operationName",operationName);
        map.put("remark",employee_check_order_remark.getText().toString());
        map.put("workOrderId",workOrderId);
        requestParam.setPostJsonRequest(map);
        requestParam.setCallback(myCallBack);
        sendRequest(requestParam,true);
    }

    //员工现场拍照
    private void employeePhoto(){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/order/api/user/photoWorkOrder");
        requestParam.setMethod(HttpMethod.PostJson);
        Map<String,Object> map=new HashMap<>();
        map.put("operationId",operationInfoEntities.get(0).getId());
        map.put("operationName",operationName);
        map.put("photoUrl",((OrderDetailActivity)context).resourceKey);
        map.put("liveDesc",employee_photo_remark.getText().toString());
        map.put("remark",employee_photo_remark.getText().toString());
        map.put("workOrderId",workOrderId);
        requestParam.setPostJsonRequest(map);
        requestParam.setCallback(myCallBack);
        sendRequest(requestParam,true);
    }

    //员工接单/拒接
    private void employeeAcceptOrder(int result){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/order/api/user/photoWorkOrder");
        requestParam.setMethod(HttpMethod.PostJson);
        Map<String,Object> map=new HashMap<>();
        map.put("operationId",operationInfoEntities.get(result).getId());
        map.put("operationName",operationName);
        map.put("result",result==0?1:0);
        map.put("workOrderId",workOrderId);
        map.put("remark","员工接工单");
        requestParam.setPostJsonRequest(map);
        requestParam.setCallback(myCallBack);
        sendRequest(requestParam,true);
    }

    //员工接单，后台处理了（待优化）
    private void employeeAcceptOrderBack(){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/order/api/user/orderReceiving");
        requestParam.setMethod(HttpMethod.PostJson);
        Map<String,Object> map=new HashMap<>();
        map.put("operationId",operationInfoEntities.get(0).getId());
        map.put("operationName",operationName);
        map.put("workOrderId",workOrderId);
        map.put("remark","接单");
        requestParam.setPostJsonRequest(map);
        requestParam.setCallback(myCallBack);
        sendRequest(requestParam,true);
    }

    //员工发送费用
    private void sendCost(){
        if(TextUtils.isEmpty(employee_send_cost_cost.getText())){
            showToast("请输入费用");
            return;
        }
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/order/api/user/orderSendFee");
        requestParam.setMethod(HttpMethod.PostJson);
        Map<String,Object> map=new HashMap<>();
        map.put("operationId",operationInfoEntities.get(0).getId());
        map.put("operationName",operationName);
        map.put("workOrderId",workOrderId);
        map.put("manCost",Double.parseDouble(employee_send_cost_cost.getText().toString()));
        map.put("remark",employee_send_cost_remark.getText().toString());
        requestParam.setPostJsonRequest(map);
        requestParam.setCallback(myCallBack);
        sendRequest(requestParam,true);
    }

    //员工完成工作
    private void finishConfirm(){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/order/api/user/orderFinish");
        requestParam.setMethod(HttpMethod.PostJson);
        Map<String,Object> map=new HashMap<>();
        map.put("operationId",operationInfoEntities.get(0).getId());
        map.put("operationName",operationName);
        map.put("workOrderId",workOrderId);
        map.put("photoUrl",((OrderDetailActivity)context).resourceKey);
        map.put("remark",employee_finish_remark.getText().toString());
        requestParam.setPostJsonRequest(map);
        requestParam.setCallback(myCallBack);
        sendRequest(requestParam,true);
    }



    private MyCallBack myCallBack=new MyCallBack<String>(){
        @Override
        public void onSuccess(String result) {
            super.onSuccess(result);
            LogUtils.d("result",result);
            BaseEntity baseEntity= JsonParse.parse(result);
            if(baseEntity.isSuccess()){
                EventBus.getDefault().post(new EventBusMessage<>("OrderDetailRefresh"));
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
