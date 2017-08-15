package com.xuhong.smarthome.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.xuhong.smarthome.R;

public class ForgetPasswordActivity extends BaseActivity {


    private Button btnSend;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
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

        RelativeLayout rlUserEmail= (RelativeLayout) findViewById(R.id.rlUserEmail);
        rlUserEmail.setAlpha(0.3f);

        btnSend= (Button) findViewById(R.id.btnSend);
        btnSend.setAlpha(0.6f);
    }
}
