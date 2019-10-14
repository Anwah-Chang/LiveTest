package com.dizan.mlicxtest.updater;

import com.dizan.mlicxtest.updater.net.INetManager;
import com.dizan.mlicxtest.updater.net.OkHttpNetManager;

public class AppUpdater {
    private static AppUpdater sInstance = new AppUpdater();

    private INetManager mNetManager = new OkHttpNetManager();

    public INetManager getNetManager() {
        return mNetManager;
    }

    public static AppUpdater getInstance() {
        return sInstance;
    }
}
