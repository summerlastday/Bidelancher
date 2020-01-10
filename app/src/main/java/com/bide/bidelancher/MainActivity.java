package com.bide.bidelancher;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.bide.bidelancher.Adapter.GalleryAdapter;

public class MainActivity extends Activity {
     ViewPager mViewPager;
     LinearLayout ll_layout;
    private int[] mPics = new int[]{R.mipmap.image4, R.mipmap.image, R.mipmap.image2, R.mipmap.image1, R.mipmap.image3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager=(ViewPager)findViewById(R.id.viewPager);
        ll_layout=(LinearLayout)findViewById(R.id.ll_layout);
//          mViewPager.setPageMargin(20);
        //设置适配器
        mViewPager.setAdapter(new GalleryAdapter(this, mPics));
        mViewPager.setOffscreenPageLimit(mPics.length);
        mViewPager.setCurrentItem(1);
        ll_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mViewPager.dispatchTouchEvent(motionEvent);
            }
        });
    }
}
