package com.dizan.appupdate.updater.net;

import java.io.File;

public interface INetManager {
    void get(String url, INetCallBack callBack, Object tag);

    void download(String url, File targetFile, INetDownloadCallBack callback, Object tag);

    void cancel(Object tag);
}
