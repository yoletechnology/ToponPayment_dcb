package com.toponpaydcb.sdk.tool;

import android.util.Log;

import com.toponpaydcb.sdk.YoleSdkMgr;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NetworkRequest {
    public String TAG = "Yole_NetworkRequest";
    public void initAppBySdk(String mobile,String gaid,String userAgent,String countryCode,String mcc,String mnc,String cpCode,String versionName) {

        Log.d(TAG, "initAppBySdk cpCode:"+cpCode+";countryCode:"+countryCode);
        if(cpCode.length() <= 0 || countryCode.length() <= 0 )
        {
            YoleSdkMgr.getsInstance().initBasicSdkResult(false,"cpCode 或者 countryCode 无效");
            return;
        }

        JSONObject formBody = new JSONObject ();
        try {
            formBody.put("mobile",mobile);
            formBody.put("gaid",gaid);
            formBody.put("userAgent",userAgent);
            formBody.put("mcc",mcc);
            formBody.put("mnc",mnc);
            formBody.put("countryCode",countryCode);
            formBody.put("cpCode",cpCode);
            formBody.put("versionName",versionName);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "initAppBySdk-error:"+e.toString());
        }


        String res = NetUtil.sendPost("api/user/initAppBySdk",formBody);
        Log.d(TAG, "initAppBySdk"+res);
        YoleSdkMgr.getsInstance().user.decodeInitAppBySdk(res);
    }
    public void getPaymentKeyList(String MccWithMnc1,String MccWithMnc2) {

        Log.d(TAG, "getPaymentKeyList MccWithMnc1:"+MccWithMnc1+";MccWithMnc2:"+MccWithMnc2);

        String formBody = "";
        formBody += "mccWithMnc=";
        formBody += MccWithMnc1 ;

        if(MccWithMnc2.length() > 0)
            formBody +=  ","+MccWithMnc2;

        String res = NetUtil.sendGet("https://api.yolegames.com/api/operator/getPaymentKeyList",formBody);
        Log.d(TAG, "getPaymentStatus"+res);
        YoleSdkMgr.getsInstance().user.getPaymentStatus(res);
    }

    public void initDcbPayment(String amount,String orderNumber,String language,String cpCode) {

        JSONObject formBody = new JSONObject ();
        try {
            if(amount.length() > 0)
                formBody.put("amount",amount);
            if(orderNumber.length() > 0)
                formBody.put("orderNumber",orderNumber);
            if(language.length() > 0)
                formBody.put("language",language);
            if(cpCode.length() > 0)
                formBody.put("cpCode",cpCode);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "initDcbPayment-error:"+e.toString());
        }


        String res = NetUtil.sendPost("api/RUPayment/initDcbPayment",formBody);
        Log.d(TAG, "initDcbPayment"+res);
        YoleSdkMgr.getsInstance().user.decodeInitDcbPayment(res);
    }
    public void getDcbPaymentStatus(String orderNumber) {

        String formBody = "";
        formBody += "billingNumber="+orderNumber;

        String res = NetUtil.sendGet("https://api.yolesdk.com/api/RUPayment/getPaymentStatus",formBody);
        Log.d(TAG, "getPaymentStatus"+res);
        YoleSdkMgr.getsInstance().user.decodeDcbPaymentStatus(res);
    }

}
