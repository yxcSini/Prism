package com.tencent.wstt.gt.controller;

import android.content.Context;

import com.tencent.wstt.gt.collector.GTRCollector;
import com.tencent.wstt.gt.service.PrismServerSave;

import java.io.IOException;

public class GTRController {
    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    /** 模式有两种
     /* 1.启动应用时开启采集并保存数据（缺点：无法准确获取启动时长）（默认）
     /* 2.启动应用时不开启采集，接受到广播时再启动采集并保存数据（缺点：开启之前的数据会被遗漏）
     * @param context
     * */
    public static void init(Context context) {
        sContext = context.getApplicationContext();

        try {
            PrismServerSave.initBufferedWriter(sContext.getPackageName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        GTRCollector.init(sContext);

        // 注册广播控制器
        GTRBroadcastReceiver.start(context);
    }
}
