package com.chanfinecloud.cflforemployee.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.adapter.NoticeListAdapter;
import com.chanfinecloud.cflforemployee.ui.base.BaseActivity;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.ListLoadingType;
import com.chanfinecloud.cflforemployee.entity.NoticeEntity;
import com.chanfinecloud.cflforemployee.entity.NoticeListEntity;
import com.chanfinecloud.cflforemployee.entity.NoticeReceiverType;
import com.chanfinecloud.cflforemployee.entity.NoticeType;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.http.HttpMethod;
import com.chanfinecloud.cflforemployee.http.JsonParse;
import com.chanfinecloud.cflforemployee.http.MyCallBack;
import com.chanfinecloud.cflforemployee.http.RequestParam;
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

import static com.chanfinecloud.cflforemployee.config.Config.ARTICLE;
import static com.chanfinecloud.cflforemployee.config.Config.BASE_URL;

@ContentView(R.layout.activity_notice)
public class NoticeActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_tv_title)
    TextView toolbar_title;

    @ViewInject(R.id.notice_srl)
    private SmartRefreshLayout notice_srl;
    @ViewInject(R.id.notice_rlv)
    private RecyclerView notice_rlv;

    private NoticeListAdapter adapter;
    private List<NoticeEntity> data=new ArrayList<>();
    private ListLoadingType loadType;
    private int page=1;
    private int pageSize=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar_title.setText("社区公告");
        adapter=new NoticeListAdapter(this,data);
        notice_rlv.setLayoutManager(new LinearLayoutManager(this));
        notice_rlv.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        notice_rlv.setAdapter(adapter);
        notice_rlv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle=new Bundle();
                bundle.putString("title","社区公告");
                bundle.putString("noticeId",data.get(position).getId());
                startActivity(NoticeDetailActivity.class,bundle);
            }
        });

        notice_srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page=1;
                getData();
                loadType=ListLoadingType.Refresh;
            }
        });
        notice_srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page++;
                getData();
                loadType=ListLoadingType.LoadMore;
            }
        });
        notice_srl.autoRefresh();
    }

    /**
     * 获取新闻列表数据
     */
    private void getData(){
        RequestParam requestParam=new RequestParam(BASE_URL+ARTICLE+"smart/content/pages",HttpMethod.Get);
        Map<String,String> map=new HashMap<>();
        map.put("receiver", NoticeReceiverType.全部.getType()+","+NoticeReceiverType.员工.getType());
        map.put("announcementTypeId", NoticeType.社区公告.getType());
        map.put("auditStatus","1");
        map.put("pageNo",page+"");
        map.put("pageSize",pageSize+"");
        requestParam.setRequestMap(map);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity<NoticeListEntity> baseEntity= JsonParse.parse(result,NoticeListEntity.class);
                if(baseEntity.isSuccess()){
                    if(page==1){
                        data.clear();
                    }
                    data.addAll(baseEntity.getResult().getData());
                    adapter.notifyDataSetChanged();
                    if(loadType==ListLoadingType.Refresh){
                        notice_srl.finishRefresh();
                        if(page*pageSize>=baseEntity.getResult().getCount()){
                            notice_srl.setNoMoreData(true);
                        }
                    }else{
                        if(page*pageSize>=baseEntity.getResult().getCount()){
                            notice_srl.finishLoadMoreWithNoMoreData();
                        }else{
                            notice_srl.finishLoadMore();
                        }
                    }

                }else{
                    if(loadType==ListLoadingType.Refresh){
                        notice_srl.finishRefresh();
                    }else{
                        page--;
                        notice_srl.finishLoadMore(false);
                    }
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                if(loadType==ListLoadingType.Refresh){
                    notice_srl.finishRefresh();
                }else{
                    page--;
                    notice_srl.finishLoadMore(false);
                }
                showToast(ex.getMessage());
            }
        });
        sendRequest(requestParam,false);
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
