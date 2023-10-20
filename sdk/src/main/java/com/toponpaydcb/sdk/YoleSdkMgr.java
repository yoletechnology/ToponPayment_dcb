package com.toponpaydcb.sdk;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.toponpaydcb.sdk.callback.PaymentStatusCallBackFunction;
import com.toponpaydcb.sdk.data.InitSdkData;
import com.toponpaydcb.sdk.dcb.PaymentView;
import com.toponpaydcb.sdk.callback.CallBackFunction;

public class YoleSdkMgr extends YoleSdkBase{

    private String TAG = "Yole_YoleSdkMgr";
    private static  YoleSdkMgr _instance = null;
    public String ruPayOrderNum = "";
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
                            user.getCountryCode(),
                            user.getMcc(),
                            user.getMnc(),
                            user.getCpCode()
                    );

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void startDcbActivity(String webUrl)
    {
        Intent i = new Intent(_activity, PaymentView.class);
        i.putExtra(YoleSdkMgr.RETURN_INFO, webUrl);
        _activity.startActivity(i);
    }
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


}
