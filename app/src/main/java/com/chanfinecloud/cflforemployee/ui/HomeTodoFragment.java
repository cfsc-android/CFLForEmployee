package com.chanfinecloud.cflforemployee.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.adapter.WorkflowListAdapter;
import com.chanfinecloud.cflforemployee.ui.base.BaseFragment;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.HomeTodoType;
import com.chanfinecloud.cflforemployee.entity.ListLoadingType;
import com.chanfinecloud.cflforemployee.entity.WorkflowEntity;
import com.chanfinecloud.cflforemployee.entity.WorkflowListEntity;
import com.chanfinecloud.cflforemployee.entity.WorkflowType;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.http.HttpMethod;
import com.chanfinecloud.cflforemployee.http.JsonParse;
import com.chanfinecloud.cflforemployee.http.MyCallBack;
import com.chanfinecloud.cflforemployee.http.RequestParam;
import com.chanfinecloud.cflforemployee.weidgt.RecyclerViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.chanfinecloud.cflforemployee.config.Config.BASE_URL;
import static com.chanfinecloud.cflforemployee.config.Config.WORKORDER;

/**
 * Created by Loong on 2020/2/12.
 * Version: 1.0
 * Describe: 首页待处理列表
 */

@ContentView(R.layout.fragment_home_todo)
public class HomeTodoFragment extends BaseFragment {
    private static final String ARG = "todoType";
    public HomeTodoFragment() {
    }

    public HomeTodoFragment newInstance(HomeTodoType type) {
        HomeTodoFragment fragment = new HomeTodoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG, type.getType());
        fragment.setArguments(bundle);
        return fragment;
    }

    @ViewInject(R.id.home_todo_srl)
    private SmartRefreshLayout home_todo_srl;
    @ViewInject(R.id.home_todo_rlv)
    private RecyclerView home_todo_rlv;

    private WorkflowListAdapter adapter;
    private List<WorkflowEntity> data=new ArrayList<>();
    private ListLoadingType loadType;
    private int page=1;
    private int pageSize=10;

    private Context context;
    private int todoType;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        todoType = getArguments().getInt(ARG);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View emptyView= LayoutInflater.from(context).inflate(R.layout.item_empty_layout,null);
        adapter=new WorkflowListAdapter(context,data);
        adapter.setEmptyView(emptyView);
        home_todo_rlv.setLayoutManager(new LinearLayoutManager(context));
        home_todo_rlv.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL));
        home_todo_rlv.setAdapter(adapter);
        home_todo_rlv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle=new Bundle();
                bundle.putString("order_id",data.get(position).getId());
                startActivity(OrderDetailActivity.class,bundle);
            }
        });

        home_todo_srl.setPrimaryColorsId(R.color.view_background,R.color.text_primary);
        home_todo_srl.setRefreshHeader(new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate));
        home_todo_srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page=1;
                getData();
                loadType=ListLoadingType.Refresh;
            }
        });
        home_todo_srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page++;
                getData();
                loadType=ListLoadingType.LoadMore;
            }
        });
        getData();
    }

    /**
     * 获取待办数据
     */
    private void getData(){
        RequestParam requestParam=new RequestParam(BASE_URL+WORKORDER+"workflow/api/todo",HttpMethod.Get);
        Map<String,String> map=new HashMap<>();
        map.put("pageNo","1");
        map.put("pageSize","20");
        if(todoType==HomeTodoType.待处理工单.getType()){
            map.put("type", WorkflowType.Order.getType());
        }else if(todoType==HomeTodoType.待处理投诉.getType()){
            map.put("type", WorkflowType.Complain.getType());
        }
        requestParam.setRequestMap(map);
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
                        home_todo_srl.finishRefresh();
                        if(page*pageSize>=baseEntity.getResult().getCount()){
                            home_todo_srl.setNoMoreData(true);
                        }
                    }else{
                        if(page*pageSize>=baseEntity.getResult().getCount()){
                            home_todo_srl.finishLoadMoreWithNoMoreData();
                        }else{
                            home_todo_srl.finishLoadMore();
                        }
                    }

                }else{
                    if(loadType==ListLoadingType.Refresh){
                        home_todo_srl.finishRefresh();
                    }else{
                        page--;
                        home_todo_srl.finishLoadMore(false);
                    }
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                if(loadType==ListLoadingType.Refresh){
                    home_todo_srl.finishRefresh();
                }else{
                    page--;
                    home_todo_srl.finishLoadMore(false);
                }
                showToast(ex.getMessage());
            }
        });
        sendRequest(requestParam,false);
    }


}
