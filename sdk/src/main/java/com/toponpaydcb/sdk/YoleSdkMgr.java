package com.toponpaydcb.sdk;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.toponpaydcb.sdk.callback.PaymentStatusCallBack;
import com.toponpaydcb.sdk.callback.PaymentStatusCallBackFunction;
import com.toponpaydcb.sdk.data.InitSdkData;
import com.toponpaydcb.sdk.dcb.PaymentView;
import com.toponpaydcb.sdk.callback.CallBackFunction;

import java.util.ArrayList;
import java.util.List;

public class YoleSdkMgr extends YoleSdkBase{

    private String TAG = "Yole_YoleSdkMgr";
    private static  YoleSdkMgr _instance = null;

    public static final String RETURN_INFO = "com.toponpaydcb.sdk.info";

    public static YoleSdkMgr getsInstance() {
        if(YoleSdkMgr._instance == null)
        {
            YoleSdkMgr._instance = new YoleSdkMgr();
        }
        return YoleSdkMgr._instance;
    }
    private YoleSdkMgr() {
        Log.e(TAG,"YoleSdkMgr");
    }
    public String getCurrencySymbol()
    {
        if(this.user != null && this.user.initSdkData != null)
        {
            return this.user.initSdkData.currencySymbol;
        }

        return "";
    }
    public List<InitSdkData.PayType> getPaymentList()
    {
        if(this.user != null && this.user.initSdkData != null)
        {
            return this.user.initSdkData.payType;
        }

        return new ArrayList();
    }
    public boolean getAdsOpen()
    {
        if(this.user != null && this.user.initSdkData != null)
        {
            return this.user.initSdkData.adsOpen;
        }

        return false;
    }
    public boolean getIsInitSuccess()
    {
        return this.isSdkInitSuccess;
    }
    /*****************************************************************/
    /************************bcd支付*********************************/
    /*****************************************************************/
    /** bcd支付*/
    Activity _activity = null;
    public void bcdStartPay(Activity act, String amount, String orderNumber, CallBackFunction callBack)
    {

        if(user.getConfig().isDcb() == false)
        {
            callBack.onCallBack(false,"sdk初始化时，未接入Dcb模块","");
            return;
        }
        if(user.initSdkData == null)
        {
            callBack.onCallBack(false,"支付方式不可用","");
            return;
        }
        _activity = act;
        user.setAmount(amount);
        user.setPayOrderNum(orderNumber);
        user.setPayCallBack(new CallBackFunction(){
            @Override
            public void onCallBack(boolean data,String info,String billingNumber) {
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadingDialog.getInstance(act).hideDialog();
                        if(data == true){

                        }else{
                        }
                        if(isDebugger)
                            Toast.makeText(act, act.getString(R.string.results)+":"+info, Toast.LENGTH_SHORT).show();
                        callBack.onCallBack(data,info,billingNumber);
                    }
                });
            }
        });

        if(this.getBCDFeasibility(act) == false)
        {
            YoleSdkMgr.getsInstance().user.getPayCallBack().onCallBack(false,act.getString(R.string.parameter_error),"");
            return;
        }

        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    request.initDcbPayment(
                            user.getAmount(),
                            user.getPayOrderNum(),
                            user.getLanguage(),
                            user.getCpCode()
                    );

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //启动dcb 支付界面
    public void startDcbActivity(String webUrl)
    {
        Intent i = new Intent(_activity, PaymentView.class);
        i.putExtra(YoleSdkMgr.RETURN_INFO, webUrl);
        _activity.startActivity(i);
    }
    //查询支付结果
    public void getDcbPaymentStatus(Activity act, String orderNumber, PaymentStatusCallBackFunction callBack)
    {
        user.setPayCallBack(new CallBackFunction(){
            @Override
            public void onCallBack(boolean result, String info, String billingNumber) {
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadingDialog.getInstance(act).hideDialog();
                        if(result == true){

                        }else{
                        }
                        callBack.onCallBack(result,info);
                    }
                });
            }


        });

        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    request.getDcbPaymentStatus(orderNumber);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**获取支付方式*/
    public void getPaymentStatus(PaymentStatusCallBack _callBack) {

        user.info.updateMccOrMnc();

        this.getPaymentStatus(user.info.mccWithMnc.getMccWithMnc(0),user.info.mccWithMnc.getMccWithMnc(1),_callBack);
    }

    public void getPaymentStatus(String MccWithMnc1, String MccWithMnc2, PaymentStatusCallBack _callBack) {

        String finalMccWithMnc1 = MccWithMnc1;
        String finalMccWithMnc2 = MccWithMnc2;
        paymentStatusCallBack = _callBack;
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    request.getPaymentKeyList(
                            finalMccWithMnc1,
                            finalMccWithMnc2
                    );

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



}
