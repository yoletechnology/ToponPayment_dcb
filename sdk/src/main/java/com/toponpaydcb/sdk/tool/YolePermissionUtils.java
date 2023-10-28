package com.toponpaydcb.sdk.tool;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

public class YolePermissionUtils {

    public static String TAG = "Yole_PermissionUtils";

    private static int mRequestCode = -1;

    private static OnPermissionListener mOnPermissionListener = null;
    /**
     * 权限请求回调
     */
    public interface OnPermissionListener {

        //权限通过
        void onPermissionGranted();

        //权限拒绝
        void onPermissionDenied();

    }

    /**
     * 请求响应的权限
     * @param context     Activity
     * @param requestCode 请求码
     * @param permissions 请求权限
     * @param listener    权限请求监听
     */
    public static void requestPermissions(Activity context, String[] permissions, int requestCode, OnPermissionListener listener) {
        mOnPermissionListener = listener;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mOnPermissionListener.onPermissionGranted();
            mOnPermissionListener = null;
            return;
        }

        List<String> deniedPermissions = new ArrayList<String>();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);//添加未授予的权限
            }
        }

        if (deniedPermissions.size() > 0) {
            mRequestCode = requestCode;
            //其中请求码范围在[0,65535]
            ActivityCompat.requestPermissions(context, deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
        } else {
            mOnPermissionListener.onPermissionGranted();
        }
    }

    /**
     * 验证所有权限是否都已经授权
     */
    private static boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {//拒绝权限
                System.out.println("grantResult: "+grantResult);
                return false;
            }
        }
        return true;
    }

    /**
     * 请求权限结果，对应Activity中onRequestPermissionsResult()方法。
     */
    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (mRequestCode != -1 && requestCode == mRequestCode) {
            if (mOnPermissionListener != null) {
                if (verifyPermissions(grantResults)) {
                    mOnPermissionListener.onPermissionGranted();
                } else {
                    mOnPermissionListener.onPermissionDenied();
                }
            }
            mOnPermissionListener = null;
        }
    }

}
