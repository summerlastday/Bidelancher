package com.bide.bidelancher;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bide.bidelancher.Adapter.ResolveAdapter;
import com.bide.bidelancher.Model.ResolveModel;

import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends Activity {
    ListView mlistview;
    List<ResolveModel> mContentList;
    ResolveAdapter madapter;
    int ibattery;
    public String Battery = "100";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mlistview = (ListView) findViewById(R.id.mlistview);
        mlistview.setOnItemClickListener(new ItemClickListener());
        show3();
        loadData();
        // 注册广播接受者java代码
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        // 创建广播接受者对象
        BatteryReceiver batteryReceiver = new BatteryReceiver();
        // 注册receiver
        registerReceiver(batteryReceiver, intentFilter);
    }
    /**
     * 广播接受者
     */
    class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // 判断它是否是为电量变化的Broadcast Action
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                // 获取当前电量
                int level = intent.getIntExtra("level", 0);
                // 电量的总刻度
                int scale = intent.getIntExtra("scale", 100);
                // 把它转成百分比
                Battery = String.valueOf(((level * 100) / scale));
                ibattery = Integer.valueOf(Battery);

                Log.e("TAG", "....ACTION_BATTERY_CHANGED   iBattery = " + ibattery);

            }
        }
    }
    private final class ItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
            String name = mContentList.get(position).getName();
//            Toast.makeText(getActivity(),"点击的是="+photoid,Toast.LENGTH_SHORT).show();
            if (name.equals("wifi设置")) {
                Intent intent=new Intent(SettingActivity.this, WifiListActivity.class);
                startActivity(intent);
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                ComponentName cp = new ComponentName("com.sjl.wifi", "com.sjl.wifi.activity.WifiListActivity");
//                intent.setComponent(cp);
//                startActivity(intent);
            } else if (name.equals("wifi热点")) {
                Intent intent = new Intent(SettingActivity.this, WifiApActivity.class);
                startActivity(intent);
            } else if (name.equals("电池")) {
                Intent intent = new Intent(SettingActivity.this, BatteryActivity.class);
                startActivity(intent);
//                Intent powerUsageIntent = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
//                ResolveInfo resolveInfo = getPackageManager().resolveActivity(powerUsageIntent, 0);
//                if (resolveInfo != null) {
//                    startActivity(powerUsageIntent);
//                }
            } else if (name.equals("日期时间")) {
                Intent intent = new Intent(SettingActivity.this, DataTimePickerActivity.class);
                startActivity(intent);
            } else if (name.equals("关于设备")) {
                Intent intent = new Intent(SettingActivity.this, AboutdeviceActivity.class);
                startActivity(intent);
            } else if (name.equals("显示")) {
                Intent intent = new Intent(SettingActivity.this, SeekBarActivity.class);
                startActivity(intent);
            }else if (name.equals("工厂模式")) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cp = new ComponentName("com.hongjingtel.factorytest", "com.hongjingtel.factorytest.FactoryTestMenu");
                intent.setComponent(cp);
                startActivity(intent);
            }else if (name.equals("工程模式")) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cp = new ComponentName("com.sprd.engineermode", "com.sprd.engineermode.EngineerModeActivity");
                intent.setComponent(cp);
                startActivity(intent);
            }

        }
    }

    // 自定义适配器
    private void show3() {
        madapter = new ResolveAdapter(SettingActivity.this, R.layout.listview_layout);
        mlistview.setAdapter(madapter);
    }

    /**
     * 刷新画面
     */
    private void refreshUI(List<ResolveModel> list) {
        madapter.setData(list);

    }

    private void loadData() {
        mContentList = new ArrayList<ResolveModel>();
        ResolveModel model11 = new ResolveModel();
        model11.setName("拨号");
        model11.setIconimg(R.drawable.high_light);
        mContentList.add(model11);
        ResolveModel model = new ResolveModel();
        model.setName("wifi设置");
        model.setIconimg(R.drawable.high_light);
        mContentList.add(model);
        ResolveModel model1 = new ResolveModel();
        model1.setName("wifi热点");
        model1.setIconimg(R.drawable.high_light);
        mContentList.add(model1);
        ResolveModel model2 = new ResolveModel();
        model2.setName("电池");
        model2.setIconimg(R.drawable.high_light);
        mContentList.add(model2);
        ResolveModel model3 = new ResolveModel();
        model3.setName("系统语言");
        model.setIconimg(R.drawable.high_light);
        mContentList.add(model3);
        ResolveModel model4 = new ResolveModel();
        model4.setName("显示");
        model4.setIconimg(R.drawable.high_light);
        mContentList.add(model4);
        ResolveModel model5 = new ResolveModel();
        model5.setName("日期时间");
        model5.setIconimg(R.drawable.high_light);
        mContentList.add(model5);
        ResolveModel model6 = new ResolveModel();
        model6.setName("关于设备");
        model6.setIconimg(R.drawable.high_light);
        mContentList.add(model6);
        ResolveModel modeFactory = new ResolveModel();
        modeFactory.setName("工厂模式");
        modeFactory.setIconimg(R.drawable.high_light);
        mContentList.add(modeFactory);
        ResolveModel modeEngineerMode = new ResolveModel();
        modeEngineerMode.setName("工程模式");
        modeEngineerMode.setIconimg(R.drawable.high_light);
        mContentList.add(modeEngineerMode);
        refreshUI(mContentList);
    }
}
