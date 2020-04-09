package com.chanfinecloud.cflforemployee.weidgt.workflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.entity.EmergencyLevelType;
import com.chanfinecloud.cflforemployee.entity.UserEntity;
import com.chanfinecloud.cflforemployee.entity.WorkflowViewEntity;
import com.chanfinecloud.cflforemployee.weidgt.imagepreview.ImagePreviewListAdapter;
import com.idlestar.ratingstar.RatingStarView;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Loong on 2020/4/9.
 * Version: 1.0
 * Describe:
 */
public class WorkflowFormUtil {


    /**
     * 初始化流程表单视图-assign(人员指派)
     * @param label 表单item名称
     * @return WorkflowViewEntity
     */
    public static WorkflowViewEntity initAssignView(Context context, String label, final List<UserEntity> userList ){
        View v = LayoutInflater.from(context).inflate(R.layout.workflow_action_spinner,null);
        TextView labelView=v.findViewById(R.id.action_spinner_label);
        labelView.setText(label);
        MaterialSpinner spinner=v.findViewById(R.id.action_spinner_spinner);
        List<String> adapterData=new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            adapterData.add(userList.get(i).getRealName());
        }
        spinner.setItems(adapterData);
        spinner.setTag(userList.get(0).getId());
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                view.setTag(userList.get(position).getId());
            }
        });
        WorkflowViewEntity<MaterialSpinner> workflowView=new WorkflowViewEntity<>(v);
        workflowView.setLabel(labelView);
        workflowView.setContent(spinner);
        return workflowView;
    }

    /**
     * 初始化流程表单视图-emergencyLevel(紧急程度)
     * @param label 表单item名称
     * @return WorkflowViewEntity
     */
    public static WorkflowViewEntity initEmergencyLevelView(Context context,String label){
        View v = LayoutInflater.from(context).inflate(R.layout.workflow_action_spinner,null);
        TextView labelView=v.findViewById(R.id.action_spinner_label);
        labelView.setText(label);
        MaterialSpinner spinner=v.findViewById(R.id.action_spinner_spinner);
        spinner.setItems(EmergencyLevelType.getEmergencyLevelTypeList());
        spinner.setTag(EmergencyLevelType.getEmergencyLevelTypeValue(0));
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                view.setTag(EmergencyLevelType.getEmergencyLevelTypeValue(position));
            }
        });
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
    public static WorkflowViewEntity initDateView(Context context, String label, View.OnClickListener listener){
        View v = LayoutInflater.from(context).inflate(R.layout.workflow_action_date,null);
        TextView labelView=v.findViewById(R.id.action_date_label);
        labelView.setText(label);
        TextView date=v.findViewById(R.id.action_date_content);
        date.setOnClickListener(listener);
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
    public static WorkflowViewEntity initPhotoView(Context context,String label,ImagePreviewListAdapter adapter,OnItemClickListener listener){
        View v = LayoutInflater.from(context).inflate(R.layout.workflow_action_photo,null);
        TextView labelView=v.findViewById(R.id.action_photo_label);
        labelView.setText(label);
        RecyclerView photo=v.findViewById(R.id.action_photo_content);
        photo.setLayoutManager(new GridLayoutManager(context,4));
        photo.setAdapter(adapter);
        photo.addOnItemTouchListener(listener);

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
    public static WorkflowViewEntity initRemarkView(Context context,String label){
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
    public static WorkflowViewEntity initInputView(Context context,String label){
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
    public static WorkflowViewEntity initRateView(Context context,String label){
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
    public static WorkflowViewEntity initSubmitButtonView(Context context,String btnText){
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
    public static WorkflowViewEntity initChooseButtonView(Context context,String acceptText,String rejectText){
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
    public static WorkflowViewEntity initTextView(Context context,String label){
        View v = LayoutInflater.from(context).inflate(R.layout.workflow_action_text,null);
        TextView labelView=v.findViewById(R.id.action_text_label);
        labelView.setText(label);
        TextView text=v.findViewById(R.id.action_text_content);
        WorkflowViewEntity<TextView> workflowView=new WorkflowViewEntity<>(v);
        workflowView.setLabel(labelView);
        workflowView.setContent(text);
        return workflowView;
    }

}
