package com.chanfinecloud.cflforemployee.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chanfinecloud.cflforemployee.R;
import com.chanfinecloud.cflforemployee.entity.ResourceEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Loong on 2020/2/28.
 * Version: 1.0
 * Describe:
 */
public class AttachmentListAdapter extends BaseQuickAdapter<ResourceEntity, BaseViewHolder> {
    public AttachmentListAdapter(@Nullable List<ResourceEntity> data) {
        super(R.layout.item_attachment_list,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ResourceEntity item) {
        helper.setText(R.id.attachment_tv_name,item.getName());
    }
}
