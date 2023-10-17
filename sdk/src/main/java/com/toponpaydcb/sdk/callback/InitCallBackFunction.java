package com.toponpaydcb.sdk.callback;

import com.toponpaydcb.sdk.data.InitSdkData;

public interface InitCallBackFunction {

    public void success(InitSdkData info);
    public void fail(String info);
}
