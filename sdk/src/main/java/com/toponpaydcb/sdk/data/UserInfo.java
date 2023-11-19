package com.toponpaydcb.sdk.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.toponpaydcb.sdk.data.init.YoleInitConfig;
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
        Log.e(TAG, "InitDcbPayment:"+res);
        if(res.length() <= 0)
        {
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
                backFunc.onCallBack(false,res,"");
            }
            else
            {
                String content = jsonObject.getString("content");
                JSONObject contentJsonObject = new JSONObject(content);
                String paymentStatus = contentJsonObject.getString("paymentStatus");
                String paymentDatetime = contentJsonObject.getString("paymentDatetime");
                boolean result = (paymentStatus.indexOf("SUCCESSFUL") != -1 && paymentStatus.length() == "SUCCESSFUL".length());

                backFunc.onCallBack(result,res,"");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            backFunc.onCallBack(false,e.toString(),"");
        }
    }
    public  void decodeInitDcbPayment(String res)
    {
        Log.e(TAG, "InitDcbPayment:"+res);
        if(res.length() <= 0)
        {
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
                backFunc.onCallBack(false,res,"");
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
        Log.e(TAG, "decodePaymentResults:"+res);
        if(res.length() <= 0)
        {
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
                    backFunc.onCallBack(false,res,"");
                }
                else
                {
                    String content = jsonObject.getString("content");
                    JSONObject contentJsonObject = new JSONObject(content);
                    String content1 = contentJsonObject.getString("content");
                    JSONObject content1JsonObject = new JSONObject(content1);
                    String billingNumber = content1JsonObject.getString("billingNumber");

                    backFunc.onCallBack(true,res,billingNumber);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                backFunc.onCallBack(false,e.toString(),"");
            }
        }
    }

    public  void decodeInitAppBySdk(String res)
    {
        Log.e(TAG, "decodeInitAppBySdk:"+res);
        if(res.length() <= 0)
        {
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
                YoleSdkMgr.getsInstance().initBasicSdkResult(false,res);
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
                String areaCode = contentJsonObject.getString("areaCode");

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
                initSdkData.areaCode = areaCode;
                String stt = "；userCode:"+userCode;
                stt += "；productName:"+productName;
                stt += "；companyName:"+companyName;
                stt += "；currencySymbol:"+currencySymbol;
                stt += "；areaCode:"+areaCode;
                stt += "；smsFeeList:"+smsFeeList;

                Log.e(TAG, "decodeInitAppBySdk stt:"+stt);
                YoleSdkMgr.getsInstance().initBasicSdkResult(true,stt);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            YoleSdkMgr.getsInstance().initBasicSdkResult(false,e.toString());
        }
    }
    public  void getPaymentStatus(String res)
    {
        Log.e(TAG, "getPaymentStatus:"+res);
        initSdkData.payType.clear();
        if(res.length() <= 0)
        {
            YoleSdkMgr.getsInstance().paymentStatusCallBack.onCallBack(false,"",initSdkData.payType,false);
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
                YoleSdkMgr.getsInstance().paymentStatusCallBack.onCallBack(false,res,initSdkData.payType,false);
            }
            else
            {
                String content = jsonObject.getString("content");
                JSONObject contentJsonObject = new JSONObject(content);

                boolean adsOpen = contentJsonObject.optBoolean("adsOpen");
                initSdkData.adsOpen = adsOpen;

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
                String stt = "";
                stt += "；adsOpen:"+adsOpen;
                stt += "；payType:"+initSdkData.payType;
                Log.e(TAG, "getPaymentStatus stt:"+stt);
                YoleSdkMgr.getsInstance().paymentStatusCallBack.onCallBack(true,stt,initSdkData.payType,adsOpen);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            YoleSdkMgr.getsInstance().paymentStatusCallBack.onCallBack(false,e.toString(),initSdkData.payType,false);
        }
    }



}
