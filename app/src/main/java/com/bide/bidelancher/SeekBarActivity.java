package com.bide.bidelancher;

import android.app.Activity;
import android.content.ContentResolver;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bide.bidelancher.Until.BrightnessUtils;

public class SeekBarActivity extends Activity {
    private int mBrightness = 0;
    private SeekBar mSeekBar_light;
    private TextView mTextView_light;
    Button button1,button2,button3,button4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_bar);
        mSeekBar_light = (SeekBar) findViewById(R.id.mySeekBar_light);
        mTextView_light = (TextView) findViewById(R.id.mTextView_light);

        button1=(findViewById(R.id.button1));
        button2=(findViewById(R.id.button2));
        button3=(findViewById(R.id.button3));
        button4=(findViewById(R.id.button4));
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentResolver resolver = getContentResolver();
                Settings.System.putLong(resolver, android.provider.Settings.System.SCREEN_OFF_TIMEOUT, 5 * 1000);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentResolver resolver = getContentResolver();
                Settings.System.putLong(resolver, android.provider.Settings.System.SCREEN_OFF_TIMEOUT, 20 * 1000);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentResolver resolver = getContentResolver();
                Settings.System.putLong(resolver, android.provider.Settings.System.SCREEN_OFF_TIMEOUT, 30 * 1000);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mSeekBar_light
                .setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());

        mBrightness = BrightnessUtils.getScreenBrightness(SeekBarActivity.this);
        mTextView_light.setText(""+mBrightness);
        mSeekBar_light.setProgress(mBrightness);
    }
    // 进度条值改变时，调节屏幕亮度
    private class OnSeekBarChangeListenerImp implements
            SeekBar.OnSeekBarChangeListener {

        // 触发操作，拖动
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            mBrightness = seekBar.getProgress();
            mTextView_light.setText(""+mBrightness);
            BrightnessUtils.setSystemBrightness(SeekBarActivity.this, mBrightness);
        }

        // 表示进度条刚开始拖动，开始拖动时候触发的操作
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        // 停止拖动时候
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }
    }
}
