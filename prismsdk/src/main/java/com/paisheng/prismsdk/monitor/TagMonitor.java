package com.paisheng.prismsdk.monitor;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;

import com.tencent.wstt.gt.client.GTRClient;
import com.tencent.wstt.gt.collector.monitor.AbsMonitor;


/**
 * <br> ClassName:   TagMonitor
 * <br>
 * <br> Author:      yexiaochuan
 * <br> Date:        2018/8/8 14:14
 */
public class TagMonitor {
    public static final String TAG = "TagMonitor";
    // 监控相关：
    private static int interval = 3000;
    protected static Handler handler;

    public static void init() {
        if (handler == null) {
            HandlerThread handlerThread = new HandlerThread("GTRTagMonitorThread");
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper()) {
                @Override
                public void dispatchMessage(Message msg) {
                    super.dispatchMessage(msg);
                    String data = msg.getData().getString("TagData");

                    if (GTRClient.getHandler() != null) {
                        GTRClient.pushData(data);
                        return;
                    }

                    if (handler != null) {
                        Message message = new Message();
                        message.setData(msg.getData());
                        handler.sendMessageDelayed(message, interval);
                    }
                }
            };
        }
    }

    public static void start(String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }

        if (handler == null) {
            HandlerThread handlerThread = new HandlerThread("GTRTagMonitorThread");
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper()) {
                @Override
                public void dispatchMessage(Message msg) {
                    super.dispatchMessage(msg);
                    String data = msg.getData().getString("TagData");

                    if (GTRClient.getHandler() != null) {
                        GTRClient.pushData(data);
                        return;
                    }

                    if (handler != null) {
                        Message message = new Message();
                        message.setData(msg.getData());
                        handler.sendMessageDelayed(message, interval);
                    }
                }
            };
        }

        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("TagData", data);
        message.setData(bundle);
        handler.sendMessage(message);
    }
}
