package com.paisheng.prismsdk.view;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.paisheng.prismsdk.R;

/**
 * <br> ClassName:   PrismNotification
 * <br> Description: Pris通知
 * <br>
 * <br> Author:      yexiaochuan
 * <br> Date:        2018/6/8 17:57
 */
public class PrismNotification {
    private final Context mContext;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private final String CHANNEL_ID = "Prism";

    /**
     *<br> Description: 初始化
     *<br> Author:      yexiaochuan
     *<br> Date:        2017/6/8 17:58
     * @param context
     *                  上下文
     */
    public PrismNotification(Context context) {
        mContext = context;
    }

    public void onNotificationInit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Prism性能报告";
            String description = "Prism性能报告通知";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(mContext.getApplicationContext(),CHANNEL_ID);
        String appName = mContext.getString(mContext.getApplicationInfo().labelRes);
        mBuilder.setContentTitle("Prism 性能监控");
        int icon = mContext.getApplicationInfo().icon;
        mBuilder.setSmallIcon(icon);
        mBuilder.setContentText("点击上传Prism性能数据");
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent intent = new Intent(mContext,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext.getApplicationContext(),
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        Notification noti = mBuilder.build();
        noti.flags = Notification.FLAG_NO_CLEAR;
        mNotifyManager.notify(0, noti);
    }
}
