package com.dizan.livetest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mPushBtn;
    private Button mPlayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView(); //初始化相关的View
        initListener(); //初始化相关的监听事件
    }

    /**
     * 初始化相关的View
     */
    private void initView() {
        mPushBtn = findViewById(R.id.push_stream_btn);
        mPlayBtn = findViewById(R.id.play_stream_btn);
    }

    private void initListener() {
        mPushBtn.setOnClickListener(this);
        mPlayBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.push_stream_btn :
                startActivity(new Intent(this, CameraActivity.class));
                break;
            case R.id.play_stream_btn:
                startActivity(new Intent(this, PlayerActivity.class));
                break;
        }
    }
}
