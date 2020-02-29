package com.chanfinecloud.cflforemployee.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.adapter.ComplainListAdapter;
import com.chanfinecloud.cflforemployee.base.BaseActivity;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.ComplainEntity;
import com.chanfinecloud.cflforemployee.entity.ComplainListEntity;
import com.chanfinecloud.cflforemployee.entity.ListLoadingType;
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

@ContentView(R.layout.activity_complain)
public class ComplainActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_tv_title)
    TextView toolbar_title;
    @ViewInject(R.id.complain_srl)
    private SmartRefreshLayout complain_srl;
    @ViewInject(R.id.complain_rlv)
    private RecyclerView complain_rlv;

    private ComplainListAdapter adapter;
    private List<ComplainEntity> data=new ArrayList<>();
    private ListLoadingType loadType;
    private int page=1;
    private int pageSize=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar_title.setText("投诉");

        adapter=new ComplainListAdapter(this,data);
        complain_rlv.setLayoutManager(new LinearLayoutManager(this));
        complain_rlv.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        complain_rlv.setAdapter(adapter);
        complain_rlv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle=new Bundle();
                bundle.putString("complain_id",data.get(position).getId());
                startActivity(ComplainDetailActivity.class,bundle);
            }
        });

        complain_srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page=1;
                getData();
                loadType=ListLoadingType.Refresh;
            }
        });
        complain_srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page++;
                getData();
                loadType=ListLoadingType.LoadMore;
            }
        });
        complain_srl.autoRefresh();
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
        requestParam.setUrl(BASE_URL+"work/complaintOwner/queryComplainList");
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
                BaseEntity<ComplainListEntity> baseEntity= JsonParse.parse(result,ComplainListEntity.class);
                if(baseEntity.isSuccess()){
                    if(page==1){
                        data.clear();
                    }
                    data.addAll(baseEntity.getResult().getData());
                    adapter.notifyDataSetChanged();
                    if(loadType==ListLoadingType.Refresh){
                        complain_srl.finishRefresh();
                        if(page*pageSize>=baseEntity.getResult().getCount()){
                            complain_srl.setNoMoreData(true);
                        }
                    }else{
                        if(page*pageSize>=baseEntity.getResult().getCount()){
                            complain_srl.finishLoadMoreWithNoMoreData();
                        }else{
                            complain_srl.finishLoadMore();
                        }
                    }

                }else{
                    if(loadType==ListLoadingType.Refresh){
                        complain_srl.finishRefresh();
                    }else{
                        page--;
                        complain_srl.finishLoadMore(false);
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
