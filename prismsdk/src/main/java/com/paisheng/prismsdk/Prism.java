package com.paisheng.prismsdk;

import android.app.Application;
import android.content.Context;

import com.paisheng.prismsdk.monitor.TagMonitor;
import com.paisheng.prismsdk.report.PrismSDCardPath;
import com.paisheng.prismsdk.view.PrismNotification;
import com.tencent.wstt.gt.collector.GTRCollector;
import com.tencent.wstt.gt.controller.GTRBroadcastReceiver;
import com.tencent.wstt.gt.controller.GTRController;
import com.tencent.wstt.gt.service.PrismServerSave;

import java.io.IOException;

/**
 * Created by yexiaochuan on 2018/7/3.
 */

public class Prism {
    public static void init(Application application) {
        //AOP初始化
        TagMonitor.init();

        new PrismNotification(application).onNotificationInit();

        PrismSDCardPath.init(application.getPackageName());

        GTRController.init(application);
    }
}
