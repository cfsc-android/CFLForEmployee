package com.chanfinecloud.cflforemployee.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.adapter.OrderListAdapter;
import com.chanfinecloud.cflforemployee.base.BaseActivity;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.ListLoadingType;
import com.chanfinecloud.cflforemployee.entity.OrderEntity;
import com.chanfinecloud.cflforemployee.entity.OrderListEntity;
import com.chanfinecloud.cflforemployee.util.LogUtils;
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

@ContentView(R.layout.activity_order)
public class OrderActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_tv_title)
    TextView toolbar_title;
    @ViewInject(R.id.toolbar_btn_action)
    ImageButton toolbar_btn_action;
    @ViewInject(R.id.order_srl)
    private SmartRefreshLayout order_srl;
    @ViewInject(R.id.order_rlv)
    private RecyclerView order_rlv;

    private OrderListAdapter adapter;
    private List<OrderEntity> data=new ArrayList<>();
    private ListLoadingType loadType;
    private int page=1;
    private int pageSize=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar_title.setText("工单");
        toolbar_btn_action.setBackgroundResource(R.drawable.icon_btn_add);
        toolbar_btn_action.setVisibility(View.VISIBLE);
        adapter=new OrderListAdapter(this,data);
        order_rlv.setLayoutManager(new LinearLayoutManager(this));
        order_rlv.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        order_rlv.setAdapter(adapter);
        order_rlv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle=new Bundle();
                bundle.putString("order_id",data.get(position).getId());
                startActivity(OrderDetailActivity.class,bundle);
            }
        });

        order_srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page=1;
                getData();
                loadType=ListLoadingType.Refresh;
            }
        });
        order_srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page++;
                getData();
                loadType=ListLoadingType.LoadMore;
            }
        });
        order_srl.autoRefresh();
    }

    @Event({R.id.toolbar_btn_back,R.id.toolbar_btn_action})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                finish();
                break;
            case R.id.toolbar_btn_action:
                startActivity(AddOrderActivity.class);
                break;
        }
    }

    private void getData(){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/order/api/user/queryWorkOrderList");
        requestParam.setMethod(HttpMethod.PostJson);
        Map<String,Object> map=new HashMap<>();
        map.put("pageNo",page);
        map.put("pageSize",pageSize);
        requestParam.setPostJsonRequest(map);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity<OrderListEntity> baseEntity= JsonParse.parse(result,OrderListEntity.class);
                if(baseEntity.isSuccess()){
                    if(page==1){
                        data.clear();
                    }
                    data.addAll(baseEntity.getResult().getData());
                    adapter.notifyDataSetChanged();
                    if(loadType==ListLoadingType.Refresh){
                        order_srl.finishRefresh();
                        if(page*pageSize>=baseEntity.getResult().getCount()){
                            order_srl.setNoMoreData(true);
                        }
                    }else{
                        if(page*pageSize>=baseEntity.getResult().getCount()){
                            order_srl.finishLoadMoreWithNoMoreData();
                        }else{
                            order_srl.finishLoadMore();
                        }
                    }

                }else{
                    if(loadType==ListLoadingType.Refresh){
                        order_srl.finishRefresh();
                    }else{
                        page--;
                        order_srl.finishLoadMore(false);
                    }
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
