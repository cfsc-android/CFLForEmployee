package com.chanfinecloud.cflforemployee.ui;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.ComplainDetailsEntity;
import com.chanfinecloud.cflforemployee.entity.EmergencyLevelType;
import com.chanfinecloud.cflforemployee.entity.EventBusMessage;
import com.chanfinecloud.cflforemployee.entity.OperationInfoEntity;
import com.chanfinecloud.cflforemployee.entity.OrderDetailsEntity;
import com.chanfinecloud.cflforemployee.entity.UserEntity;
import com.chanfinecloud.cflforemployee.entity.WorkflowFormContentEntity;
import com.chanfinecloud.cflforemployee.entity.WorkflowProcessesEntity;
import com.chanfinecloud.cflforemployee.entity.WorkflowType;
import com.chanfinecloud.cflforemployee.entity.WorkflowViewEntity;
import com.chanfinecloud.cflforemployee.entity.WorkflowViewTagEntity;
import com.chanfinecloud.cflforemployee.http.HttpMethod;
import com.chanfinecloud.cflforemployee.http.JsonParse;
import com.chanfinecloud.cflforemployee.http.MyCallBack;
import com.chanfinecloud.cflforemployee.http.ParamType;
import com.chanfinecloud.cflforemployee.http.RequestParam;
import com.chanfinecloud.cflforemployee.ui.base.BaseFragment;
import com.chanfinecloud.cflforemployee.util.AnimationUtil;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.util.SharedPreferencesManage;
import com.chanfinecloud.cflforemployee.weidgt.imagepreview.ImagePreviewListAdapter;
import com.chanfinecloud.cflforemployee.weidgt.imagepreview.ImageViewInfo;
import com.chanfinecloud.cflforemployee.weidgt.photopicker.PhotoPicker;
import com.chanfinecloud.cflforemployee.weidgt.wheelview.WheelDialog;
import com.google.gson.Gson;
import com.idlestar.ratingstar.RatingStarView;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.chanfinecloud.cflforemployee.config.Config.BASE_URL;
import static com.chanfinecloud.cflforemployee.config.Config.WORKORDER;

/**
 * Created by Loong on 2020/2/21.
 * Version: 1.0
 * Describe: 通用流程处理界面
 */

@ContentView(R.layout.workflow_action_layout)
public class WorkflowActionFragment extends BaseFragment {
    public WorkflowActionFragment() {
    }
    public WorkflowActionFragment newInstance(Bundle bundle) {
        WorkflowActionFragment fragment = new WorkflowActionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @ViewInject(R.id.workflow_action_name)
    private TextView workflow_action_name;
    @ViewInject(R.id.workflow_action_toggle_icon)
    private ImageView workflow_action_toggle_icon;
    @ViewInject(R.id.workflow_action_content)
    private FrameLayout workflow_action_content;
    @ViewInject(R.id.workflow_action_content_ll)
    private LinearLayout workflow_action_content_ll;


    private Activity context;
    private String action,businessId;
    private boolean permission;
    private WorkflowProcessesEntity workflowProcesses;
    private List<OperationInfoEntity> operationInfo;
    private ComplainDetailsEntity complainDetail;
    private OrderDetailsEntity orderDetail;
    private boolean toggle=true;

    private List<UserEntity> directorList;
    private List<UserEntity> employeeList;

    private List<ImageViewInfo> picList=new ArrayList<>();
    private ImagePreviewListAdapter adapter;

    private WorkflowType workflowType;
    private List<WorkflowViewTagEntity> workflowViewTagList =new ArrayList<>();


    private WheelDialog wheeldialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        permission=getArguments().getBoolean("permission");
        action = getArguments().getString("action");
        businessId = getArguments().getString("businessId");
        workflowType= (WorkflowType) getArguments().getSerializable("workflowType");
        if(workflowType==WorkflowType.Complain){
            complainDetail= (ComplainDetailsEntity) getArguments().getSerializable("complainDetail");
        }else{
            orderDetail= (OrderDetailsEntity) getArguments().getSerializable("orderDetail");
        }
        workflowProcesses = (WorkflowProcessesEntity) getArguments().getSerializable("workflowProcesses");
        operationInfo=workflowProcesses.getOperationInfos();

        directorList= SharedPreferencesManage.getDirectorList();
        employeeList=SharedPreferencesManage.getEmployeeList();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        workflow_action_name.setText(action);
        initActionView();
    }

    /**
     * 照片选择回填
     * @param imageViewInfo 图片实体
     */
    public void setPicData(ImageViewInfo imageViewInfo){
        picList.add(picList.size()-1,imageViewInfo);
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化流程表单视图-spinner
     * @param label 表单item名称
     * @param type 下拉类型(主管、员工、紧急程度)
     * @return WorkflowViewEntity
     */
    private WorkflowViewEntity initSpinner(String label,String type){
        View v = LayoutInflater.from(context).inflate(R.layout.workflow_action_spinner,null);
        TextView labelView=v.findViewById(R.id.action_spinner_label);
        labelView.setText(label);
        MaterialSpinner spinner=v.findViewById(R.id.action_spinner_spinner);
        List<String> adapterData=new ArrayList<>();
        if(type.equals("director")){
            for (int i = 0; i < directorList.size(); i++) {
                adapterData.add(directorList.get(i).getRealName());
            }
            spinner.setItems(adapterData);
            spinner.setTag(directorList.get(0).getId());
            spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    view.setTag(directorList.get(position).getId());
                }
            });
        }else if(type.equals("employee")){
            for (int i = 0; i < employeeList.size(); i++) {
                adapterData.add(employeeList.get(i).getRealName());
            }
            spinner.setItems(adapterData);
            spinner.setTag(employeeList.get(0).getId());
            spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    view.setTag(employeeList.get(position).getId());
                }
            });
        }else if(type.equals("emergency")){
            spinner.setItems(EmergencyLevelType.getEmergencyLevelTypeList());
            spinner.setTag(EmergencyLevelType.getEmergencyLevelTypeValue(0));
            spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    view.setTag(EmergencyLevelType.getEmergencyLevelTypeValue(position));
                }
            });
        }
        WorkflowViewEntity<MaterialSpinner> workflowView=new WorkflowViewEntity<>(v);
        workflowView.setLabel(labelView);
        workflowView.setContent(spinner);
        return workflowView;
    }

    /**
     * 初始化流程表单视图-date
     * @param label 表单item名称
     * @return WorkflowViewEntity
     */
    private WorkflowViewEntity initDateView(String label){
        View v = LayoutInflater.from(context).inflate(R.layout.workflow_action_date,null);
        TextView labelView=v.findViewById(R.id.action_date_label);
        labelView.setText(label);
        final TextView date=v.findViewById(R.id.action_date_content);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wheeldialog = new WheelDialog(context, R.style.Dialog_Floating, new WheelDialog.OnDateTimeConfirm() {
                    @Override
                    public void returnData(String dateText, String dateValue) {
                        wheeldialog.cancel();
                        date.setText(dateText);
                    }
                });
                wheeldialog.show();
            }
        });
        WorkflowViewEntity<TextView> workflowView=new WorkflowViewEntity<>(v);
        workflowView.setLabel(labelView);
        workflowView.setContent(date);
        return workflowView;
    }

    /**
     * 初始化流程表单视图-photo
     * @param label 表单item名称
     * @return WorkflowViewEntity
     */
    private WorkflowViewEntity initPhotoView(String label){
        View v = LayoutInflater.from(context).inflate(R.layout.workflow_action_photo,null);
        TextView labelView=v.findViewById(R.id.action_photo_label);
        labelView.setText(label);
        RecyclerView photo=v.findViewById(R.id.action_photo_content);
        picList.add(new ImageViewInfo("plus"));
        adapter=new ImagePreviewListAdapter(context,R.layout.item_workflow_image_perview_list,picList);
        photo.setLayoutManager(new GridLayoutManager(context,4));
        photo.setAdapter(adapter);
        photo.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(position==picList.size()-1){
                    if(permission){
                        int request_code=0;
                        switch (workflowType){
                            case Order:
                                request_code=OrderDetailActivity.REQUEST_CODE_CHOOSE;
                                break;
                            case Complain:
                                request_code= ComplainDetailActivity.REQUEST_CODE_CHOOSE;
                                break;
                        }
                        PhotoPicker.pick(context,10,true, request_code);
                    }else{
                        showToast("相机或读写手机存储的权限被禁止！");
                    }
                }
            }
        });

        WorkflowViewEntity<RecyclerView> workflowView=new WorkflowViewEntity<>(v);
        workflowView.setLabel(labelView);
        workflowView.setContent(photo);
        return workflowView;
    }

    /**
     * 初始化流程表单视图-remark
     * @param label 表单item名称
     * @return WorkflowViewEntity
     */
    private WorkflowViewEntity initRemarkView(String label){
        View v = LayoutInflater.from(context).inflate(R.layout.workflow_action_remark,null);
        TextView labelView=v.findViewById(R.id.action_remark_label);
        labelView.setText(label);
        EditText remark=v.findViewById(R.id.action_remark_content);
        WorkflowViewEntity<EditText> workflowView=new WorkflowViewEntity<>(v);
        workflowView.setLabel(labelView);
        workflowView.setContent(remark);
        return workflowView;
    }

    /**
     * 初始化流程表单视图-input
     * @param label 表单item名称
     * @return WorkflowViewEntity
     */
    private WorkflowViewEntity initInputView(String label){
        View v = LayoutInflater.from(context).inflate(R.layout.workflow_action_input,null);
        TextView labelView=v.findViewById(R.id.action_text_label);
        labelView.setText(label);
        EditText text=v.findViewById(R.id.action_text_content);
        WorkflowViewEntity<EditText> workflowView=new WorkflowViewEntity<>(v);
        workflowView.setLabel(labelView);
        workflowView.setContent(text);
        return workflowView;
    }

    /**
     * 初始化流程表单视图-rate
     * @param label 表单item名称
     * @return WorkflowViewEntity
     */
    private WorkflowViewEntity initRateView(String label){
        View v = LayoutInflater.from(context).inflate(R.layout.workflow_action_rate,null);
        TextView labelView=v.findViewById(R.id.action_rate_label);
        labelView.setText(label);
        RatingStarView rateView=v.findViewById(R.id.action_rate_content);
        WorkflowViewEntity<RatingStarView> workflowView=new WorkflowViewEntity<>(v);
        workflowView.setLabel(labelView);
        workflowView.setContent(rateView);
        return workflowView;
    }

    /**
     * 初始化流程表单视图-rate
     * @param btnText 提交按钮文字
     * @return WorkflowViewEntity
     */
    private WorkflowViewEntity initSubmitButtonView(String btnText){
        View v = LayoutInflater.from(context).inflate(R.layout.workflow_action_submit,null);
        Button submitView=v.findViewById(R.id.action_button_submit);
        submitView.setText(btnText);
        WorkflowViewEntity workflowView=new WorkflowViewEntity(v);
        workflowView.setSubmit(submitView);
        return workflowView;
    }

    /**
     * 初始化流程表单视图-choose
     * @param acceptText accept按钮文字
     * @param rejectText reject按钮文字
     * @return WorkflowViewEntity
     */
    private WorkflowViewEntity initChooseButtonView(String acceptText,String rejectText){
        View v = LayoutInflater.from(context).inflate(R.layout.workflow_action_button,null);
        Button acceptView=v.findViewById(R.id.action_button_accept);
        acceptView.setText(acceptText);
        Button rejectView=v.findViewById(R.id.action_button_reject);
        rejectView.setText(rejectText);
        WorkflowViewEntity workflowView=new WorkflowViewEntity(v);
        workflowView.setAccept(acceptView);
        workflowView.setReject(rejectView);
        return workflowView;
    }

    /**
     * 初始化流程表单视图-text
     * @param label 表单item名称
     * @return WorkflowViewEntity
     */
    private WorkflowViewEntity initTextView(String label){
        View v = LayoutInflater.from(context).inflate(R.layout.workflow_action_text,null);
        TextView labelView=v.findViewById(R.id.action_text_label);
        labelView.setText(label);
        TextView text=v.findViewById(R.id.action_text_content);
        WorkflowViewEntity<TextView> workflowView=new WorkflowViewEntity<>(v);
        workflowView.setLabel(labelView);
        workflowView.setContent(text);
        return workflowView;
    }

    /**
     * 初始化流程表单
     */
    private void initActionView(){
        List<WorkflowFormContentEntity> formContent= workflowProcesses.getFormContent();
        for (int i = 0; i < formContent.size(); i++) {
            WorkflowViewEntity workflowViewEntity=null;
            if("choose".equals(formContent.get(i).getFormItemType())){
                String label=formContent.get(i).getFormItemLabel();
                String[] labels=label.split(",");
                if(labels.length>=2){
                    workflowViewEntity=initChooseButtonView(labels[0],labels[1]);
                }else{
                    workflowViewEntity=initChooseButtonView("接受","不接受");
                }
                workflowViewEntity.getAccept().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int j = 0; j < operationInfo.size(); j++) {
                            if(operationInfo.get(j).getChoose()==1){
                                initHandleMap(j);
                            }
                        }
                    }
                });
                workflowViewEntity.getReject().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int j = 0; j < operationInfo.size(); j++) {
                            if(operationInfo.get(j).getChoose()==0){
                                initHandleMap(j);
                            }
                        }
                    }
                });
            }else if("director".equals(formContent.get(i).getFormItemType())){
                workflowViewEntity=initSpinner(formContent.get(i).getFormItemLabel(),"director");
            }else if("employee".equals(formContent.get(i).getFormItemType())){
                workflowViewEntity=initSpinner(formContent.get(i).getFormItemLabel(),"employee");
            }else if("emergency".equals(formContent.get(i).getFormItemType())){
                workflowViewEntity=initSpinner(formContent.get(i).getFormItemLabel(),"emergency");
            }else if("pick_photo".equals(formContent.get(i).getFormItemType())){
                workflowViewEntity=initPhotoView(formContent.get(i).getFormItemLabel());
            }else if("textarea".equals(formContent.get(i).getFormItemType())){
                workflowViewEntity=initRemarkView(formContent.get(i).getFormItemLabel());
            }else if("pick_date".equals(formContent.get(i).getFormItemType())){
                workflowViewEntity=initDateView(formContent.get(i).getFormItemLabel());
            }else if("rate".equals(formContent.get(i).getFormItemType())){
                workflowViewEntity=initRateView(formContent.get(i).getFormItemLabel());
            }else if("input_number".equals(formContent.get(i).getFormItemType())){
                workflowViewEntity=initInputView(formContent.get(i).getFormItemLabel());
                EditText et= (EditText) workflowViewEntity.getContent();
                et.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }else if("text".equals(formContent.get(i).getFormItemType())){
                workflowViewEntity=initTextView(formContent.get(i).getFormItemLabel());
                TextView textView= (TextView) workflowViewEntity.getContent();
                String textValue;
                if(workflowType==WorkflowType.Complain){
                    textValue= (String) getFieldValueByName(formContent.get(i).getFieldName(),complainDetail);
                }else{
                    textValue= (String) getFieldValueByName(formContent.get(i).getFieldName(),orderDetail);
                }
                textView.setText(textValue);
            }else if("submit".equals(formContent.get(i).getFormItemType())){
                workflowViewEntity=initSubmitButtonView(formContent.get(i).getFormItemLabel());
                workflowViewEntity.getSubmit().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initHandleMap(0);
                    }
                });
            }
            if(workflowViewEntity!=null){
                workflow_action_content_ll.addView(workflowViewEntity.getView());
                workflowViewTagList.add(new WorkflowViewTagEntity(formContent.get(i).getFormItemType(),formContent.get(i).getFormKey(),workflowViewEntity));
            }
        }
    }

    /**
     * 根据属性名获取属性值
     * */
    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
            return null;
        }
    }

    /**
     * 初始化表单参数处理
     * @param operationIndex choose index
     */
    private void initHandleMap(int operationIndex){
        Map<String,Object> map=new HashMap<>();

        for (int i = 0; i < workflowViewTagList.size(); i++) {
            WorkflowViewTagEntity workflowViewTag=workflowViewTagList.get(i);
            if("pick_photo".equals(workflowViewTag.getFormType())){
                switch (workflowType){
                    case Order:
                        map.put(workflowViewTag.getFormKey(),((OrderDetailActivity)context).resourceKey);
                        break;
                    case Complain:
                        map.put(workflowViewTag.getFormKey(),((ComplainDetailActivity)context).resourceKey);
                        break;
                }
            }else if("textarea".equals(workflowViewTag.getFormType())){
                EditText remark= (EditText) workflowViewTag.getWorkflowView().getContent();
                map.put(workflowViewTag.getFormKey(),remark.getText().toString());
            }else if("director".equals(workflowViewTag.getFormType())||"employee".equals(workflowViewTag.getFormType())||"emergency".equals(workflowViewTag.getFormType())){
                MaterialSpinner assignId= (MaterialSpinner) workflowViewTag.getWorkflowView().getContent();
                map.put(workflowViewTag.getFormKey(),assignId.getTag().toString());
            }else if("pick_date".equals(workflowViewTag.getFormType())){
                TextView date= (TextView) workflowViewTag.getWorkflowView().getContent();
                map.put(workflowViewTag.getFormKey(),date.getTag().toString());
            }else if("rate".equals(workflowViewTag.getFormType())){
                RatingStarView rate= (RatingStarView) workflowViewTag.getWorkflowView().getContent();
                map.put(workflowViewTag.getFormKey(),rate.getRating());
            }else if("input_number".equals(workflowViewTag.getFormType())){
                EditText manualCost= (EditText) workflowViewTag.getWorkflowView().getContent();
                map.put(workflowViewTag.getFormKey(),Double.parseDouble(manualCost.getText().toString()));
            }
        }
        map.put("businessId",businessId);
        map.put("choose",operationInfo.get(operationIndex).getChoose());
        map.put("operationDesc",operationInfo.get(operationIndex).getDesc());
        map.put("operationId",operationInfo.get(operationIndex).getId());
        map.put("operationName",operationInfo.get(operationIndex).getName());

        Gson gson=new Gson();
        LogUtil.d(gson.toJson(map));
        workflowAction(map);
    }

    @Event({R.id.workflow_action_toggle})
    private void onClickEvent(View v){
        switch (v.getId()){
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

    /**
     * 初始化流程处理请求参数
     * @param map
     */
    private void workflowAction(Map<String,Object> map){
        RequestParam requestParam=new RequestParam(BASE_URL+WORKORDER+"workflow/api/push/"+workflowType.getType(),HttpMethod.Post);
        requestParam.setRequestMap(map);
        requestParam.setParamType(ParamType.Json);
        requestParam.setCallback(myCallBack);
        sendRequest(requestParam,true);
    }

    /**
     * 请求回调
     */
    private MyCallBack myCallBack=new MyCallBack<String>(){
        @Override
        public void onSuccess(String result) {
            super.onSuccess(result);
            LogUtils.d("result",result);
            BaseEntity baseEntity= JsonParse.parse(result);
            if(baseEntity.isSuccess()){
                EventBus.getDefault().post(new EventBusMessage<>("WorkflowActionRefresh"));
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
