package com.xuhong.smarthome.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.squareup.picasso.Picasso;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.view.BlurTransformation;
import com.xuhong.smarthome.view.CircleTransform;


public class MineFragment extends BaseFragment {


    private RelativeLayout all;

    private ImageView ivHeaderBg, ivmeIcon;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ImmersionBar.setTitleBar(getActivity(), toolbarl);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view) {
        all = (RelativeLayout) view.findViewById(R.id.all);
        ivHeaderBg = (ImageView) view.findViewById(R.id.ivHeaderBg);
        ivmeIcon = (ImageView) view.findViewById(R.id.ivIcon);

        Picasso.with(getActivity())
                .load(R.mipmap.me)
                .transform(new BlurTransformation(getActivity()))
                .fit()
                .into(ivHeaderBg);


        Picasso.with(getActivity())
                .load(R.mipmap.me)
                .transform(new CircleTransform())
                .into(ivmeIcon);



    }
}