package com.bide.bidelancher.Until;

/**
 * Created by Administrator on 2018/6/14.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;

public class WifiConnector {
    public static String INFOrmation = "Information";
    Handler mHandler;
    WifiManager wifiManager;
    Context context;
    private MediaPlayer mediaPlayer = null; //声频
    private final String TAG = "MainService";
    /**
     * 向UI发送消息
     *
     * @param info 消息
     */
    public void sendMsg(String info) {
        if (mHandler != null) {
            Message msg = new Message();
            msg.obj = info;
            mHandler.sendMessage(msg);// 向Handler发送消息
        } else {
            Log.e("wifi", info);
        }
    }


    //WIFICIPHER_WEP是WEP ，WIFICIPHER_WPA是WPA，WIFICIPHER_NOPASS没有密码
    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }


    // 构造函数
    public WifiConnector(WifiManager wifiManager, Context context) {
        this.wifiManager = wifiManager;
        this.context = context;

    }

    // 提供一个外部接口，传入要连接的无线网
    public void connect(String ssid, String password, WifiCipherType type) {
        Thread thread = new Thread(new ConnectRunnable(ssid, password, type));
        thread.start();

    }

    // 查看以前是否也配置过这个网络
    private WifiConfiguration isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = wifiManager
                .getConfiguredNetworks();
        Log.e("TAG", "existingConfigs=" + existingConfigs + "," + "wifiManager=" + wifiManager);
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }

        return null;
    }

    // 打开wifi功能
    private boolean openWifi() {
        boolean bRet = true;
        if (!wifiManager.isWifiEnabled()) {
            bRet = wifiManager.setWifiEnabled(true);
        }
        Log.e("TAG", "... is open wifi = " + bRet);
        return bRet;
    }

    private WifiConfiguration createWifiInfo(String SSID, String Password, WifiCipherType Type) {

        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        wifiManager.saveConfiguration();
        // nopass
        if (Type == WifiCipherType.WIFICIPHER_NOPASS) {
            config.allowedKeyManagement.set(KeyMgmt.NONE);
        }
        // wep
        if (Type == WifiCipherType.WIFICIPHER_WEP) {
            if (!TextUtils.isEmpty(Password)) {
                if (isHexWepKey(Password)) {
                    config.wepKeys[0] = Password;
                } else {
                    config.wepKeys[0] = "\"" + Password + "\"";
                }
            }
            config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
            config.allowedKeyManagement.set(KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        // wpa
        if (Type == WifiCipherType.WIFICIPHER_WPA) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
            Log.e(TAG,"WIFICIPHER_WPA方法");
        }
        return config;
    }


    class ConnectRunnable implements Runnable {
        private String ssid;

        private String password;

        private WifiCipherType type;

        private SharedPreferences sp;

        public ConnectRunnable(String ssid, String password, WifiCipherType type) {
            this.ssid = ssid;
            this.password = password;
            this.type = type;
        }

        @Override
        public void run() {
            try {
                // 打开wifi
                openWifi();
                sendMsg("opened");
                Thread.sleep(400);
                // 开启wifi功能需要一段时间(我在手机上测试一般需要1-3秒左右)，所以要等到wifi
                // 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
                while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
                    try {
                        // 为了避免程序一直while循环，让它睡个100毫秒检测……
                        Thread.sleep(100);
                    } catch (InterruptedException ie) {
                    }
                }

                WifiConfiguration wifiConfig = createWifiInfo(ssid, password,
                        type);

                //
                if (wifiConfig == null) {
                    sendMsg("wifiConfig is null!");
                    return;
                }
                sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
//                try {
                WifiConfiguration tempConfig = isExsits(ssid);
                Log.e("TAG", "tempConfig=" + tempConfig + "," + "ssid=" + ssid);
                if (tempConfig != null) {
                    wifiManager.removeNetwork(tempConfig.networkId);
                }
//                }catch (Exception e){
//
//                   Log.e("TAG","是否第一次联网e"+e);
//                }
                int netID = wifiManager.addNetwork(wifiConfig);
                boolean enabled = wifiManager.enableNetwork(netID, true);
                sendMsg("enableNetwork status enable=" + enabled);
                boolean connected = wifiManager.reconnect();
                sendMsg("enableNetwork connected=" + connected);
                if (enabled == true && connected == true) {
                    sendMsg("连接成功!");
                    Log.e("TAG", ssid + password);
//                    mediaPlayer= MediaPlayer.create(context, R.raw.net03);//网络连接成功
//                    mediaPlayer.start();//播放声音
                    Intent favorite = new Intent("com.bide.truing.wif.connect");
                    favorite.putExtra("ssid", ssid);
                    favorite.putExtra("password", password);
                    context.sendBroadcast(favorite);

//                    /**
//                     * 保存wifi
//                     */
//                    /**
//                     * 在保存wifi之前，先判断是否已经保存过
//                     */
//
//                    FileInputStream fis;
//                    try {
//                        fis = context.openFileInput("bisddsdddddebidssse.txt");
//                        byte[] buffer = new byte[fis.available()];
//                        fis.read(buffer);
//                        String aaa = new String(buffer);
//                        System.out.println(aaa);
//                        fis.close();
//                        if (aaa != null && !aaa.equals("")) {
//                            String[] strarray = aaa.split("[,]");
//                            for (int j = 0; j < strarray.length; j++) {
//                                String ssids = strarray[j].substring(0, strarray[j].indexOf("|"));
//                                String userIdJiequ = strarray[j].substring(strarray[j].indexOf("|"));
//                                String posswords = userIdJiequ.substring(1, userIdJiequ.length());
//                                Log.e("TAG", "pwd=" + posswords + "飒飒" + ssid);
//                                if (ssid != null && ssid.equals(ssids) && password.equals(posswords)) {
//                                    Log.e("TAG", "已经保存了");
//                                } else {
//                                    Log.e("TAG", "保存了");
//                                    /**
//                                     * 保存wifi
//                                     */
//                                    try {
//                                        FileOutputStream fos = context.openFileOutput("bisddsdddddebidssse.txt", Context.MODE_APPEND);
//                                        String name = ssids + "|" + password + ",";
//                                        fos.write(name.getBytes());
//                                        fos.close();
//
//                                    } catch (FileNotFoundException e) {
//                                        e.printStackTrace();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        } else {
//                            /**
//                             * 保存wifi
//                             */
//                            try {
//                                Log.e("TAG", "保存了密码");
//                                FileOutputStream fos = context.openFileOutput("bisddsdddddebidssse.txt", Context.MODE_APPEND);
//                                String name = ssid + "|" + password + ",";
//                                fos.write(name.getBytes());
//                                fos.close();
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                   }

                } else {
//                    mediaPlayer = MediaPlayer.create(context, R.raw.net04);//网络连接失败
//                    mediaPlayer.start();//播放声音
                }


//                Intent intent1= new Intent();
//                intent1.setAction(INFOrmation);
//                intent1.putExtra("types", "1");
//                context.sendBroadcast(intent1);

            } catch (Exception e) {
                // TODO: handle exception
                sendMsg(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static boolean isHexWepKey(String wepKey) {
        final int len = wepKey.length();

        // WEP-40, WEP-104, and some vendors using 256-bit WEP (WEP-232?)
        if (len != 10 && len != 26 && len != 58) {
            return false;
        }

        return isHex(wepKey);
    }

    private static boolean isHex(String key) {
        for (int i = key.length() - 1; i >= 0; i--) {
            final char c = key.charAt(i);
            if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a'
                    && c <= 'f')) {
                return false;
            }
        }

        return true;
    }

    /*
     * 对网络连接进行判断
     *
     * */
    public static boolean isNetWorkConnected(Context context) {

        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /*
     * 对ｗｉｆｉ网络连接进行判断
     * */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }



    public void startWifiAP(String ssid, String passwd) {
        //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
        Log.e("MainService", "...startWifiAP ssid = " + ssid + "...passwd = " + passwd);
        if (this.wifiManager.isWifiEnabled()) {
            this.wifiManager.setWifiEnabled(false);
        }

        if (!isWifiApEnabled()) {
            stratWifiAp(ssid, passwd);
        }
    }

    /**
     * 设置热点名称及密码，并创建热点
     *
     * @param mSSID
     * @param mPasswd
     */
    private void stratWifiAp(String mSSID, String mPasswd) {
        Log.e("MainService", "...stratWifiAp ssid = " + mSSID + "...passwd = " + mPasswd);

        Method method1 = null;
        try {
            //通过反射机制打开热点
            method1 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = new WifiConfiguration();

            netConfig.SSID = mSSID;
            netConfig.preSharedKey = mPasswd;

            netConfig.hiddenSSID = true;
            netConfig.status = WifiConfiguration.Status.ENABLED;

            netConfig.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            netConfig.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
//            netConfig.allowedKeyManagement.set(4);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            method1.invoke(wifiManager, netConfig, true);
//            wakeUpAndUnlock(context);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Log.e("MainService", "....wifiAp state = " + getWifiApState());
    }

    public void wakeUpAndUnlock(Context context, int timeout) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm
                .newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        // 点亮屏幕
        wl.acquire(timeout);
        // 释放
        wl.release();
    }

    public void wakeUpAndUnlock(Context context) {
//        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
//        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
//        // 解锁
//        kl.disableKeyguard();
        // 获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm
                .newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        // 点亮屏幕
        wl.acquire();
        // 释放
        wl.release();
    }

    /**
     * 热点开关是否打开
     *
     * @return
     */
    public boolean isWifiApEnabled() {
        try {
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭WiFi热点
     */
    public void closeWifiAp() {
//        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (isWifiApEnabled()) {
            try {
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);
                WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);
                Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method2.invoke(wifiManager, config, false);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @判断 是否含有sim卡
     **/
    public boolean readSIMCard() {

        TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);// 取得相关系统服务
        String imsi = manager.getSubscriberId(); // 取出IMSI
        System.out.println("取出IMSI" + imsi);

        if (imsi == null || imsi.length() <= 0) {
            Log.e("MainService", "....请确认sim卡是否插入或者sim卡暂时不可用！");
            return false;

        } else {
            return true;

        }

    }

    /**
     * 判断热点开启状态
     */
    public boolean getWifiApState() {
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApState");
            int temp = ((Integer) method.invoke(wifiManager));
            // 估计是某类一个常量吧，反正测试了一下，好像打开了wifi热点的话，返回的就是13，没有打开的话，返回的就是11
            return temp != 11;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 打开WiFi热点状态
     *
     * @param context
     * @return
     */
    public static boolean openWifiAP(Context context, WifiConfiguration apConfig) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
        wifiManager.setWifiEnabled(false);

        try {
            //通过反射调用设置热点
            Method method = wifiManager.getClass().getMethod(
                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            //返回热点状态
            return (Boolean) method.invoke(wifiManager, apConfig, true);
        } catch (Exception e) {
            return false;
        }
    }
}
