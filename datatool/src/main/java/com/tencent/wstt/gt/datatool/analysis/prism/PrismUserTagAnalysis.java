package com.tencent.wstt.gt.datatool.analysis.prism;

import com.tencent.wstt.gt.datatool.obj.UserTagInfo;

import java.util.ArrayList;

public class PrismUserTagAnalysis {
    private ArrayList<UserTagInfo> userTagInfos;

    public PrismUserTagAnalysis(ArrayList<UserTagInfo> lists) {
        userTagInfos = lists;
    }

    public void collectTagInfo(String className,String methodName,String hashCode,String start,String end) {
        UserTagInfo userTagInfo = new UserTagInfo();
        userTagInfo.className = className;
        userTagInfo.methodName = methodName;
        userTagInfo.hashcode = hashCode;
        userTagInfo.start = start;
        userTagInfo.end = end;
        userTagInfos.add(userTagInfo);
    }
}
