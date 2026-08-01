package com.bytedance.demo;

import android.app.Application;
import android.os.Environment;

import com.bytedance.raphael.Raphael;

import java.io.File;

public class DemoApp extends Application {

    static {
        System.loadLibrary("demo");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        File file = new File(getExternalCacheDir(), "raphael");
        File[] lists = file.listFiles();
        if (lists != null) {
            for (int i = 0; i < lists.length; i++) {
                lists[i].delete();
            }
        }
        String space = new File(getExternalCacheDir(), "raphael").getAbsolutePath();
        Raphael.start(Raphael.MAP64_MODE | Raphael.ALLOC_MODE | 0x0F0000 | 1024, space, null);
//      Raphael.start(Raphael.MAP64_MODE|Raphael.ALLOC_MODE|0x0F0000|1024, space, ".*libdemo\\.so$");
    }
}