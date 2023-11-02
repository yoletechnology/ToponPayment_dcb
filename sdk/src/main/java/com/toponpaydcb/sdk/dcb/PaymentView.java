
package com.toponpaydcb.sdk.dcb;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.toponpaydcb.sdk.R;
import com.toponpaydcb.sdk.YoleSdkMgr;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PaymentView extends Activity {
    public String TAG = "Yole_PaymentView";
    public Activity m_activity = null;

    private BridgeWebView webview;
    public static SystemBarTintManager tintManager = null;
    public int NavigationBarHeight = 0;//导航栏的高
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_activity = this;
        setContentView(R.layout.payment_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            tintManager = new SystemBarTintManager(m_activity);
            NavigationBarHeight = tintManager.getConfig().getNavigationBarHeight();
            Log.d(TAG,  ""+tintManager.getConfig().getNavigationBarHeight());


            // 激活状态栏
            tintManager.setStatusBarTintEnabled(false);
            // enable navigation bar tint 激活导航栏
            tintManager.setNavigationBarTintEnabled(false);
            tintManager.setNavigationBarAlpha(0);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            // 激活状态栏
            tintManager.setStatusBarTintEnabled(true);

        }
        String weburl = getIntent().getStringExtra(YoleSdkMgr.RETURN_INFO);
        openHtml(weburl);
    }



    public void openHtml(String url)
    {
        webview = (BridgeWebView) findViewById(R.id.webview);
        webview.setDefaultHandler(new DefaultHandler());
        webview.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(false);
//        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.supportMultipleWindows();
        webSettings.setAllowContentAccess(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
//        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webview.getSettings().setLoadWithOverviewMode(true);
//        webview.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Log.e(TAG, "webview keyCode：" + keyCode);
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK ) {  //表示按返回键 时的操作
////                        mWebView.goBack();   //后退
//                        return true;    //已处理
//                    }
//                }
//                return false;
//            }
//        });


        webview.registerHandler("getCountryCode", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.e("TAG", "js返回：" + data);

                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("dataURL","1");
                JSONObject json = new JSONObject( hashMap );
                //Android返回给JS的消息
                function.onCallBack(json.toString());
            }
        });
        webview.registerHandler("closeH5Web", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {

                Log.e("TAG", "js返回：" + data);
                try {

                    JSONObject jsonObject = new JSONObject(data);
                    String status = jsonObject.getString("status");
                    String billingNumber = jsonObject.getString("billingNumber");

                    boolean result = (status.indexOf("paid") != -1 && status.length() == "paid".length());
                    YoleSdkMgr.getsInstance().user.getPayCallBack().onCallBack(result,status,billingNumber);

                    m_activity.finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        webview.loadUrl(url);
    }

    @Override //当点击了返回键并且能够返回的时候
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e(TAG, "onKeyDown：" + keyCode);
        if(keyCode==KeyEvent.KEYCODE_BACK ){ //如果点击了返回键并且webView能够返回
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}