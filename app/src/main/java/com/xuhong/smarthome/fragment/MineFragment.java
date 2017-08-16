package com.xuhong.smarthome.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.squareup.picasso.Picasso;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.activity.AlterUserInfActivity;
import com.xuhong.smarthome.activity.LoginActivity;
import com.xuhong.smarthome.bean.User;
import com.xuhong.smarthome.utils.L;
import com.xuhong.smarthome.utils.ToastUtils;
import com.xuhong.smarthome.view.BlurTransformation;
import com.xuhong.smarthome.view.CircleTransform;
import com.xuhong.smarthome.view.PullScrollView;
import com.xuhong.smarthome.view.SelectPopupWindow;

import cn.bmob.v3.BmobUser;


public class MineFragment extends BaseFragment implements View.OnClickListener {


    private ImageView ivHeaderBg;
    private ImageView ivmeIcon;
    private PullScrollView pullView;

    private TextView tvName;

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
        tvName = (TextView) view.findViewById(R.id.tvName);
        ivmeIcon.setOnClickListener(this);
        pullView = (PullScrollView) view.findViewById(R.id.pullView);
        pullView.setZoomView(ivHeaderBg);
        getUserInf();
    }


    @Override
    public void onResume() {
        super.onResume();
        getUserInf();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.ivIcon:
                //判断是否已经登录
                if (BmobUser.getCurrentUser(User.class) == null) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    SelectPopupWindow popupWindow = new SelectPopupWindow(getActivity(), new SelectPopupWindow.OnPopWindowClickListener() {
                        @Override
                        public void onPopWindowClickListener(View view) {
                            switch (view.getId()) {

                                //修改资料
                                case R.id.btnAlterInf:
                                    startActivity(new Intent(getActivity(), AlterUserInfActivity.class));
                                    break;

                                //退出登录
                                case R.id.btnExit:
                                    User.logOut();
                                    ToastUtils.showToast(getActivity(), "退出成功！");
                                    getUserInf();
                                    break;
                            }
                        }
                    }, SelectPopupWindow.TYPE_MINE_PHOTO);
                    popupWindow.show();
                }
                break;
        }
    }

    /**
     * 获取当前用户信息
     */
    private void getUserInf() {
        User userInfo = BmobUser.getCurrentUser(User.class);
        if (userInfo != null) {
            String nick = userInfo.getNick();
            Picasso.with(getActivity())
                    .load(nick)
                    .transform(new BlurTransformation(getActivity()))
                    .fit()
                    .into(ivHeaderBg);

            Picasso.with(getActivity())
                    .load(nick)
                    .transform(new CircleTransform())
                    .into(ivmeIcon);

            tvName.setText(userInfo.getUsername());

        } else {
            Picasso.with(getActivity())
                    .load(R.mipmap.ic_mine_header_bg)
                    .transform(new BlurTransformation(getActivity()))
                    .fit()
                    .into(ivHeaderBg);

            Picasso.with(getActivity())
                    .load(R.mipmap.ic_unget)
                    .transform(new CircleTransform())
                    .into(ivmeIcon);

            tvName.setText("欢迎使用智能管家");
        }
    }

    ;
}