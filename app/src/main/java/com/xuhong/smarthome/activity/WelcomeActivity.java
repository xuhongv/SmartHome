package com.xuhong.smarthome.activity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.romainpiel.shimmer.ShimmerViewHelper;
import com.xuhong.smarthome.MainActivity;
import com.xuhong.smarthome.R;

public class WelcomeActivity extends AppCompatActivity {


    private Shimmer shimmer ;

@SuppressLint("HandlerLeak")
private Handler mHandler =new Handler(){

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what==101){
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    }
};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        shimmer=new Shimmer();
        ShimmerTextView shimmer_login = (ShimmerTextView) findViewById(R.id.shimmer_login);
        shimmer.start(shimmer_login);
        mHandler.sendEmptyMessageDelayed(101,3000);




    }
}
