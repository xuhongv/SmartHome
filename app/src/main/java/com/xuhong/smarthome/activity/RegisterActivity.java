package com.xuhong.smarthome.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.xuhong.smarthome.R;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

        RelativeLayout rlUserName = (RelativeLayout) findViewById(R.id.rlUserName);
        rlUserName.setAlpha(0.3f);
        RelativeLayout rlUserPassword = (RelativeLayout) findViewById(R.id.rlUserPassword);
        rlUserPassword.setAlpha(0.3f);
        RelativeLayout rlUserPasswordComfir = (RelativeLayout) findViewById(R.id.rlUserPasswordComfir);
        rlUserPasswordComfir.setAlpha(0.3f);
        RelativeLayout rlUserEmail = (RelativeLayout) findViewById(R.id.rlUserEmail);
        rlUserEmail.setAlpha(0.3f);
        RelativeLayout rlUserRemark = (RelativeLayout) findViewById(R.id.rlUserRemark);
        rlUserRemark.setAlpha(0.3f);
    }
}
