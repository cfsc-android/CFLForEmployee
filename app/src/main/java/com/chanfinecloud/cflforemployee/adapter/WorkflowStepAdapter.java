package com.chanfinecloud.cflforemployee.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.entity.WorkflowProcessesEntity;
import com.chanfinecloud.cflforemployee.util.LogUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Loong on 2020/2/20.
 * Version: 1.0
 * Describe:
 */
public class WorkflowStepAdapter extends BaseQuickAdapter<WorkflowProcessesEntity, BaseViewHolder> {
    private Context context;
    public WorkflowStepAdapter(Context context, @Nullable List<WorkflowProcessesEntity> data) {
        super(R.layout.item_workflow_step_list, data);
        this.context=context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, WorkflowProcessesEntity item) {
        LogUtils.d("position:"+helper.getAdapterPosition());
        if(helper.getAdapterPosition()==0){
            helper.setGone(R.id.workflow_step_up_line,false);
        }else{
            helper.setGone(R.id.workflow_step_up_line,true);
        }
        if(item.getUpdateTime()==null){
            helper.setText(R.id.workflow_step_date,"正在处理");
            helper.setText(R.id.workflow_step_time,"请耐心等待");
        }else{
            helper.setText(R.id.workflow_step_date,item.getUpdateTime().substring(0,10));
            helper.setText(R.id.workflow_step_time,item.getUpdateTime().substring(11));
        }

        ImageView avatar=helper.getView(R.id.workflow_step_avatar);
        if (item.getHandlerId() != null){
            Glide.with(context)
                    .load(item.getAvatarUrl())
                    .error(R.drawable.icon_user_default)
                    .circleCrop()
                    .into(avatar);
        } else{
            Glide.with(context)
                    .load(item.getAvatarUrl())
                    .error(R.drawable.icon_user_default_red)
                    .circleCrop()
                    .into(avatar);
        }
        helper.setText(R.id.workflow_step_press,item.getNodeName());
    }
}
