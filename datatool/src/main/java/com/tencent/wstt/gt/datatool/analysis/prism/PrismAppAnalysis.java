package com.tencent.wstt.gt.datatool.analysis.prism;

import com.tencent.wstt.gt.datatool.GTRAnalysis;
import com.tencent.wstt.gt.datatool.analysis.AppAnalysis;
import com.tencent.wstt.gt.datatool.obj.AppInfo;

/**
 * <br> ClassName:   PrismAppAnalysis
 * <br> Description: Prism只记录首次的appInfo数据
 * <br>
 * <br> Author:      yexiaochuan
 * <br> Date:        2018/6/19 11:40
 */
public class PrismAppAnalysis extends AppAnalysis {
    private boolean isSet;

    public PrismAppAnalysis(GTRAnalysis gtrAnalysis, AppInfo appInfo) {
        super(gtrAnalysis, appInfo);
    }

    @Override
    public void onCollectAppInfo(String packageName, String appName, String versionName, int versionCode, int gtrVersionCode, long startTestTime, int mainThreadId) {
        if (!isSet) {
            isSet = true;
            super.onCollectAppInfo(packageName, appName, versionName, versionCode, gtrVersionCode, startTestTime, mainThreadId);
        }
    }
}
