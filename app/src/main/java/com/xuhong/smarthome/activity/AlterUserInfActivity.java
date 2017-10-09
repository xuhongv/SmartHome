package com.xuhong.smarthome.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gyf.barlibrary.ImmersionBar;
import com.squareup.picasso.Picasso;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.bean.User;
import com.xuhong.smarthome.utils.SharePreUtils;
import com.xuhong.smarthome.view.CircleTransform;

import cn.bmob.v3.BmobUser;

public class AlterUserInfActivity extends BaseActivity {

    //用户名
    private EditText register_et_name;
    //用户邮箱
    private EditText register_et_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_user_inf);
        initView();
    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImmersionBar.setTitleBar(this, toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView ivCameraBg = (ImageView) findViewById(R.id.ivCameraBg);
        User userInfo = BmobUser.getCurrentUser(User.class);
        if (userInfo != null) {

            //设置头像
            Picasso.with(this)
                    .load(userInfo.getNick())
                    .transform(new CircleTransform())
                    .into(ivCameraBg);

            //用户名
            register_et_name = (EditText) findViewById(R.id.register_et_name);
            register_et_name.setHint(userInfo.getUsername());


            //用户邮箱
            register_et_email = (EditText) findViewById(R.id.register_et_email);
            register_et_email.setHint(userInfo.getEmail());

        }


    }
}
