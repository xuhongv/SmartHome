package com.xuhong.smarthome.fragment;

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
import com.xuhong.smarthome.view.BlurTransformation;
import com.xuhong.smarthome.view.CircleTransform;
import com.xuhong.smarthome.view.PullScrollView;


public class MineFragment extends BaseFragment {


    private ImageView ivHeaderBg, ivmeIcon;

    private PullScrollView pullView;

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

        ivHeaderBg = (ImageView) view.findViewById(R.id.ivHeaderBg);
        ivmeIcon = (ImageView) view.findViewById(R.id.ivIcon);
        pullView = (PullScrollView) view.findViewById(R.id.pullView);
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
}