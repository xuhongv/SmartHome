package com.xuhong.smarthome.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.squareup.picasso.Picasso;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.view.BlurTransformation;
import com.xuhong.smarthome.view.CircleTransform;
import com.xuhong.smarthome.view.PullScrollView;

public class LoginActivity extends BaseActivity {


    Shimmer shimmer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImmersionBar.setTitleBar(this, toolbar);

        //默认头像
//        ImageView ivmeIcon = (ImageView) findViewById(R.id.ivIcon);
//        Picasso.with(this)
//                .load(R.mipmap.ic_logo)
//                .transform(new CircleTransform())
//                .into(ivmeIcon);

        //闪亮效果的文本显示
        shimmer = new Shimmer();
        ShimmerTextView shimmerTextView = (ShimmerTextView) findViewById(R.id.shimmer_login);
        shimmer.start(shimmerTextView);
    }


}

