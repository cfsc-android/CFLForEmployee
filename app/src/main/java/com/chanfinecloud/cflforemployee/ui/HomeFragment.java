package com.chanfinecloud.cflforemployee.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.chanfinecloud.cflforemployee.adapter.HomeTodoPagerAdapter;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.base.BaseFragment;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.HomeTodoType;
import com.chanfinecloud.cflforemployee.entity.ListLoadingType;
import com.chanfinecloud.cflforemployee.entity.NoticeEntity;
import com.chanfinecloud.cflforemployee.entity.NoticeListEntity;
import com.chanfinecloud.cflforemployee.entity.NoticeReceiverType;
import com.chanfinecloud.cflforemployee.entity.NoticeType;
import com.chanfinecloud.cflforemployee.entity.WorkflowType;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.util.http.HttpMethod;
import com.chanfinecloud.cflforemployee.util.http.JsonParse;
import com.chanfinecloud.cflforemployee.util.http.MyCallBack;
import com.chanfinecloud.cflforemployee.util.http.RequestParam;
import com.chanfinecloud.cflforemployee.weidgt.adtext.ADTextView;
import com.chanfinecloud.cflforemployee.weidgt.adtext.OnAdConetentClickListener;
import com.chanfinecloud.cflforemployee.weidgt.easyindicator.EasyIndicator;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import static com.chanfinecloud.cflforemployee.base.Config.BASE_URL;

/**
 * Created by Loong on 2020/2/12.
 * Version: 1.0
 * Describe: 首页
 */
@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {
    @ViewInject(R.id.home_ei_tab)
    private EasyIndicator home_ei_tab;
    @ViewInject(R.id.home_vp_tab)
    private ViewPager home_vp_tab;
    @ViewInject(R.id.home_ad_hot)
    private ADTextView home_ad_hot;

    private Context context;
    private HomeTodoPagerAdapter adapter;
    private ArrayList<HomeTodoFragment> data=new ArrayList<>();
    private ArrayList<NoticeEntity> hotTopicList = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        data.add(new HomeTodoFragment().newInstance(HomeTodoType.待处理工单));
        data.add(new HomeTodoFragment().newInstance(HomeTodoType.待处理投诉));
        data.add(new HomeTodoFragment().newInstance(HomeTodoType.待处理任务));
        adapter=new HomeTodoPagerAdapter(getChildFragmentManager(),1,getFragmentManager(),data);
        getData();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        home_ei_tab.setTabTitles(HomeTodoType.getPageNames());
        home_ei_tab.setViewPager(home_vp_tab, adapter);
        home_vp_tab.setOffscreenPageLimit(HomeTodoType.size() - 1);
        home_vp_tab.setCurrentItem(0);
    }


    private void getData(){
        RequestParam requestParam=new RequestParam(BASE_URL+"smart/content/pages",HttpMethod.Get);
        Map<String,String> map=new HashMap<>();
        map.put("projectId","ec93bb06f5be4c1f19522ca78180e2i9");
        map.put("receiver", NoticeReceiverType.全部.getType()+","+NoticeReceiverType.员工.getType());
        map.put("announcementTypeId", NoticeType.热点关注.getType());
        map.put("auditStatus","1");
        map.put("pageNo","1");
        map.put("pageSize","10");
        requestParam.setRequestMap(map);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity<NoticeListEntity> baseEntity= JsonParse.parse(result,NoticeListEntity.class);
                if(baseEntity.isSuccess()){
                    hotTopicList.addAll(baseEntity.getResult().getData());
                    initADTextView();
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

    private void initADTextView() {
        home_ad_hot.setSpeed(2);
        home_ad_hot.setData(hotTopicList);
        home_ad_hot.setMode(ADTextView.RunMode.UP);
        home_ad_hot.setOnAdConetentClickListener(new OnAdConetentClickListener() {
            @Override
            public void OnAdConetentClickListener(int index, NoticeEntity noticeEntity) {
                NoticeEntity selectNotice = hotTopicList.get(index);
                Bundle bundle = new Bundle();
                bundle.putString("title","热点关注");
                bundle.putString("noticeId", selectNotice.getId());
                startActivity(NoticeDetailActivity.class, bundle);

            }
        });
    }


    @Event({R.id.home_tv_order,R.id.home_tv_complain,R.id.home_tv_inspect,R.id.home_tv_task,R.id.home_tv_notice})
    private void onClickEvent(View v){
        Bundle bundle=new Bundle();
        switch (v.getId()){
            case R.id.home_tv_order:
//                startActivity(OrderActivity.class);
                bundle.putSerializable("workflowType", WorkflowType.Order);
                startActivity(WorkflowListActivity.class,bundle);
                break;
            case R.id.home_tv_complain:
//                startActivity(ComplainActivity.class);
                bundle.putSerializable("workflowType", WorkflowType.Complain);
                startActivity(WorkflowListActivity.class,bundle);
                break;
            case R.id.home_tv_inspect:
                showToast("巡检待开发");
                break;
            case R.id.home_tv_task:
                showToast("任务待开发");
                break;
            case R.id.home_tv_notice:
                startActivity(NoticeActivity.class);
                break;
        }
    }
}
