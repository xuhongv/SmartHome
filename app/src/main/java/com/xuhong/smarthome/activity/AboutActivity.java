package com.xuhong.smarthome.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.lqr.optionitemview.OptionItemView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.constant.Constant;
import com.xuhong.smarthome.utils.L;
import com.xuhong.smarthome.utils.OkHttpUtils;
import com.xuhong.smarthome.view.BubbleView;
import com.xuhong.smarthome.view.PullScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class AboutActivity extends BaseActivity implements View.OnClickListener {


    private BubbleView mBubbleView;
    private OptionItemView OIViewUpdata;
    private com.lqr.optionitemview.OptionItemView mOIViewState;
    private com.lqr.optionitemview.OptionItemView mOIViewOpinion;
    private com.lqr.optionitemview.OptionItemView mOIViewAbout;
    private com.lqr.optionitemview.OptionItemView mOIViewShare;
    private PullScrollView mPullScrollView;
    private QMUITipDialog tipDialog;

    private String lasterVersion = "1.00.00";
    private String lasterVersionInf = "修复了Bug。";

    private static final int CODE_HANLDER_GET_FAIL = 201;
    private static final int CODE_HANLDER_GET_SUCCEED = 202;
    private static final int CODE_HANLDER_DISS = 203;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //L.e("mBubbleView getWidth :" + mBubbleView.getWidth() + ",mBubbleView.getHeight():" + mBubbleView.getHeight());

            if (msg.what == 85) {
                mHandler.sendEmptyMessageDelayed(85, 500);
                mBubbleView.startAnimation(mBubbleView.getWidth(), mBubbleView.getHeight());
            }


            //关闭提示
            if (msg.what == CODE_HANLDER_DISS) {
                if (tipDialog != null) {
                    tipDialog.dismiss();
                }
            }


            //获取版本失败
            if (msg.what == CODE_HANLDER_GET_FAIL) {
                tipDialog = new QMUITipDialog.Builder(AboutActivity.this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord("获取最新版本失败，请重试！")
                        .create();
                tipDialog.show();
                mHandler.sendEmptyMessageDelayed(CODE_HANLDER_DISS, 1500);
            }


            if (msg.what == CODE_HANLDER_GET_SUCCEED) {
                if (isLasterVersion()) {
                    tipDialog = new QMUITipDialog.Builder(AboutActivity.this)
                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                            .setTipWord("已是最新版本")
                            .create();
                    tipDialog.show();
                    mHandler.sendEmptyMessageDelayed(CODE_HANLDER_DISS, 1500);
                } else {

                    String title = "是否更新到最新版本？";
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
                    if (networkInfo != null) {
                        if (networkInfo.isConnected()) {
                            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                                title = "当前wifi网络，是否更新？";
                            }

                            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                                title = "当前非wifi网络，是否更新？";
                            }
                        } else {
                            return;
                        }
                    }

                    new QMUIDialog.MessageDialogBuilder(AboutActivity.this)
                            .setTitle(title)
                            .setMessage(lasterVersionInf)
                            .addAction("取消", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            })
                            .addAction("确定", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    Toast.makeText(AboutActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
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


        OIViewUpdata = (OptionItemView) findViewById(R.id.OIViewUpdata);
        mOIViewState = (com.lqr.optionitemview.OptionItemView) findViewById(R.id.OIViewState);
        mOIViewOpinion = (com.lqr.optionitemview.OptionItemView) findViewById(R.id.OIViewOpinion);
        mOIViewAbout = (com.lqr.optionitemview.OptionItemView) findViewById(R.id.OIViewAbout);
        mOIViewShare = (com.lqr.optionitemview.OptionItemView) findViewById(R.id.OIViewShare);

        OIViewUpdata.setOnClickListener(this);
        mOIViewState.setOnClickListener(this);
        mOIViewOpinion.setOnClickListener(this);
        mOIViewAbout.setOnClickListener(this);
        mOIViewShare.setOnClickListener(this);


        List<Drawable> drawableList = new ArrayList<>();
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_indigo_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_deep_purple_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_cyan_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_blue_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_deep_purple_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_light_blue_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_lime_a200_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_pink_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_red_900_24dp));
        mBubbleView = (BubbleView) findViewById(R.id.mBubbleView);
        mBubbleView.setDrawableList(drawableList);
        mHandler.sendEmptyMessageDelayed(85, 1000);
        mPullScrollView = (PullScrollView) findViewById(R.id.mPullScrollView);
        mPullScrollView.setZoomView(findViewById(R.id.rll));

        String getcurrentAppName = getcurrentVersion();
        OIViewUpdata.setRightText("当前版本：" + getcurrentAppName);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.OIViewUpdata:
                getCurrentVersion();
                break;
            //声明
            case R.id.OIViewState:
                startActivity(new Intent(AboutActivity.this, StatementActivity.class));
                break;
            //关于作者
            case R.id.OIViewAbout:
                startActivity(new Intent(AboutActivity.this, AboutAuthorActivity.class));
                break;
            //意见反馈
            case R.id.OIViewOpinion:
                tipDialog = new QMUITipDialog.Builder(AboutActivity.this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                        .setTipWord("请到GitHub项目Issues提出问题吧！")
                        .create();
                tipDialog.show();
                mHandler.sendEmptyMessageDelayed(CODE_HANLDER_DISS, 2000);

                break;
        }
    }


    //获取最新版本号
    private void getCurrentVersion() {

        String title = "努力检测最新版本...";
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        if (networkInfo != null) {
            if (!networkInfo.isConnected()) {
                 title = "网络不可用！检测失败...";
                tipDialog = new QMUITipDialog.Builder(AboutActivity.this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                        .setTipWord(title)
                        .create();
                tipDialog.show();
                mHandler.sendEmptyMessageDelayed(CODE_HANLDER_DISS, 1500);
                return;
            }
        } else {
            title = "网络未连接！检测失败...";
            tipDialog = new QMUITipDialog.Builder(AboutActivity.this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                    .setTipWord(title)
                    .create();
            tipDialog.show();
            mHandler.sendEmptyMessageDelayed(CODE_HANLDER_DISS, 1500);
            return;
        }

        questNetVersion();

        tipDialog = new QMUITipDialog.Builder(AboutActivity.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(title)
                .create();
        tipDialog.show();


    }


    private void questNetVersion() {

        OkHttpUtils.getInstance().sendCommon(Constant.GET_APP_VERSION, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessageDelayed(CODE_HANLDER_GET_FAIL, 1000);
                tipDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String string = new String(response.body().bytes(), "GBK");
                        JSONObject jsonObject = new JSONObject(string);
                        lasterVersion = jsonObject.getString("appVersion");
                        lasterVersionInf = jsonObject.getString("appVersionInf");
                        mHandler.sendEmptyMessageDelayed(CODE_HANLDER_GET_SUCCEED, 1000);
                        tipDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mHandler.sendEmptyMessageDelayed(CODE_HANLDER_GET_FAIL, 1000);
                        tipDialog.dismiss();
                    }
                } else {
                    mHandler.sendEmptyMessageDelayed(CODE_HANLDER_GET_FAIL, 1000);
                    tipDialog.dismiss();
                }
            }
        });
    }


    //获取当前app版本
    private String getcurrentVersion() {
        try {
            return this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0).versionName;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否为最新版本
     *
     * @return ture最新版本，false不是
     */
    private boolean isLasterVersion() {
        long lastetVersionCode = versionInf(lasterVersion);
        long currentVersionCode = versionInf(getcurrentVersion());
        L.i("最新版本：" + lastetVersionCode);
        L.i("当前版本：" + currentVersionCode);
        return (currentVersionCode - lastetVersionCode) >= 0;
    }

    private long versionInf(String versionInf) {
        String[] verNums = versionInf.split("\\.");
        int mVersionNum1 = Integer.parseInt(verNums[0]);
        int mVersionNum2 = Integer.parseInt(verNums[1]);
        int mVersionNum3 = Integer.parseInt(verNums[2]);
        return mVersionNum1 * 1000 * 1000 + mVersionNum2 * 1000 + mVersionNum3;
    }


}
