package com.dizan.mlicxtest.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dizan.mlicxtest.R;
import com.dizan.mlicxtest.updater.AppUpdater;
import com.dizan.mlicxtest.updater.bean.DownloadBean;
import com.dizan.mlicxtest.updater.net.INetCallBack;
import com.dizan.mlicxtest.updater.ui.UpdateVersionShowDialog;
import com.dizan.mlicxtest.updater.utils.AppUtils;

/**
 * 文件名：MainActivity.class
 * 文件作者：Anwahh
 * 编写时间：2019-08-31
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_carmerapusher;
    private TextView tv_screenpusher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //checkUpdate();  // 检查版本更新升级
        initView();  // 初始化相关的View
        initListener();  // 初始化相关的监听事件
    }

    /**
     * 检查版本更新升级
     */
    private void checkUpdate() {
        AppUpdater.getInstance().getNetManager().get("http://59.110.162.30/app_updater_version.json", new INetCallBack() {
            @Override
            public void success(String response) {
                Log.d("Anwahh", "response = " + response);
                // 1. 解析json
                DownloadBean bean = DownloadBean.parse(response);
                if(bean == null) {
                    Toast.makeText(MainActivity.this, "版本检查接口返回数据异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 2. 做版本匹配，检查：是否需要弹窗
                try {
                    long versionCode = Long.parseLong(bean.versionCode);
                    if(versionCode <= AppUtils.getVersionCode(MainActivity.this)) {
                        Toast.makeText(MainActivity.this, "已经是最新版本，无需更新", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "版本检查接口返回版本号异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 3. 弹窗
                UpdateVersionShowDialog.show(MainActivity.this, bean);
                // 4. 点击下载


            }

            @Override
            public void failed(Throwable throwable) {
                throwable.printStackTrace();
                Toast.makeText(MainActivity.this, "版本更新接口请求失败", Toast.LENGTH_SHORT).show();
            }
        }, MainActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUpdater.getInstance().getNetManager().cancel(MainActivity.this);
    }

    /**
     * 初始化相关的View
     */
    private void initView() {
        tv_carmerapusher = findViewById(R.id.tv_carmerapusher);
        tv_screenpusher = findViewById(R.id.tv_screenpusher);
    }

    /**
     * 初始化相关的监听事件
     */
    private void initListener() {
        tv_carmerapusher.setOnClickListener(this);
        tv_screenpusher.setOnClickListener(this);
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_carmerapusher:
                startActivity(new Intent(this, CameraActivity.class));
                break;
            case R.id.tv_screenpusher:
                startActivity(new Intent(this, ScreenActivity.class));
                break;
        }
    }

}
