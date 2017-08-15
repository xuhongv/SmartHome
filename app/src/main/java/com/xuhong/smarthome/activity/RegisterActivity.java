package com.xuhong.smarthome.activity;

import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.utils.ToastUtils;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout llCamera;

    private Button btn_register;

    //透明度
    private float BgAlpha = 0.5f;
    private float ButtonAlpha = 0.7f;


    private EditText register_et_name, register_et_email, register_et_password, register_et_password_again;

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

        register_et_name = (EditText) findViewById(R.id.register_et_name);
        register_et_email = (EditText) findViewById(R.id.register_et_email);
        register_et_password = (EditText) findViewById(R.id.register_et_password);
        register_et_password_again = (EditText) findViewById(R.id.register_et_password_again);


        RelativeLayout rlUserName = (RelativeLayout) findViewById(R.id.rlUserName);
        rlUserName.setAlpha(BgAlpha);
        RelativeLayout rlUserPassword = (RelativeLayout) findViewById(R.id.rlUserPassword);
        rlUserPassword.setAlpha(BgAlpha);
        RelativeLayout rlUserPasswordComfir = (RelativeLayout) findViewById(R.id.rlUserPasswordComfir);
        rlUserPasswordComfir.setAlpha(BgAlpha);
        RelativeLayout rlUserEmail = (RelativeLayout) findViewById(R.id.rlUserEmail);
        rlUserEmail.setAlpha(BgAlpha);
        ImageView ivCameraBg = (ImageView) findViewById(R.id.ivCameraBg);
        ivCameraBg.setAlpha(BgAlpha);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        btn_register.setAlpha(ButtonAlpha);

        llCamera = (FrameLayout) findViewById(R.id.llCamera);
        llCamera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.llCamera:
                break;

            case R.id.btn_register:
                if (!register_et_name.getText().toString().isEmpty()
                        && !register_et_email.getText().toString().isEmpty()
                        && !register_et_password.getText().toString().isEmpty()
                        && !register_et_password_again.getText().toString().isEmpty()) {

                    if (!register_et_password.getText().toString().equals(register_et_password_again.getText().toString())) {
//                        Snackbar.make(llCamera, , Snackbar.LENGTH_LONG)
//                                .setAction("知道了！", null)
//                                .show();

                        ToastUtils.showPhotoToast(RegisterActivity.this,R.drawable.ic_warning,"两次输入密码不一致哦！");
                    }



                } else {
                    Snackbar.make(llCamera, "请输入完整的信息！", Snackbar.LENGTH_LONG)
                            .setAction("知道了！", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .show();
                }


                break;
        }
    }
}
