package com.dizan.appupdate.updater;

import com.dizan.appupdate.updater.net.INetManager;
import com.dizan.appupdate.updater.net.OkHttpNetManager;

/**
 * Created by Anwahh
 */
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
