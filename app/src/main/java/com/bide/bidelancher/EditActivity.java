package com.bide.bidelancher;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bide.bidelancher.Until.WifiConnector;
import com.bide.bidelancher.Until.WifiHelper;

import java.lang.reflect.Method;

import edit.input.bide.com.commoneditext.widget.SoftKeyboardView;

public class EditActivity extends Activity {
    private String TAG ="main";
    public SoftKeyboardView softKeyboardView;
    public TextView mInputEdit;
    String wifiname,Capabilities;
    WifiConnector wac;
    WifiManager wifiManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wac = new WifiConnector(wifiManager, this);
        wifiname=getIntent().getStringExtra("wifiname");
        Capabilities=getIntent().getStringExtra("Capabilities");

//        mInputEdit=(EditText)findViewById(R.id.passwordtext);
        initView();
    }
    private void initView() {
        softKeyboardView = (SoftKeyboardView) findViewById(edit.input.bide.com.commoneditext.R.id.softKeyboardView);
        mInputEdit = (TextView) findViewById(R.id.passwordtext);
        //     mInputEdit.setText(wifiname);
        if(Build.VERSION.SDK_INT<=10){
            Log.e(TAG,"-----initView 1111");
            mInputEdit.setInputType(InputType.TYPE_NULL);
        }else{
            Log.e(TAG,"-----initView 2222");
            this.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(mInputEdit, false);
                mInputEdit.setCursorVisible(true);
                softKeyboardView.bindInputView(mInputEdit);
            } catch (Exception e) {
                Log.e(TAG,"-----method error");
                e.printStackTrace();
            }
            softKeyboardView.bindInputView(mInputEdit);
        }
        View.OnClickListener mOkListener =  new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.e("TAG","wifiname="+wifiname+"mInputEdit="+mInputEdit.toString()+"Capabilities="+Capabilities);
                WifiHelper wifiHelper = new WifiHelper(EditActivity.this);
                String password=mInputEdit.getText().toString().trim();
                Log.e("TAG","password="+password);
                if (password.length()<8){
                    Toast.makeText(EditActivity.this,"Password length cannot be less than 8",Toast.LENGTH_LONG).show();
                }else {
                    Log.e("TAG","wifiname="+wifiname+",password="+password);
//                    wifiHelper.connectWifi(wifiname,password, Capabilities);
                    String s = "aaaa";
                    try {
                        wac.connect(wifiname, password,
                                s.equals("") ? WifiConnector.WifiCipherType.WIFICIPHER_NOPASS : WifiConnector.WifiCipherType.WIFICIPHER_WPA);
                    } catch (Exception e) {
                        Log.e("TAG", "错误信息" + e);
                    }
                    finish();
                }

            }
        };
        View.OnLongClickListener onLongClickListener=new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mInputEdit.setText("");

                return true;
            }
        };
        softKeyboardView.setOkBtnClickListener(mOkListener);
        softKeyboardView.setondeleteClickListener(onLongClickListener);

    }
}
