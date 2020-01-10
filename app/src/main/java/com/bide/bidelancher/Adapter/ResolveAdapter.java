package com.bide.bidelancher.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bide.bidelancher.Model.ResolveModel;
import com.bide.bidelancher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/12/16.
 */

public class ResolveAdapter extends BaseAdapter{
    private Activity context;
    private List<ResolveModel> mList = new ArrayList<ResolveModel>();
    protected LayoutInflater inflater;
    private int mResource;// 绑定的条目界面
    private LayoutInflater mInflater;

    public ResolveAdapter(Activity context, int resource) {
        this.context = context;
        this.mResource = resource;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void setData(List<ResolveModel> list) {
        mList = list;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if (mList == null || mList.size() == 0) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;
        TextView nameview=null;
        if (convertView==null){
            convertView = mInflater.inflate(mResource, null);// 生成条目界面对象
            nameview=(TextView)convertView.findViewById(R.id.appname);
            imageView = (ImageView) convertView.findViewById(R.id.imageview);
            ViewCache cache = new ViewCache();
            cache.nameview = nameview;
            cache.imageView = imageView;
            convertView.setTag(cache);
        }else {
            ViewCache cache = (ViewCache) convertView.getTag();
            nameview = cache.nameview;
            imageView = cache.imageView;
        }
        ResolveModel model = mList.get(position);
        nameview.setText(model.getName());
        imageView.setImageResource(R.drawable.high_light);
        return convertView;
    }
    private final class ViewCache {
        public ImageView imageView;
        public TextView nameview;
    }
}
