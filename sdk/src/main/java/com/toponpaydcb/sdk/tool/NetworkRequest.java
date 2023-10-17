package com.toponpaydcb.sdk.tool;

import android.util.Log;

import com.toponpaydcb.sdk.YoleSdkMgr;

import org.json.JSONObject;

public class NetworkRequest {
    public String TAG = "Yole_NetworkRequest";
    public void initAppBySdk(String mobile,String gaid,String userAgent,String imei,String mac,String countryCode,String mcc,String mnc,String cpCode) throws Exception {

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

        String res = NetUtil.sendPost("api/user/initAppBySdk",formBody);
        Log.d(TAG, "initAppBySdk"+res);
        YoleSdkMgr.getsInstance().user.decodeInitAppBySdk(res);
    }
    public void initDcbPayment(String amount,String orderNumber,String countryCode,String language,String currency,String mcc,String mnc,String cpCode,String from) throws Exception {

        JSONObject formBody = new JSONObject ();
        if(amount.length() > 0)
            formBody.put("amount",amount);
        if(orderNumber.length() > 0)
            formBody.put("orderNumber",orderNumber);
        if(countryCode.length() > 0)
            formBody.put("countryCode",countryCode);
        if(language.length() > 0)
            formBody.put("language",language);
        if(currency.length() > 0)
            formBody.put("currency",currency);
        if(mcc.length() > 0)
            formBody.put("mcc",mcc);
        if(mnc.length() > 0)
            formBody.put("mnc",mnc);
        if(cpCode.length() > 0)
            formBody.put("cpCode",cpCode);
        if(from.length() > 0)
            formBody.put("from",from);

        String res = NetUtil.sendPost("api/RUPayment/initDcbPayment",formBody);
        Log.d(TAG, "initDcbPayment"+res);
        YoleSdkMgr.getsInstance().user.decodeInitDcbPayment(res);
    }
    public void getDcbPaymentStatus(String orderNumber) throws Exception {

        JSONObject formBody = new JSONObject ();

        if(orderNumber.length() > 0)
            formBody.put("orderNumber",orderNumber);


        String res = NetUtil.sendPost("api/RUPayment/getDcbPaymentStatus",formBody);
        Log.d(TAG, "getDcbPaymentStatus"+res);
        YoleSdkMgr.getsInstance().user.decodeDcbPaymentStatus(res);
    }
    /**获取支付策略*/
    /**
     *
     * @param countryCode   国家码         CH
     */
    public void getPaymentSms(String countryCode) throws Exception {
        JSONObject formBody = new JSONObject ();
        if(countryCode.length() > 0)
            formBody.put("countryCode",countryCode);

        String res = NetUtil.sendPost("api/payment/getPaymentSms",formBody);
        Log.d(TAG, "getPaymentSms"+res);
        YoleSdkMgr.getsInstance().user.getPaymentSms(res);
    }

    public void smsPaymentNotify(String payOrderNum,String paymentStatus) throws Exception {
        JSONObject formBody = new JSONObject ();
        formBody.put("payOrderNum",payOrderNum);
        formBody.put("paymentStatus",paymentStatus);

        String res = NetUtil.sendPost("api/payment/smsPaymentNotify",formBody);
        Log.d(TAG, "smsPaymentNotify"+res);
        YoleSdkMgr.getsInstance().user.smsPaymentNotify(res);
    }
}
