package com.chanfinecloud.cflforemployee.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.base.BaseActivity;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.ComplainDetailsEntity;
import com.chanfinecloud.cflforemployee.entity.ComplainEntity;
import com.chanfinecloud.cflforemployee.entity.OrderDetailsEntity;
import com.chanfinecloud.cflforemployee.entity.OrderEntity;
import com.chanfinecloud.cflforemployee.entity.ResourceEntity;
import com.chanfinecloud.cflforemployee.entity.WorkflowComplainEntity;
import com.chanfinecloud.cflforemployee.entity.WorkflowOrderEntity;
import com.chanfinecloud.cflforemployee.util.DicDataTransUtil;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.util.http.HttpMethod;
import com.chanfinecloud.cflforemployee.util.http.JsonParse;
import com.chanfinecloud.cflforemployee.util.http.MyCallBack;
import com.chanfinecloud.cflforemployee.util.http.RequestParam;
import com.chanfinecloud.cflforemployee.weidgt.NoUnderlineSpan;
import com.chanfinecloud.cflforemployee.weidgt.imagepreview.ImagePreviewListAdapter;
import com.chanfinecloud.cflforemployee.weidgt.imagepreview.ImageViewInfo;
import com.chanfinecloud.cflforemployee.weidgt.imagepreview.PreviewBuilder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.chanfinecloud.cflforemployee.base.Config.BASE_URL;

@ContentView(R.layout.activity_complain_detail)
public class ComplainDetailActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_tv_title)
    TextView toolbar_title;
    @ViewInject(R.id.toolbar_tv_action)
    TextView toolbar_tv_action;
    @ViewInject(R.id.toolbar_btn_action)
    ImageButton toolbar_btn_action;

    @ViewInject(R.id.complain_detail_user_avatar)
    private ImageView complain_detail_user_avatar;
    @ViewInject(R.id.complain_detail_user_name)
    private TextView complain_detail_user_name;
    @ViewInject(R.id.complain_detail_user_room)
    private TextView complain_detail_user_room;
    @ViewInject(R.id.complain_detail_complain_type)
    private TextView complain_detail_complain_type;
    @ViewInject(R.id.complain_detail_contact_tel)
    private TextView complain_detail_contact_tel;
    @ViewInject(R.id.complain_detail_remark_text)
    private TextView complain_detail_remark_text;
    @ViewInject(R.id.complain_detail_remark_time)
    private TextView complain_detail_remark_time;


    @ViewInject(R.id.complain_detail_workflow_ll)
    private LinearLayout complain_detail_workflow_ll;
    @ViewInject(R.id.complain_detail_workflow_action_fl)
    private FrameLayout complain_detail_workflow_action_fl;

    private String complainId;
    private NoUnderlineSpan mNoUnderlineSpan;
    private List<WorkflowComplainEntity> data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar_title.setText("投诉详情");
        toolbar_btn_action.setVisibility(View.GONE);
        toolbar_tv_action.setText("进度");
        toolbar_tv_action.setVisibility(View.VISIBLE);

        complainId=getIntent().getExtras().getString("complain_id");
        getData();
        mNoUnderlineSpan = new NoUnderlineSpan();
    }

    @Event({R.id.toolbar_btn_back,R.id.toolbar_tv_action})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                finish();
                break;
            case R.id.toolbar_tv_action:
                Bundle bundle=new Bundle();
                bundle.putSerializable("complainWorkflowList", (Serializable) data);
                startActivity(WorkflowStepActivity.class,bundle);
                break;
        }
    }

    private void getData(){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"work/complaintOwner/getUserComplaintDetails/"+complainId);
        requestParam.setMethod(HttpMethod.Get);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity<ComplainDetailsEntity> baseEntity= JsonParse.parse(result,ComplainDetailsEntity.class);
                if(baseEntity.isSuccess()){
                    initView(baseEntity.getResult().getComplaint());
                    initWorkFlow(baseEntity.getResult().getCfcComplaintDetailsVo());
                    data.addAll(baseEntity.getResult().getCfcComplaintDetailsVo());
                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }

            @Override
            public void onFinished() {
                super.onFinished();
                stopProgressDialog();
            }
        });
        sendRequest(requestParam,true);
    }


    private void initView(ComplainEntity complainEntity){
        if(TextUtils.isEmpty(complainEntity.getCreatorAvatarUrl())){
            Glide.with(this)
                    .load(R.drawable.icon_user_default)
                    .error(R.drawable.ic_no_img)
                    .circleCrop()
                    .into(complain_detail_user_avatar);
        }else{
            Glide.with(this)
                    .load(complainEntity.getCreatorAvatarUrl())
                    .error(R.drawable.ic_no_img)
                    .circleCrop()
                    .into(complain_detail_user_avatar);
        }
        complain_detail_user_name.setText(complainEntity.getHouseholdName());
        complain_detail_user_room.setText(complainEntity.getRoomNameAll());
        complain_detail_complain_type.setText(DicDataTransUtil.getInstance().getComplainTypeChs(complainEntity.getTypeId()));
        if (!TextUtils.isEmpty(complainEntity.getHouseholdMobile())) {
            complain_detail_contact_tel.setText(complainEntity.getHouseholdMobile());
        } else {
            complain_detail_contact_tel.setVisibility(View.GONE);
        }
        if (complain_detail_contact_tel.getText() instanceof Spannable) {
            Spannable s = (Spannable) complain_detail_contact_tel.getText();
            s.setSpan(mNoUnderlineSpan, 0, s.length(), Spanned.SPAN_MARK_MARK);
        }
        complain_detail_remark_text.setText(complainEntity.getContent());
        complain_detail_remark_time.setText(complainEntity.getComplainTime());
    }

    private void initWorkFlow(final List<WorkflowComplainEntity> list){
        for (int i = 0; i < list.size(); i++) {
            View v = LayoutInflater.from(this).inflate(R.layout.item_workflow_list,null);
            ImageView item_workflow_avatar=v.findViewById(R.id.item_workflow_avatar);
            TextView item_workflow_user_name=v.findViewById(R.id.item_workflow_user_name);
            TextView item_workflow_user_role=v.findViewById(R.id.item_workflow_user_role);
            TextView item_workflow_tel=v.findViewById(R.id.item_workflow_tel);
            TextView item_workflow_content=v.findViewById(R.id.item_workflow_content);
            RecyclerView item_workflow_pic=v.findViewById(R.id.item_workflow_pic);
            TextView item_workflow_node=v.findViewById(R.id.item_workflow_node);
            TextView item_workflow_time=v.findViewById(R.id.item_workflow_time);
            WorkflowComplainEntity item=list.get(i);
            if(TextUtils.isEmpty(item.getAvatarUrl())){
                Glide.with(this)
                        .load(R.drawable.ic_launcher)
                        .circleCrop()
                        .error(R.drawable.ic_no_img)
                        .into(item_workflow_avatar);
            }else{
                Glide.with(this)
                        .load(item.getAvatarUrl())
                        .circleCrop()
                        .error(R.drawable.ic_no_img)
                        .into(item_workflow_avatar);
            }
            item_workflow_user_name.setText(item.getHandlerName());
            item_workflow_user_role.setText(item.getShortDesc());
            if (!TextUtils.isEmpty(item.getMobile())) {
                item_workflow_tel.setText(item.getMobile());
            } else {
                item_workflow_tel.setVisibility(View.GONE);
            }
            if (item_workflow_tel.getText() instanceof Spannable) {
                Spannable s = (Spannable) item_workflow_tel.getText();
                s.setSpan(mNoUnderlineSpan, 0, s.length(), Spanned.SPAN_MARK_MARK);
            }
            item_workflow_content.setText(item.getComment());
            item_workflow_node.setText(item.getNodeName());
            item_workflow_time.setText(item.getCreateTime());
            final List<ImageViewInfo> data=new ArrayList<>();
            List<ResourceEntity> picData=item.getResourceValue();
            if(picData!=null){
                for (int j = 0; j < picData.size(); j++) {
                    data.add(new ImageViewInfo(picData.get(j).getUrl()));
                }
            }
            final ImagePreviewListAdapter imageAdapter=new ImagePreviewListAdapter(this,R.layout.item_workflow_image_perview_list,data);
            final GridLayoutManager mGridLayoutManager = new GridLayoutManager(this,3);
            item_workflow_pic.setLayoutManager(mGridLayoutManager);
            item_workflow_pic.setAdapter(imageAdapter);
            item_workflow_pic.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    for (int k = mGridLayoutManager.findFirstVisibleItemPosition(); k < adapter.getItemCount(); k++) {
                        View itemView = mGridLayoutManager.findViewByPosition(k);
                        Rect bounds = new Rect();
                        if (itemView != null) {
                            ImageView imageView = itemView.findViewById(R.id.iiv_item_image_preview);
                            imageView.getGlobalVisibleRect(bounds);
                        }
                        //计算返回的边界
                        imageAdapter.getItem(k).setBounds(bounds);
                    }
                    PreviewBuilder.from(ComplainDetailActivity.this)
                            .setImgs(data)
                            .setCurrentIndex(position)
                            .setSingleFling(true)
                            .setType(PreviewBuilder.IndicatorType.Number)
                            .start();
                }
            });
            complain_detail_workflow_ll.addView(v);
        }



    }

}
