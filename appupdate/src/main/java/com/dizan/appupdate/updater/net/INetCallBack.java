package com.dizan.appupdate.updater.net;

public interface INetCallBack {
    void success(String response);

    void failed(Throwable throwable);
}
