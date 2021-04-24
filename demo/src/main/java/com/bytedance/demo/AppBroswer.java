package com.bytedance.demo;

import android.content.Context;
import android.util.Log;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class AppBroswer {
    private static String TAG = "AppBroswer";
    private static ArrayList<TbsListener> tbsListeners = new ArrayList<>();
    private static volatile boolean isinit = false;
    /** 默认成功，如果失败，直接进去 */
    private static boolean onViewInitFinished = true;
    private static SimpleDateFormat dateFormat;

    static {
        // only hours.
        dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    }

    public static void initX5Environment(Context con) {
        if (isinit) {
            return;
        }
        final Context context = con.getApplicationContext();
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d(TAG, "onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                isinit = true;
            }
        };
        //官方防止dex2odex 太慢导致的ANR的解决方案;
        HashMap<String, Object> map = new HashMap<>();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        //x5内核初始化接口
//        看了下好像一类问题。一般这个问题的是同时调用initX5Enviroment和new WebView 出现的，在大版本升级的时候由于拉到开关会清掉原有的内核。
//        app有个最快的兼容方式就是把QbSdk.initX5Enviroment替换成QbSdk.preInit
//        https://bugly.qq.com/v2/crash-reporting/crashes/a0df5ed682/1329191?pid=1
//        QbSdk.initX5Environment(context, cb);
        QbSdk.initX5Environment(context, cb);
    }
}
