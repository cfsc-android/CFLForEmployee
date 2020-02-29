package com.chanfinecloud.cflforemployee.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.base.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import androidx.annotation.Nullable;

/**
 * Created by Loong on 2020/2/21.
 * Version: 1.0
 * Describe:
 */

@ContentView(R.layout.workflow_action_layout)
public class WorkflowActionFragment extends BaseFragment {
    @ViewInject(R.id.workflow_action_name)
    private TextView workflow_action_name;

    private String action;

    public WorkflowActionFragment(String action) {
        this.action = action;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        workflow_action_name.setText(action);
    }
}
