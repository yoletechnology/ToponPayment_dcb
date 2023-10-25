package com.toponpaydcb.sdk.tool;

import android.util.Log;

import com.toponpaydcb.sdk.YoleSdkMgr;

import org.json.JSONObject;

import java.util.HashMap;

public class NetworkRequest {
    public String TAG = "Yole_NetworkRequest";
    public void initAppBySdk(String mobile,String gaid,String userAgent,String imei,String mac,String countryCode,String mcc,String mnc,String cpCode,String versionName) throws Exception {

        Log.d(TAG, "initAppBySdk cpCode:"+cpCode+";countryCode:"+countryCode);
        if(cpCode.length() <= 0 || countryCode.length() <= 0 )
        {
            YoleSdkMgr.getsInstance().initBasicSdkResult(false,"cpCode 或者 countryCode 无效");
            return;
        }

        JSONObject formBody = new JSONObject ();
        if(mobile.length() > 0)
            formBody.put("mobile",mobile);
        if(gaid.length() > 0)
            formBody.put("gaid",gaid);
        if(userAgent.length() > 0)
            formBody.put("userAgent",userAgent);
        if(imei.length() > 0)
            formBody.put("imei",imei);
        if(mac.length() > 0)
            formBody.put("mac",mac);
        if(mcc.length() > 0)
            formBody.put("mcc",mcc);
        if(mnc.length() > 0)
            formBody.put("mnc",mnc);

        formBody.put("countryCode",countryCode);
        formBody.put("cpCode",cpCode);
        formBody.put("versionName",versionName);

        String res = NetUtil.sendPost("api/user/initAppBySdk",formBody);
        Log.d(TAG, "initAppBySdk"+res);
        YoleSdkMgr.getsInstance().user.decodeInitAppBySdk(res);
    }
    public void getPaymentStatus(String mcc,String mnc) throws Exception {

        Log.d(TAG, "getPaymentStatus mcc:"+mcc+";mnc:"+mnc);

        String formBody = "";
        formBody += "mcc="+mcc;
        formBody += "&mnc="+mnc;

        String res = NetUtil.sendGet("https://api.yolegames.com",formBody);
        Log.d(TAG, "getPaymentStatus"+res);
        YoleSdkMgr.getsInstance().user.getPaymentStatus(res);
    }

    public void initDcbPayment(String amount,String orderNumber,String language,String mcc,String mnc,String cpCode) throws Exception {

        JSONObject formBody = new JSONObject ();
        if(amount.length() > 0)
            formBody.put("amount",amount);
        if(orderNumber.length() > 0)
            formBody.put("orderNumber",orderNumber);
        if(language.length() > 0)
            formBody.put("language",language);
        if(mcc.length() > 0)
            formBody.put("mcc",mcc);
        if(mnc.length() > 0)
            formBody.put("mnc",mnc);
        if(cpCode.length() > 0)
            formBody.put("cpCode",cpCode);

        String res = NetUtil.sendPost("api/RUPayment/initDcbPayment",formBody);
        Log.d(TAG, "initDcbPayment"+res);
        YoleSdkMgr.getsInstance().user.decodeInitDcbPayment(res);
    }
    public void getDcbPaymentStatus(String orderNumber) throws Exception {

        String formBody = "";
        formBody += "billingNumber="+orderNumber;

        String res = NetUtil.sendGet("https://api.yolesdk.com/api/RUPayment/getPaymentStatus",formBody);
        Log.d(TAG, "getPaymentStatus"+res);
        YoleSdkMgr.getsInstance().user.decodeDcbPaymentStatus(res);
    }

}
