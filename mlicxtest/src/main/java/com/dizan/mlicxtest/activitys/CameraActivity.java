package com.dizan.mlicxtest.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dizan.mlicxtest.R;
import com.github.faucamp.simplertmp.RtmpHandler;
import com.seu.magicfilter.utils.MagicFilterType;

import net.ossrs.yasea.SrsCameraView;
import net.ossrs.yasea.SrsEncodeHandler;
import net.ossrs.yasea.SrsPublisher;
import net.ossrs.yasea.SrsRecordHandler;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件名：MainActivity.class
 * 文件作者：Anwahh
 * 编写时间：2019-08-31
 */
public class CameraActivity extends AppCompatActivity implements View.OnClickListener, SrsEncodeHandler.SrsEncodeListener, SrsRecordHandler.SrsRecordListener, RtmpHandler.RtmpListener {

    private static final String TAG = "CameraActivity";

    private Button mPublishBtn;
    private Button mCameraSwitchBtn;
    private Button mEncoderBtn;
    private EditText mRempUrlEt;
    private SrsPublisher mPublisher;
    private String rtmpUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        checkPusherPermission();  //检查权限
        initView();  // 初始化相关的View
        initListener();  // 初始化相关的监听
        initData();  // 初始化相关的数据
    }

    /**
     * 权限请求结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 检查权限
     */
    private boolean checkPusherPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)) {
                permissions.add(Manifest.permission.INTERNET);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), 100);
                return false;
            }
        }
        return true;
    }

    /**
     * 初始化相关的View
     */
    private void initView() {
        mPublishBtn = findViewById(R.id.btn_publish);
        mCameraSwitchBtn = findViewById(R.id.btn_swCam);
        mEncoderBtn = findViewById(R.id.btn_swEnc);
        mRempUrlEt = findViewById(R.id.et_url);
    }

    /**
     * 初始化相关的监听
     */
    private void initListener() {
        mPublishBtn.setOnClickListener(this);
        mCameraSwitchBtn.setOnClickListener(this);
        mEncoderBtn.setOnClickListener(this);
    }

    /**
     * 初始化相关的数据
     */
    private void initData() {
        mPublisher = new SrsPublisher((SrsCameraView) findViewById(R.id.glsurfaceview_camera));
        //编码状态回调
        mPublisher.setEncodeHandler(new SrsEncodeHandler(this));
        mPublisher.setRecordHandler(new SrsRecordHandler(this));
        //rtmp推流状态回调
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        //预览分辨率
        mPublisher.setPreviewResolution(1280, 720);
        //推流分辨率
        mPublisher.setOutputResolution(720, 1280);
        //传输率
        mPublisher.setVideoHDMode();
        //开启美颜（其他滤镜效果在MagicFilterType中查看）
        mPublisher.switchCameraFilter(MagicFilterType.BEAUTY);
        //打开摄像头，开始预览（未推流）
        mPublisher.startCamera();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 开始/停止 推流
            case R.id.btn_publish:
                if (mPublishBtn.getText().toString().contentEquals("开始")) {
                    rtmpUrl = mRempUrlEt.getText().toString();
                    if (TextUtils.isEmpty(rtmpUrl)) {
                        Toast.makeText(getApplicationContext(), "地址不能为空！", Toast.LENGTH_SHORT).show();
                    }
                    mPublisher.startPublish(rtmpUrl);
                    mPublisher.startCamera();

                    if (mEncoderBtn.getText().toString().contentEquals("软编码")) {
                        Toast.makeText(getApplicationContext(), "当前使用硬解码", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "当前使用软解码", Toast.LENGTH_SHORT).show();
                    }
                    mPublishBtn.setText("停止");
                    mEncoderBtn.setEnabled(false);
                } else if (mPublishBtn.getText().toString().contentEquals("停止")) {
                    mPublisher.stopPublish();
                    mPublisher.stopRecord();
                    mPublishBtn.setText("开始");
                    mEncoderBtn.setEnabled(true);
                }
                break;
            //切换摄像头
            case R.id.btn_swCam:
                mPublisher.switchCameraFace((mPublisher.getCameraId() + 1) % Camera.getNumberOfCameras());
                break;
            //切换编码方式
            case R.id.btn_swEnc:
                if (mEncoderBtn.getText().toString().contentEquals("软编码")) {
                    mPublisher.switchToSoftEncoder();
                    mEncoderBtn.setText("硬编码");
                } else if (mEncoderBtn.getText().toString().contentEquals("硬编码")) {
                    mPublisher.switchToHardEncoder();
                    mEncoderBtn.setText("软编码");
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPublisher.resumeRecord();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPublisher.pauseRecord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPublisher.stopPublish();
        mPublisher.stopRecord();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPublisher.stopEncode();
        mPublisher.stopRecord();
        mPublisher.setScreenOrientation(newConfig.orientation);
        if (mPublishBtn.getText().toString().contentEquals("停止")) {
            mPublisher.startEncode();
        }
        mPublisher.startCamera();
    }

    @Override
    public void onNetworkWeak() {
        Toast.makeText(getApplicationContext(), "网络信号弱", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkResume() {

    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    private void handleException(Exception e) {
        try {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            mPublisher.stopPublish();
            mPublisher.stopRecord();
            mPublishBtn.setText("开始");
        } catch (Exception e1) {

        }
    }

    @Override
    public void onRecordPause() {
        Toast.makeText(getApplicationContext(), "Record paused", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordResume() {
        Toast.makeText(getApplicationContext(), "Record resume", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordStarted(String msg) {
        Toast.makeText(getApplicationContext(), "Recording file: " + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordFinished(String msg) {
        Toast.makeText(getApplicationContext(), "MP4 file saved: " + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    @Override
    public void onRecordIOException(IOException e) {
        handleException(e);
    }

    @Override
    public void onRtmpConnecting(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpConnected(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpVideoStreaming() {

    }

    @Override
    public void onRtmpAudioStreaming() {

    }

    @Override
    public void onRtmpStopped() {
        Toast.makeText(getApplicationContext(), "已停止", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpDisconnected() {
        Toast.makeText(getApplicationContext(), "未连接服务器", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpVideoFpsChanged(double fps) {

    }

    @Override
    public void onRtmpVideoBitrateChanged(double bitrate) {

    }

    @Override
    public void onRtmpAudioBitrateChanged(double bitrate) {

    }

    @Override
    public void onRtmpSocketException(SocketException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIOException(IOException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIllegalStateException(IllegalStateException e) {
        handleException(e);
    }
}
