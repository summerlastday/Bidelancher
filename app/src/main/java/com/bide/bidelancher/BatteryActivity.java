package com.bide.bidelancher;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BatteryActivity extends Activity {
LinearLayout layout_main;
float mPosX,mPosY,mCurPosX,mCurPosY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        layout_main=(LinearLayout)findViewById(R.id.ll_main);
        layout_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mPosX = event.getX();
                        mPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurPosX = event.getX();
                        mCurPosY = event.getY();

                        break;
                    case MotionEvent.ACTION_UP:
                        float Y=mCurPosY - mPosY;
                        float X=mCurPosX-mPosX;
                        if(Math.abs(Y)>Math.abs(X)){
                            if(Y>0){
                                Toast.makeText(BatteryActivity.this,"下滑",Toast.LENGTH_SHORT).show();
//                                slideDown(); //改成自己想要执行的代码
                            }else{
                                Toast.makeText(BatteryActivity.this,"上滑",Toast.LENGTH_SHORT).show();
//                                slideUp();//改成自己想要执行的代码
                            }
                        }else{
                            if(X>0){
                                Toast.makeText(BatteryActivity.this,"左滑",Toast.LENGTH_SHORT).show();
//                                slideRight();//改成自己想要执行的代码
                            }else{
                                Toast.makeText(BatteryActivity.this,"右滑",Toast.LENGTH_SHORT).show();
//                                slideLeft();//改成自己想要执行的代码
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }
}
