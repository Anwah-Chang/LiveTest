package com.dizan.appupdate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dizan.appupdate.updater.AppUpdater;
import com.dizan.appupdate.updater.bean.DownloadBean;
import com.dizan.appupdate.updater.net.INetCallBack;
import com.dizan.appupdate.updater.net.INetDownloadCallBack;
import com.dizan.appupdate.updater.ui.UpdateVersionShowDialog;
import com.dizan.appupdate.updater.utils.AppUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button mBtnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnUpdate = findViewById(R.id.btn_update);
        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUpdater.getInstance().getNetManager().cancel(MainActivity.this);
    }
}
