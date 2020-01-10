package com.bide.bidelancher.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bide.bidelancher.Model.WifiBean;
import com.bide.bidelancher.R;
import com.bide.bidelancher.Until.AppConstants;

import java.util.List;

/**
 * Created by Administrator on 2020/1/7.
 */

public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.MyViewHolder> {

    private Context mContext;
    private List<WifiBean> resultList;
    private onItemClickListener onItemClickListener;

    public void setOnItemClickListener(WifiListAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public WifiListAdapter(Context mContext, List<WifiBean> resultList) {
        this.mContext = mContext;
        this.resultList = resultList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.wifi_list_recycle_item, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final WifiBean bean = resultList.get(position);
        holder.tvItemWifiName.setText(bean.getWifiName());
        holder.tvItemWifiStatus.setText("("+bean.getState()+")");
        if (bean.getState().equals("已连接")){
            holder.imageviewtrue.setVisibility(View.VISIBLE);
        }else {
            holder.imageviewtrue.setVisibility(View.INVISIBLE);
        }
        Log.e("TAG","bean="+bean.getScanResult()+"bean.getScanResult()="+Math.abs(bean.getScanResult().level));
        //判断信号强度，显示对应的指示图标+
        if (Math.abs(bean.getScanResult().level) > 100) {
            Log.e("TAg","scanResult.SSID");
            holder.imageView.setBackgroundResource(R.drawable.setting_icon_wifi_1);
        } else if (Math.abs(bean.getScanResult().level) > 80) {
            holder.imageView.setBackgroundResource(R.drawable.setting_icon_wifi_1);
        } else if (Math.abs(bean.getScanResult().level) > 70) {
            holder.imageView.setBackgroundResource(R.drawable.setting_icon_wifi_1);
        } else if (Math.abs(bean.getScanResult().level) > 60) {
            holder.imageView.setBackgroundResource(R.drawable.setting_icon_wifi_2);
        } else if (Math.abs(bean.getScanResult().level) > 50) {
            holder.imageView.setBackgroundResource(R.drawable.setting_icon_wifi_3);
        } else {
            holder.imageView.setBackgroundResource(R.drawable.setting_icon_wifi_4);
        }
        //已连接或者正在连接状态的wifi都是处于集合中的首位，所以可以写出如下判断
        if(position == 0  && (AppConstants.WIFI_STATE_ON_CONNECTING.equals(bean.getState()) || AppConstants.WIFI_STATE_CONNECT.equals(bean.getState()))){
//            holder.tvItemWifiName.setTextColor(mContext.getResources().getColor(R.color.homecolor1));
//            holder.tvItemWifiStatus.setTextColor(mContext.getResources().getColor(R.color.homecolor1));
        }else{
//            holder.tvItemWifiName.setTextColor(mContext.getResources().getColor(R.color.gray_home));
//            holder.tvItemWifiStatus.setTextColor(mContext.getResources().getColor(R.color.gray_home));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view,position,bean);
            }
        });
    }

    public void replaceAll(List<WifiBean> datas) {
        if (resultList.size() > 0) {
            resultList.clear();
        }
        resultList.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvItemWifiName, tvItemWifiStatus;
        ImageView imageView,imageviewtrue;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvItemWifiName = (TextView) itemView.findViewById(R.id.tv_item_wifi_name);
            tvItemWifiStatus = (TextView) itemView.findViewById(R.id.tv_item_wifi_status);
            imageView=(ImageView)itemView.findViewById(R.id.imageView);
            imageviewtrue=(ImageView)itemView.findViewById(R.id.imageviewtrue);
        }

    }

    public interface onItemClickListener{
        void onItemClick(View view, int postion, Object o);
    }


}
