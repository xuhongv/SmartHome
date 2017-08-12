package com.xuhong.smarthome.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.squareup.picasso.Picasso;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.activity.LoginActivity;
import com.xuhong.smarthome.view.BlurTransformation;
import com.xuhong.smarthome.view.CircleTransform;
import com.xuhong.smarthome.view.PullScrollView;


public class MineFragment extends BaseFragment implements View.OnClickListener {


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view) {

        ImageView ivHeaderBg = (ImageView) view.findViewById(R.id.ivHeaderBg);
        ImageView ivmeIcon = (ImageView) view.findViewById(R.id.ivIcon);
        ivmeIcon.setOnClickListener(this);
        PullScrollView pullView = (PullScrollView) view.findViewById(R.id.pullView);
        pullView.setZoomView(ivHeaderBg);

        Picasso.with(getActivity())
                .load(R.mipmap.ic_mine_header_bg)
                .transform(new BlurTransformation(getActivity()))
                .fit()
                .into(ivHeaderBg);


        Picasso.with(getActivity())
                .load(R.mipmap.ic_unget)
                .transform(new CircleTransform())
                .into(ivmeIcon);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivIcon:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
        }
    }
}