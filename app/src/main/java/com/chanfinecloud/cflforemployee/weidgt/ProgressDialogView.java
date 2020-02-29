package com.chanfinecloud.cflforemployee.weidgt;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanfinecloud.cflforemployee.R;


/**
 * Created by Loong on 2020/2/6.
 * Version: 1.0
 * Describe: 通用加载框
 */
public class ProgressDialogView {
    private ProgressDialog progressDialog = null;
    private ImageView progressImageView = null;
    private TextView loadingTitle = null;

    public void startLoad(Context context, String message, boolean cancelable){
        if (progressDialog != null) {
            stopLoad();
        }
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context, R.style.loading_dialog);
            View proView = LayoutInflater.from(context).inflate(R.layout.progress_dialog_view,null);
            progressDialog.show();
            progressDialog.setContentView(proView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            progressImageView = proView.findViewById(R.id.loading_animato);
            progressImageView.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(message)) {
                loadingTitle = proView.findViewById(R.id.loading_title);
                loadingTitle.setVisibility(View.VISIBLE);
                loadingTitle.setText(message);
            }

            progressImageView.setImageResource(R.drawable.kprogresshud_spinner);

            AnimationSet animationSet = new AnimationSet(false);
            RotateAnimation rotateAnimation = new RotateAnimation(0, 345,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(1000);
            rotateAnimation.setRepeatCount(-1);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            animationSet.addAnimation(rotateAnimation);
            progressImageView.startAnimation(animationSet);

            progressDialog.setCancelable(cancelable);
        }
    }

    public void stopLoad(){
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (progressImageView != null) {
            progressImageView.clearAnimation();
            progressImageView = null;
        }
    }

}
