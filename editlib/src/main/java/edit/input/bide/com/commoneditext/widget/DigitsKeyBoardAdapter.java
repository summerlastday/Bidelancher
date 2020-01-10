package edit.input.bide.com.commoneditext.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import edit.input.bide.com.commoneditext.R;

class DigitsKeyBoardAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<Map<String, String>> valueList;

    public DigitsKeyBoardAdapter(Context context, ArrayList<Map<String, String>> valueList) {
        mContext = context;
        this.valueList = valueList;
    }

    @Override
    public int getCount() {
        return this.valueList.size();
    }

    @Override
    public Object getItem(int i) {
        return valueList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.grid_item_soft,null);
            viewHolder = new ViewHolder();
            viewHolder.mBtnKey = (TextView) convertView.findViewById(R.id.btn_keys);
            viewHolder.imgDelete = (RelativeLayout) convertView.findViewById(R.id.imgDelete);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        if (position == 9) {
//            viewHolder.imgDelete.setVisibility(View.INVISIBLE);
//            viewHolder.mBtnKey.setVisibility(View.VISIBLE);
//            viewHolder.mBtnKey.setText(valueList.get(position).get("name"));
//            //viewHolder.btnKey.setBackgroundColor(Color.parseColor("#e0e0e0"));
//        } else if (position == 11) {
//            viewHolder.mBtnKey.setBackgroundResource(R.drawable.keyboard_delete);
//            viewHolder.imgDelete.setVisibility(View.VISIBLE);
//            viewHolder.mBtnKey.setVisibility(View.INVISIBLE);
//        } else {
            viewHolder.imgDelete.setVisibility(View.INVISIBLE);
            viewHolder.mBtnKey.setVisibility(View.VISIBLE);
            viewHolder.mBtnKey.setText(valueList.get(position).get("name"));
        Log.e("TAG","valueList.get(position).get(\"name\")"+valueList.get(position).get("name"));
//        }

        return convertView;
    }

    private class ViewHolder {
        public TextView mBtnKey;
        public RelativeLayout imgDelete;
    }
}
