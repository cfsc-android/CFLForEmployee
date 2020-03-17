package com.chanfinecloud.cflforemployee.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.adapter.WorkflowListAdapter;
import com.chanfinecloud.cflforemployee.base.BaseActivity;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.FinishStatusType;
import com.chanfinecloud.cflforemployee.entity.ListLoadingType;
import com.chanfinecloud.cflforemployee.entity.UserType;
import com.chanfinecloud.cflforemployee.entity.WorkflowEntity;
import com.chanfinecloud.cflforemployee.entity.WorkflowListEntity;
import com.chanfinecloud.cflforemployee.entity.WorkflowType;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.util.SharedPreferencesManage;
import com.chanfinecloud.cflforemployee.util.http.HttpMethod;
import com.chanfinecloud.cflforemployee.util.http.JsonParse;
import com.chanfinecloud.cflforemployee.util.http.MyCallBack;
import com.chanfinecloud.cflforemployee.util.http.RequestParam;
import com.chanfinecloud.cflforemployee.weidgt.RecyclerViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.chanfinecloud.cflforemployee.base.Config.BASE_URL;

@ContentView(R.layout.activity_workflow_list)
public class WorkflowListActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_tv_title)
    TextView toolbar_title;
    @ViewInject(R.id.workflow_list_srl)
    private SmartRefreshLayout workflow_list_srl;
    @ViewInject(R.id.workflow_list_rlv)
    private RecyclerView workflow_list_rlv;

    private WorkflowListAdapter adapter;
    private List<WorkflowEntity> data=new ArrayList<>();
    private ListLoadingType loadType;
    private int page=1;
    private int pageSize=10;
    private WorkflowType workflowType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workflowType= (WorkflowType) getIntent().getExtras().getSerializable("workflowType");
        toolbar_title.setText(workflowType.getTypeChs());

        adapter=new WorkflowListAdapter(this,data);
        workflow_list_rlv.setLayoutManager(new LinearLayoutManager(this));
        workflow_list_rlv.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        workflow_list_rlv.setAdapter(adapter);
        workflow_list_rlv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle=new Bundle();
                if("1".equals(workflowType.getType())){//工单
                    bundle.putString("order_id",data.get(position).getId());
                    startActivity(OrderDetailActivity.class,bundle);
                }else if("2".equals(workflowType.getType())){//投诉
                    bundle.putString("complain_id",data.get(position).getId());
                    startActivity(ComplainDetailActivity.class,bundle);
                }

            }
        });

        workflow_list_srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page=1;
                getData();
                loadType=ListLoadingType.Refresh;
            }
        });
        workflow_list_srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page++;
                getData();
                loadType=ListLoadingType.LoadMore;
            }
        });
        workflow_list_srl.autoRefresh();
    }

    @Event({R.id.toolbar_btn_back})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                finish();
                break;
        }
    }

    private void getData(){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"workflow/api/page");
        requestParam.setMethod(HttpMethod.Get);
        Map<String,String> map=new HashMap<>();
        map.put("pageNo",page+"");
        map.put("pageSize",pageSize+"");
        map.put("isFinish", FinishStatusType.UnFinish.getType());
        map.put("type",workflowType.getType());
        map.put("userType", UserType.Employee.getType());
        requestParam.setGetRequestMap(map);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity<WorkflowListEntity> baseEntity= JsonParse.parse(result,WorkflowListEntity.class);
                if(baseEntity.isSuccess()){
                    if(page==1){
                        data.clear();
                    }
                    data.addAll(baseEntity.getResult().getData());
                    adapter.notifyDataSetChanged();
                    if(loadType==ListLoadingType.Refresh){
                        workflow_list_srl.finishRefresh();
                        if(page*pageSize>=baseEntity.getResult().getCount()){
                            workflow_list_srl.setNoMoreData(true);
                        }
                    }else{
                        if(page*pageSize>=baseEntity.getResult().getCount()){
                            workflow_list_srl.finishLoadMoreWithNoMoreData();
                        }else{
                            workflow_list_srl.finishLoadMore();
                        }
                    }

                }else{
                    if(loadType==ListLoadingType.Refresh){
                        workflow_list_srl.finishRefresh();
                    }else{
                        page--;
                        workflow_list_srl.finishLoadMore(false);
                    }
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                if(loadType==ListLoadingType.Refresh){
                    workflow_list_srl.finishRefresh();
                }else{
                    page--;
                    workflow_list_srl.finishLoadMore(false);
                }
                showToast(ex.getMessage());
            }
        });
        sendRequest(requestParam,false);
    }
}
