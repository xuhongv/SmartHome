package com.xuhong.smarthome.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.squareup.picasso.Picasso;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.view.BlurTransformation;
import com.xuhong.smarthome.view.CircleTransform;
import com.xuhong.smarthome.view.PullScrollView;

public class LoginActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImmersionBar.setTitleBar(this, toolbar);
        ImageView ivHeaderBg = (ImageView) findViewById(R.id.ivHeaderBg);
        ImageView ivmeIcon = (ImageView) findViewById(R.id.ivIcon);
        PullScrollView pullView = (PullScrollView) findViewById(R.id.pullView);
        pullView.setZoomView(ivHeaderBg);


        Picasso.with(this)
                .load(R.mipmap.ic_mine_header_bg)
                .transform(new BlurTransformation(this))
                .fit()
                .into(ivHeaderBg);


        Picasso.with(this)
                .load(R.mipmap.ic_logo)
                .transform(new CircleTransform())
                .into(ivmeIcon);


    }


}

