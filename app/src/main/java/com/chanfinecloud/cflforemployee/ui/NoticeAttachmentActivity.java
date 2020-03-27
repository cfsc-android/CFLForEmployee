package com.chanfinecloud.cflforemployee.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.adapter.AttachmentListAdapter;
import com.chanfinecloud.cflforemployee.ui.base.BaseActivity;
import com.chanfinecloud.cflforemployee.config.Config;
import com.chanfinecloud.cflforemployee.entity.ResourceEntity;
import com.chanfinecloud.cflforemployee.util.FilePathUtil;
import com.chanfinecloud.cflforemployee.http.HttpMethod;
import com.chanfinecloud.cflforemployee.http.MyProgressCallBack;
import com.chanfinecloud.cflforemployee.http.RequestParam;
import com.chanfinecloud.cflforemployee.weidgt.RecyclerViewDivider;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.List;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.chanfinecloud.cflforemployee.config.Config.File_DIR_NAME;
import static com.chanfinecloud.cflforemployee.config.Config.SD_APP_DIR_NAME;


@ContentView(R.layout.activity_notice_attachment)
public class NoticeAttachmentActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_tv_title)
    TextView toolbar_title;

    @ViewInject(R.id.notice_attachment_rlv)
    private RecyclerView notice_attachment_rlv;

    private AttachmentListAdapter adapter;
    private List<ResourceEntity> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data= (List<ResourceEntity>) getIntent().getExtras().getSerializable("resourceList");
        adapter=new AttachmentListAdapter(data);
        notice_attachment_rlv.setLayoutManager(new LinearLayoutManager(this));
        notice_attachment_rlv.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        notice_attachment_rlv.setAdapter(adapter);
        notice_attachment_rlv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                downloadAttachment(data.get(position).getUrl(),data.get(position).getName(),data.get(position).getContentType());
            }
        });
    }

    private void downloadAttachment(String url, String path, final String type){
        String _Path = FilePathUtil.createPathIfNotExist("/" + SD_APP_DIR_NAME + "/" + File_DIR_NAME);
        final File file = new File(_Path+"/"+path);
        if (file.exists()) {
            openFile(file,type);
        } else {
            RequestParam requestParam=new RequestParam(url,HttpMethod.Download);
            requestParam.setFilepath(_Path+"/"+path);
            requestParam.setProgressCallback(new MyProgressCallBack<File>(){
                @Override
                public void onSuccess(File result) {
                    super.onSuccess(result);
                    openFile(result,type);
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

    }


    private void openFile(File file,String type){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//设置标记
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(Intent.ACTION_VIEW);//动作，查看
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = Config.PROVIDER_AUTHORITY;
            uri = FileProvider.getUriForFile(this, authority, file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, type);//设置类型
        startActivity(intent);
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
