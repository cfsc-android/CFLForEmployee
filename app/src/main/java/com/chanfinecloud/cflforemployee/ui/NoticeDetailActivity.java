package com.chanfinecloud.cflforemployee.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.base.BaseActivity;
import com.chanfinecloud.cflforemployee.entity.BaseEntity;
import com.chanfinecloud.cflforemployee.entity.ListLoadingType;
import com.chanfinecloud.cflforemployee.entity.NoticeEntity;
import com.chanfinecloud.cflforemployee.entity.NoticeListEntity;
import com.chanfinecloud.cflforemployee.entity.OrderDetailsEntity;
import com.chanfinecloud.cflforemployee.util.LogUtils;
import com.chanfinecloud.cflforemployee.util.Utils;
import com.chanfinecloud.cflforemployee.util.http.HttpMethod;
import com.chanfinecloud.cflforemployee.util.http.JsonParse;
import com.chanfinecloud.cflforemployee.util.http.MyCallBack;
import com.chanfinecloud.cflforemployee.util.http.RequestParam;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.chanfinecloud.cflforemployee.base.Config.BASE_URL;


@ContentView(R.layout.activity_notice_detail)
public class NoticeDetailActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_tv_title)
    TextView toolbar_title;

    @ViewInject(R.id.notice_detail_tv_title)
    private TextView notice_detail_tv_title;
    @ViewInject(R.id.notice_detail_tv_publisher_name)
    private TextView notice_detail_tv_publisher_name;
    @ViewInject(R.id.notice_detail_tv_publisher_time)
    private TextView notice_detail_tv_publisher_time;
    @ViewInject(R.id.notice_detail_wv)
    private WebView notice_detail_wv;
    @ViewInject(R.id.notice_detail_tv_resource)
    private TextView notice_detail_tv_resource;
    @ViewInject(R.id.notice_detail_tv_count)
    private TextView notice_detail_tv_count;
    @ViewInject(R.id.notice_detail_iv_up)
    private ImageView notice_detail_iv_up;
    @ViewInject(R.id.notice_detail_tv_up)
    private TextView notice_detail_tv_up;
    @ViewInject(R.id.notice_detail_iv_share)
    private ImageView notice_detail_iv_share;


    private String title,noticeId;
    private NoticeEntity noticeEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title=getIntent().getExtras().getString("title");
        toolbar_title.setText(title);
        noticeId=getIntent().getExtras().getString("noticeId");
        getData();
        postPreview();
    }

    private void getData(){
        startProgressDialog();
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"smart/content/"+noticeId);
        requestParam.setMethod(HttpMethod.Get);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity<NoticeEntity> baseEntity= JsonParse.parse(result,NoticeEntity.class);
                if(baseEntity.isSuccess()){
                    noticeEntity=baseEntity.getResult();
                    init();
                }else{
                    showToast(baseEntity.getMessage());
                    stopProgressDialog();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
                stopProgressDialog();
            }
        });
        sendRequest(requestParam,true);
    }

    //预览+1
    private void postPreview(){
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(BASE_URL+"smart/contentThumbup/annoucementUp");
        requestParam.setMethod(HttpMethod.PostJson);
        Map<String,Object> map=new HashMap<>();
        map.put("announcementId",noticeId);
        map.put("field","browseNum");
        requestParam.setPostJsonRequest(map);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(!baseEntity.isSuccess()){
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

    //点赞/取消点赞
    private void thumbUp(final String flag){
        RequestParam requestParam=new RequestParam();
        if("0".equals(flag)){
            requestParam.setUrl(BASE_URL+"smart/contentThumbup/noLikeAnnouncement/"+noticeId);
        }else{
            requestParam.setUrl(BASE_URL+"smart/contentThumbup/likeAnnouncement/"+noticeId);

        }
        requestParam.setMethod(HttpMethod.Get);
        requestParam.setCallback(new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d("result",result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    if("0".equals(flag)){
                        noticeEntity.setIsThumbup("0");
                        noticeEntity.setPraiseNum(noticeEntity.getPraiseNum()-1);
                        notice_detail_tv_up.setText(noticeEntity.getPraiseNum()+"");
                        notice_detail_iv_up.setImageResource(R.drawable.ic_action_thumb_up_normal);
                    }else{
                        noticeEntity.setIsThumbup("1");
                        noticeEntity.setPraiseNum(noticeEntity.getPraiseNum()+1);
                        notice_detail_tv_up.setText(noticeEntity.getPraiseNum()+"");
                        notice_detail_iv_up.setImageResource(R.drawable.ic_action_thumb_up_press);
                    }
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
    private void init() {

        WebSettings settings = notice_detail_wv.getSettings();
        settings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        settings.setSupportZoom(false);
        // 设置默认缩放方式尺寸是far
        // shopElement.settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        // 设置出现缩放工具
        settings.setBuiltInZoomControls(false);
        // 让网页自适应屏幕宽度
        // shopElement.settings.setLayoutAlgorithm(
        // LayoutAlgorithm.SINGLE_COLUMN);
        int screenDensity = getResources().getDisplayMetrics().densityDpi;
        WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
        switch (screenDensity) {
            case DisplayMetrics.DENSITY_LOW:
                zoomDensity = WebSettings.ZoomDensity.CLOSE;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                break;
            case DisplayMetrics.DENSITY_HIGH:
            case DisplayMetrics.DENSITY_XHIGH:
            case DisplayMetrics.DENSITY_TV:
                zoomDensity = WebSettings.ZoomDensity.FAR;
                break;
            default:
                break;
        }
        settings.setDefaultZoom(zoomDensity);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        // 设置可以访问文件
        settings.setAllowFileAccess(true);
        settings.setDefaultTextEncodingName("utf-8");

        String url = noticeEntity.getDetailUrl();
        loadNoticeDetail(url);
        notice_detail_tv_title.setText(noticeEntity.getTitle());
        notice_detail_tv_publisher_name.setText(noticeEntity.getPublisher());
        notice_detail_tv_publisher_time.setText(noticeEntity.getPublishTime());
        notice_detail_tv_count.setText((noticeEntity.getBrowseNum()+1)+"");
        notice_detail_tv_up.setText(noticeEntity.getPraiseNum()+"");

        if (noticeEntity.getAttachments()==null||noticeEntity.getAttachments().size()==0) {
            notice_detail_tv_resource.setVisibility(View.GONE);
        }

        stopProgressDialog();
    }

    /**
     * @author TanYong
     * create at 2017/6/14 15:36
     * TODO：加载公告详情
     */
    private void loadNoticeDetail(String url) {
        notice_detail_wv.setHorizontalScrollBarEnabled(false);//水平不显示
        notice_detail_wv.setVerticalScrollBarEnabled(false); //垂直不显示

        notice_detail_wv.loadUrl(url);
        notice_detail_wv.setWebViewClient(new webViewClient());
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            imgReset();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void imgReset() {
        notice_detail_wv.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                " img.style.maxWidth = '100%';img.style.height='auto';" +
                "}" +
                "})()");
    }


    @Event({R.id.toolbar_btn_back,R.id.notice_detail_iv_up,R.id.notice_detail_iv_share,R.id.notice_detail_tv_resource})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                finish();
                break;
            case R.id.notice_detail_iv_up:
                if("1".equals(noticeEntity.getIsThumbup())){
                    thumbUp("0");
                }else{
                    thumbUp("1");
                }
                break;
            case R.id.notice_detail_iv_share:
                share();
                break;
            case R.id.notice_detail_tv_resource:
                Bundle bundle = new Bundle();
                bundle.putSerializable("resourceList", (Serializable) noticeEntity.getAttachments());
                startActivity(NoticeAttachmentActivity.class, bundle);
                break;
        }
    }

    /**
     * 分享
     */
    private void share(){
        String thumbUrl=noticeEntity.getCoverUrl();
        if(!TextUtils.isEmpty(thumbUrl)){
            x.image().loadFile(thumbUrl, Utils.getImageOption(), new Callback.CacheCallback<File>() {
                @Override
                public boolean onCache(File result) {
                    return false;
                }

                @Override
                public void onSuccess(File result) {
                    toShare(result);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }else{
            toShare(null);
        }
    }

    private void toShare(File file){
        UMImage thumb;
        if(file!=null){
            thumb =  new UMImage(NoticeDetailActivity.this,file);
        }else{
            thumb =  new UMImage(this, R.drawable.ic_launcher_background);
        }
        UMWeb web = new UMWeb(noticeEntity.getDetailUrl());
        web.setTitle("通知公告");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(noticeEntity.getTitle());//描述
        new ShareAction(NoticeDetailActivity.this)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
                .setCallback(shareListener).open();
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            showToast("成功了");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Log.d("错误日志",platform.toString());
            Log.d("错误日志",t.toString());
            showToast("失败了");
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            showShortToast("取消了");
        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
