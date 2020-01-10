package com.bide.bidelancher.Adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bide.bidelancher.R;
import com.bide.bidelancher.SettingActivity;

import java.util.IdentityHashMap;


/**
 * Created by Administrator on 2019/12/17.
 */

public class GalleryAdapter extends PagerAdapter {
    private int[] mData;
    private Context mContext;

    public GalleryAdapter(Context context, int[] data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.length;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View view = View.inflate(container.getContext(), R.layout.item, null);
        ImageView imageView = view.findViewById(R.id.iv_icon);
        imageView.setImageResource(mData[position]);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "当前条目:" + position, Toast.LENGTH_SHORT).show();
                if (position == 1) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ComponentName cp = new ComponentName("com.enqualcomm.imessage", "com.enqualcomm.imessage.ui.MicroChatActivity");
                    intent.setComponent(cp);
                    mContext.startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ComponentName cp = new ComponentName("com.enqualcomm.imessage", "com.enqualcomm.imessage.ui.PhoneDirectoryActivity");
                    intent.setComponent(cp);
                    mContext.startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent(mContext, SettingActivity.class);
                    mContext.startActivity(intent);
                }else if (position == 4) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ComponentName cp = new ComponentName("com.enqualcomm.imessage", "com.enqualcomm.imessage.ui.pair.PairActivity");
                    intent.setComponent(cp);
                    mContext.startActivity(intent);
                }

            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
