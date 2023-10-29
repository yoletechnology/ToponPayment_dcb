package com.toponpaydcb.sdk.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.toponpaydcb.sdk.data.init.YoleInitConfig;
import com.toponpaydcb.sdk.tool.FileSave;
import com.toponpaydcb.sdk.tool.PhoneInfo;
import com.toponpaydcb.sdk.YoleSdkMgr;
import com.toponpaydcb.sdk.callback.CallBackFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserInfo extends UserInfoBase{
    private static String TAG = "Yole_UserInfo";

    public UserInfo(Context var1, YoleInitConfig _config)
    {
        act = var1;
        config = _config;
        info = new PhoneInfo(act);
        String[] data = FileSave.readContent("PhoneNumber.text",var1);
        phoneNumber = data.length > 0 ? data[0] : "";
        Log.d(TAG, "NetUtil init:appkey="+config.getAppKey()+"cpCode="+config.getCpCode());
    }
    public YoleInitConfig getConfig(){return super.config;}
    public  String getSmsNumber(){return super.getSmsNumber();}
    public  String getSmsCode(){return super.getSmsCode();}
    public  String getCpCode(){return super.getCpCode();}
    public  String getAppkey(){return super.getAppkey();}
    public  String getMcc(){return super.getMcc();}
    public  String getMnc(){return super.getMnc();}
    public  String getCountryCode(){return super.getCountryCode();}
    public  String getImei()
    {
        return super.getImei();
    }
    public  String getMac()
    {
        return super.getMac();
    }
    public  String getPackageName()
    {
        return super.getPackageName();
    }
    public  String getAppName()
    {
        return super.getAppName();
    }
    public  Drawable getIcon()
    {
        return super.getIcon();
    }
    public  String getVersionName()
    {
        return super.getVersionName();
    }
    public  String getPhoneModel()
    {
        return super.getPhoneModel();
    }
    public  String getGaid()
    {
        return super.getGaid();
    }
    public  String getPhoneNumber(){return super.getPhoneNumber();}
    public  String getPayOrderNum()
    {
        return super.getPayOrderNum();
    }
    public  String getAmount()
    {
        return super.getAmount();
    }
    public  String getLanguage()
    {
        return super.getLanguage();
    }
    public  CallBackFunction getPayCallBack()
    {
        return super.getPayCallBack();
    }

    public  void decodeDcbPaymentStatus(String res)
    {
        if(res.length() <= 0)
        {
            Log.e(TAG, "InitDcbPayment:"+res);
            if(backFunc != null)
                backFunc.onCallBack(false,"","");
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(res);
            String status = jsonObject.getString("status");
            String errorCode = jsonObject.getString("errorCode");
            String message = jsonObject.getString("message");

            if(status.indexOf("SUCCESS") ==  -1)
            {
                backFunc.onCallBack(false,"errorCode:"+errorCode+";message:"+message,"");
            }
            else
            {
                String content = jsonObject.getString("content");
                JSONObject contentJsonObject = new JSONObject(content);
                String paymentStatus = contentJsonObject.getString("paymentStatus");
                String paymentDatetime = contentJsonObject.getString("paymentDatetime");
                boolean result = (paymentStatus.indexOf("SUCCESSFUL") != -1 && paymentStatus.length() == "SUCCESSFUL".length());
                backFunc.onCallBack(result,contentJsonObject.toString(),"");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            backFunc.onCallBack(false,e.toString(),"");
        }
    }
    public  void decodeInitDcbPayment(String res)
    {
        if(res.length() <= 0)
        {
            Log.e(TAG, "InitDcbPayment:"+res);
            if(backFunc != null)
                backFunc.onCallBack(false,"","");
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(res);
            String status = jsonObject.getString("status");
            String errorCode = jsonObject.getString("errorCode");
            String message = jsonObject.getString("message");

            if(status.indexOf("SUCCESS") ==  -1)
            {
                backFunc.onCallBack(false,"errorCode:"+errorCode+";message:"+message,"");
            }
            else
            {
                String content = jsonObject.getString("content");
                JSONObject contentJsonObject = new JSONObject(content);
                String webUrl = contentJsonObject.getString("webUrl");

                YoleSdkMgr.getsInstance().startDcbActivity(webUrl);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            backFunc.onCallBack(false,e.toString(),"");
        }
    }
    public  void decodePaymentResults(String res)
    {
        if(res.length() <= 0)
        {
            Log.e(TAG, "decodePaymentResults:"+res);
            if(backFunc != null)
                backFunc.onCallBack(false,"","");
            return;
        }
        if(backFunc != null)
        {
            try {
                JSONObject jsonObject = new JSONObject(res);
                String status = jsonObject.getString("status");
                String errorCode = jsonObject.getString("errorCode");
                String message = jsonObject.getString("message");

                if(status.indexOf("SUCCESS") ==  -1)
                {
                    backFunc.onCallBack(false,"errorCode:"+errorCode+";message:"+message,"");
                }
                else
                {
                    String content = jsonObject.getString("content");
                    JSONObject contentJsonObject = new JSONObject(content);
                    String content1 = contentJsonObject.getString("content");
                    JSONObject content1JsonObject = new JSONObject(content1);
                    String billingNumber = content1JsonObject.getString("billingNumber");

                    backFunc.onCallBack(true,status,billingNumber);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                backFunc.onCallBack(false,e.toString(),"");
            }
        }
    }

    public  void decodeInitAppBySdk(String res)
    {
        if(res.length() <= 0)
        {
            Log.e(TAG, "decodeInitAppBySdk:"+res);
            YoleSdkMgr.getsInstance().initBasicSdkResult(false,"");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(res);
            String status = jsonObject.getString("status");
            String errorCode = jsonObject.getString("errorCode");
            String message = jsonObject.getString("message");

            if(status.indexOf("SUCCESS") ==  -1)
            {
                Log.d(TAG, "decodeInitAppBySdk error:"+status);
                YoleSdkMgr.getsInstance().initBasicSdkResult(false,"errorCode:"+errorCode+";message="+message);
            }
            else
            {

                String content = jsonObject.getString("content");
                JSONObject contentJsonObject = new JSONObject(content);

                String userCode = contentJsonObject.getString("userCode");
                String productName = contentJsonObject.getString("productName");
                String productIcon = contentJsonObject.getString("productIcon");
                String companyName = contentJsonObject.getString("companyName");
                String currencySymbol = contentJsonObject.getString("currencySymbol");
                boolean adsOpen = contentJsonObject.optBoolean("adsOpen");
                String areaCode = contentJsonObject.getString("areaCode");
//                int currencyDecimal = contentJsonObject.getInt("currencyDecimal");
                JSONArray smsFeeList = contentJsonObject.getJSONArray("smsFeeList");
                List<String> list = new ArrayList<>();
                for(int i=0;i<smsFeeList.length();i++)
                {
                    list.add(smsFeeList.get(i).toString());
                }
                initSdkData.userCode = userCode;
                initSdkData.productName = productName;
                initSdkData.productIcon = productIcon;
                initSdkData.companyName = companyName;
                initSdkData.currencySymbol = currencySymbol;
                initSdkData.adsOpen = adsOpen;
                initSdkData.areaCode = areaCode;
//                initSdkData.currencyDecimal = currencyDecimal;
//                if(paymentKeyList.length() <= 0)
//                {
//                    initSdkData.payType = InitSdkData.PayType.UNAVAILABLE;
//                }
//                else if(list.indexOf("OP_DCB") != -1)
//                {
//                    initSdkData.payType = InitSdkData.PayType.OP_DCB;
//                }
//                else if(list.indexOf("OP_SMS") != -1)
//                {
//                    initSdkData.payType = InitSdkData.PayType.OP_SMS;
//                }
//                else
//                {
//                    initSdkData.payType = InitSdkData.PayType.UNAVAILABLE;
//                }
                String stt = "；userCode:"+userCode;
                stt += "；productName:"+productName;
                stt += "；companyName:"+companyName;
                stt += "；currencySymbol:"+currencySymbol;
                stt += "；adsOpen:"+adsOpen;
                stt += "；smsFeeList:"+smsFeeList;
                Log.e(TAG, "decodeInitAppBySdk stt:"+stt);
                YoleSdkMgr.getsInstance().initBasicSdkResult(true,"errorCode:"+errorCode+";message="+message);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            YoleSdkMgr.getsInstance().initBasicSdkResult(false,e.toString());
        }
    }
    public  void getPaymentStatus(String res)
    {
        initSdkData.payType.clear();
        if(res.length() <= 0)
        {
            Log.e(TAG, "getPaymentStatus:"+res);
            YoleSdkMgr.getsInstance().paymentStatusCallBack.onCallBack(false,initSdkData.payType);
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(res);
            String status = jsonObject.getString("status");
            String errorCode = jsonObject.getString("errorCode");
            String message = jsonObject.getString("message");

            if(status.indexOf("SUCCESS") ==  -1)
            {
                Log.d(TAG, "getPaymentStatus error:"+status);
                YoleSdkMgr.getsInstance().paymentStatusCallBack.onCallBack(false,initSdkData.payType);
            }
            else
            {

                String content = jsonObject.getString("content");
                JSONObject contentJsonObject = new JSONObject(content);

                JSONArray paymentKeyList = contentJsonObject.getJSONArray("paymentKeyList");
                List<String> list = new ArrayList<>();
                for(int i=0;i<paymentKeyList.length();i++)
                {
                    list.add(paymentKeyList.get(i).toString());
                }

                if(paymentKeyList.length() <= 0)
                {
                    initSdkData.payType.add(InitSdkData.PayType.UNAVAILABLE) ;
                }
                else
                {
                    if(list.indexOf("OP_DCB_BEELINE") != -1)
                    {
                        initSdkData.payType.add(InitSdkData.PayType.OP_DCB_BEELINE) ;
                    }
                    if(list.indexOf("OP_DCB") != -1)
                    {
                        initSdkData.payType.add(InitSdkData.PayType.OP_DCB) ;
                    }
                    if(list.indexOf("OP_SMS") != -1)
                    {
                        initSdkData.payType.add(InitSdkData.PayType.OP_SMS) ;
                    }
                }
                Log.e(TAG, "getPaymentStatus stt:"+initSdkData.payType);
                YoleSdkMgr.getsInstance().paymentStatusCallBack.onCallBack(true,initSdkData.payType);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            YoleSdkMgr.getsInstance().paymentStatusCallBack.onCallBack(false,initSdkData.payType);
        }
    }



}
