package com.toponpaydcb.sdk.callback;

import com.toponpaydcb.sdk.data.InitSdkData;

import java.util.List;

public interface PaymentStatusCallBack {
    public void onCallBack(boolean result,String info, List<InitSdkData.PayType> payType,boolean adsOpen);
}

