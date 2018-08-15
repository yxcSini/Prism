package com.paisheng.prism.test;

import android.app.Application;

//import com.tencent.wstt.gt.collector.GTRCollector;

import com.paisheng.prismsdk.Prism;

/**
 * Created by yexiaochuan on 2018/5/28.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Prism.init(this);
//        GTRCollector.init(this);
    }
}
