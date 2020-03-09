package com.chanfinecloud.cflforemployee.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.adapter.ComplainListAdapter;
import com.chanfinecloud.cflforemployee.adapter.OrderListAdapter;
import com.chanfinecloud.cflforemployee.base.BaseFragment;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.ComplainEntity;
import com.chanfinecloud.cflforemployee.entity.ComplainListEntity;
import com.chanfinecloud.cflforemployee.entity.HomeTodoType;
import com.chanfinecloud.cflforemployee.entity.ListLoadingType;
import com.chanfinecloud.cflforemployee.entity.OrderEntity;
import com.chanfinecloud.cflforemployee.entity.OrderListEntity;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.util.SharedPreferencesManage;
import com.chanfinecloud.cflforemployee.util.http.HttpMethod;
import com.chanfinecloud.cflforemployee.util.http.JsonParse;
import com.chanfinecloud.cflforemployee.util.http.MyCallBack;
import com.chanfinecloud.cflforemployee.util.http.RequestParam;
import com.chanfinecloud.cflforemployee.weidgt.RecyclerViewDivider;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.chanfinecloud.cflforemployee.base.Config.BASE_URL;

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

    @ViewInject(R.id.home_todo_rlv)
    private RecyclerView home_todo_rlv;

    private OrderListAdapter orderAdapter;
    private ComplainListAdapter complainAdapter;
    private List<OrderEntity> orderData=new ArrayList<>();
    private List<ComplainEntity> complainData=new ArrayList<>();

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
        if(todoType==HomeTodoType.待处理工单.getType()){
            orderAdapter=new OrderListAdapter(context,orderData);
            orderAdapter.setEmptyView(emptyView);
            home_todo_rlv.setLayoutManager(new LinearLayoutManager(context));
            home_todo_rlv.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL));
            home_todo_rlv.setAdapter(orderAdapter);
            home_todo_rlv.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Bundle bundle=new Bundle();
                    bundle.putString("order_id",orderData.get(position).getId());
                    startActivity(OrderDetailActivity.class,bundle);
                }
            });
            getOrderData();
        }else if(todoType==HomeTodoType.待处理投诉.getType()){
            complainAdapter=new ComplainListAdapter(context,complainData);
            complainAdapter.setEmptyView(emptyView);
            home_todo_rlv.setLayoutManager(new LinearLayoutManager(context));
            home_todo_rlv.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL));
            home_todo_rlv.setAdapter(complainAdapter);
            home_todo_rlv.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Bundle bundle=new Bundle();
                    bundle.putString("complain_id",complainData.get(position).getId());
                    startActivity(ComplainDetailActivity.class,bundle);
                }
            });
            getComplainData();
        }else{
            orderAdapter=new OrderListAdapter(context,null);
            orderAdapter.setEmptyView(emptyView);
            home_todo_rlv.setLayoutManager(new LinearLayoutManager(context));
            home_todo_rlv.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL));
            home_todo_rlv.setAdapter(orderAdapter);
        }

    }

    private void getOrderData(){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/order/api/user/todo/"+ SharedPreferencesManage.getUserInfo().getUser().getId());
        requestParam.setMethod(HttpMethod.Get);
        Map<String,String> map=new HashMap<>();
        map.put("pageNo","1");
        map.put("pageSize","20");
        requestParam.setGetRequestMap(map);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity<OrderListEntity> baseEntity= JsonParse.parse(result,OrderListEntity.class);
                if(baseEntity.isSuccess()){
                    orderData.addAll(baseEntity.getResult().getData());
                    orderAdapter.notifyDataSetChanged();
                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }
        });
        sendRequest(requestParam,false);
    }

    private void getComplainData(){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/complaintOwner/todo/"+ SharedPreferencesManage.getUserInfo().getUser().getId());
        requestParam.setMethod(HttpMethod.Get);
        Map<String,String> map=new HashMap<>();
        map.put("pageNo","1");
        map.put("pageSize","20");
        requestParam.setGetRequestMap(map);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity<ComplainListEntity> baseEntity= JsonParse.parse(result,ComplainListEntity.class);
                if(baseEntity.isSuccess()){
                    complainData.addAll(baseEntity.getResult().getData());
                    complainAdapter.notifyDataSetChanged();
                }else{
                    showToast(baseEntity.getMessage());
                }
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
