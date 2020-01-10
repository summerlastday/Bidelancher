package com.bide.bidelancher;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bide.bidelancher.Until.SystemUtil;

public class AboutdeviceActivity extends Activity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutdevice);
        textView=findViewById(R.id.textview);
        textView.setText("手机厂商：" + SystemUtil.getDeviceBrand()+"手机型号：" + SystemUtil.getSystemModel()+"手机当前系统语言：" + SystemUtil.getSystemLanguage()+"Android系统版本号：" + SystemUtil.getSystemVersion()+"手机IMEI：" + SystemUtil.getIMEI(getApplicationContext()));
    }
    private void showSystemParameter() {
        String TAG = "系统参数：";
        Log.e(TAG, "手机厂商：" + SystemUtil.getDeviceBrand());
        Log.e(TAG, "手机型号：" + SystemUtil.getSystemModel());
        Log.e(TAG, "手机当前系统语言：" + SystemUtil.getSystemLanguage());
        Log.e(TAG, "Android系统版本号：" + SystemUtil.getSystemVersion());
        Log.e(TAG, "手机IMEI：" + SystemUtil.getIMEI(getApplicationContext()));
    }
}
