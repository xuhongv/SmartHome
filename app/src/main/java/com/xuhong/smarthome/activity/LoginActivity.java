package com.xuhong.smarthome.activity;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;

import com.xuhong.smarthome.R;
import com.xuhong.smarthome.bean.User;
import com.xuhong.smarthome.utils.ToastUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends BaseActivity implements View.OnClickListener {


    //弹窗
    private LinearLayout allLogin;
    //输入手机号码或邮箱时候出现的
    private ImageView ivname_goneAll;

    //密码输入框
    private EditText login_et_password;

    //显示密码？
    private CheckBox cb_eye_gone;
    private TextInputLayout textInLayName;
    private TextInputLayout textInLayPass;
    private EditText login_et_name;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        //弹窗
        allLogin = (LinearLayout) findViewById(R.id.allLogin);

        //免费注册
        TextView login_registered = (TextView) findViewById(R.id.login_registered);
        login_registered.setOnClickListener(this);

        //忘记密码
        TextView login_forget = (TextView) findViewById(R.id.login_forget);
        login_forget.setOnClickListener(this);

        ivname_goneAll = (ImageView) findViewById(R.id.ivname_goneAll);
        ivname_goneAll.setOnClickListener(this);

        textInLayName = (TextInputLayout) findViewById(R.id.textInLayName);
        textInLayPass = (TextInputLayout) findViewById(R.id.textInLayPass);

        RelativeLayout rlUserName = (RelativeLayout) findViewById(R.id.rlUserName);
        RelativeLayout rlUserPassword = (RelativeLayout) findViewById(R.id.rlUserPassword);
        //登录按钮
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        btn_login.setAlpha(0.7f);
        rlUserName.setAlpha(0.5f);
        rlUserPassword.setAlpha(0.5f);

        login_et_name = (EditText) findViewById(R.id.login_et_name);
        login_et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textInLayName.setErrorEnabled(true);
                } else {
                    textInLayName.setErrorEnabled(false);
                }
            }
        });

        login_et_password = (EditText) findViewById(R.id.login_et_password);
        login_et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textInLayPass.setErrorEnabled(true);
                } else {
                    textInLayPass.setErrorEnabled(false);
                }
            }
        });


        login_et_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    ivname_goneAll.setVisibility(View.VISIBLE);
                } else {
                    ivname_goneAll.setVisibility(View.INVISIBLE);
                }
            }
        });

        login_et_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    cb_eye_gone.setVisibility(View.VISIBLE);
                } else {
                    cb_eye_gone.setVisibility(View.INVISIBLE);
                }
            }
        });


        //显示密码
        cb_eye_gone = (CheckBox) findViewById(R.id.cb_eye_gone);
        cb_eye_gone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    login_et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    login_et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_registered:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

            case R.id.login_forget:
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;

            case R.id.ivname_goneAll:
                //清空
                login_et_name.setText("");
                break;

            case R.id.btn_login:


                if (!login_et_name.getText().toString().isEmpty() && !login_et_password.getText().toString().isEmpty()) {

                    final User user = new User();
                    user.setUsername(login_et_name.getText().toString());
                    user.setPassword(login_et_password.getText().toString());
                    user.login(new SaveListener<BmobUser>() {
                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {
                            if (e == null) {
                                finish();
                            } else {
                                Log.e("==w", "登录失败：" + e);
                                switch (e.getErrorCode()) {
                                    case 101:
                                        ToastUtils.showToast(LoginActivity.this, "用户名或密码不正确哦！");
                                        break;
                                    default:
                                        ToastUtils.showToast(LoginActivity.this, "出了点小问题，请重试！错误码：" + e.getErrorCode());
                                        break;
                                }
                            }
                        }
                    });


                } else {

                    String flagEmpty = "";
                    if (login_et_name.getText().toString().isEmpty() && login_et_password.getText().toString().isEmpty()) {
                        flagEmpty = "请输入账号和密码！";
                    } else {
                        if (login_et_name.getText().toString().isEmpty()) {
                            flagEmpty = "请输入账号！";
                        }
                        if (login_et_password.getText().toString().isEmpty()) {
                            flagEmpty = "请输入密码！";
                        }
                    }

                    Snackbar.make(btn_login, flagEmpty, Snackbar.LENGTH_LONG)
                            .setAction("知道了！", null)
                            .setActionTextColor(getResources().getColor(R.color.yellow0))

                            .show();

                }
                break;
        }
    }

    private void showForgetPw() {

        View view = getLayoutInflater().inflate(R.layout.dialog_forget_password, null);

        final AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                .setView(view)
                .show();

        final EditText forgetps_et = (EditText) view.findViewById(R.id.forgetps_et);
        Button tv_cancel = (Button) view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        Button tv_sure = (Button) view.findViewById(R.id.tv_sure);
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(LoginActivity.this, "ddd", Toast.LENGTH_LONG).show();
                String string = forgetps_et.getText().toString();

                Log.e("==w", "xiaojianpan:" + string);
                dialog.dismiss();
            }
        });


    }
}

