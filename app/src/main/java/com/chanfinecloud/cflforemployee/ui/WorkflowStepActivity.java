package com.chanfinecloud.cflforemployee.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.adapter.WorkflowStepAdapter;
import com.chanfinecloud.cflforemployee.base.BaseActivity;
import com.chanfinecloud.cflforemployee.entity.WorkflowProcessesEntity;
import com.chanfinecloud.cflforemployee.util.LogUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

@ContentView(R.layout.activity_workflow_step)
public class WorkflowStepActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_tv_title)
    TextView toolbar_title;
    @ViewInject(R.id.workflow_step_rlv)
    private RecyclerView workflow_step_rlv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar_title.setText("进度");
        if(getIntent().getExtras().getSerializable("workflowProcessesList")!=null){
            List<WorkflowProcessesEntity> data = (List<WorkflowProcessesEntity>) getIntent().getExtras().getSerializable("workflowProcessesList");
            if(data!=null){
                workflow_step_rlv.setLayoutManager(new LinearLayoutManager(this));
                workflow_step_rlv.setAdapter(new WorkflowStepAdapter(this,data));
                LogUtils.d("data:"+data.size());
            }
        }
    }

    @Event({R.id.toolbar_btn_back})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                finish();
                break;
        }
    }
}
