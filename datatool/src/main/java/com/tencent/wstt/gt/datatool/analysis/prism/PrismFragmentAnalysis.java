package com.tencent.wstt.gt.datatool.analysis.prism;

import com.alibaba.fastjson.JSON;
import com.tencent.wstt.gt.datatool.GTRAnalysis;
import com.tencent.wstt.gt.datatool.obj.FragmentInfo;
import com.tencent.wstt.gt.datatool.obj.FragmentLifecycleMethod;

import java.util.ArrayList;
import java.util.HashMap;

public class PrismFragmentAnalysis {

    ArrayList<FragmentInfo> fragmentInfos;
    ArrayList<Integer> overFragments;

    public PrismFragmentAnalysis(GTRAnalysis gtrAnalysis, ArrayList<FragmentInfo> fragmentInfos, ArrayList<Integer> overFragments) {
        this.fragmentInfos = fragmentInfos;
        this.overFragments = overFragments;
    }


    /**
     * 冷启动：从onAttach函数开始
     * 热启动：从onCreateView或onStart函数开始
     * 1.ViewPager的回退栈：会引起 onCreateView - onDestroyView 的生命周期
     * 2.Activity的回退栈：会引起 onStart - onStop 的生命周期
     */

    private final Object lock = new Object();
    private int startNumber = 0;//启动页面的数量，用于赋值给startOrderId
    //onAttach列表：加入列表中
    private HashMap<String,FragmentInfo> fragmentMap = new HashMap<>();
    //onCreateView列表：
    private HashMap<String,FragmentInfo> onCreateViewList = new HashMap<>();
    //fragment列表：
    // 1.onStart，加入列表中
    // 2.onStop,从表中移除,写入文件
    private HashMap<String,FragmentInfo> fragmentInfoList = new HashMap<>();


    private final Object stateLock = new Object();
    //Fragment状态列表：
    // 1.onAttach时加入
    // 2.onDetach时移除列表
    private ArrayList<FragmentState> fragmentStateList = new ArrayList<>();


    public void onFragment_onAttach(String activityClassName, String activityHashCode, String fragmentClassName,
                                    String fragmentHashCode, long start, long end) {
        synchronized (lock) {
            FragmentInfo fragmentInfo = new FragmentInfo(activityClassName, activityHashCode, fragmentClassName, fragmentHashCode);
            FragmentInfo current = fragmentMap.get(fragmentHashCode);
            if (current != null) {
                current.addAttachInfo(start,end);
                return;
            }

            fragmentInfo.addAttachInfo(start, end);
            fragmentMap.put(fragmentHashCode,fragmentInfo);
        }

    }


    public void onFragment_performCreate(String activityClassName, String activityHashCode, String fragmentClassName,
                                         String fragmentHashCode, long start, long end) {
        synchronized (lock) {
            FragmentInfo current = fragmentMap.get(fragmentHashCode);
            if (current != null && current.activityClassName.equals(activityClassName)
                    && current.activityHashCode.equals(activityHashCode)
                    && current.fragmentClassName.equals(fragmentClassName)) {
                current.addCreateInfo(start, end);
            }
        }

    }


    public void onFragment_performCreateView(String activityClassName, String activityHashCode,
                                             String fragmentClassName, String fragmentHashCode, long start, long end) {
        synchronized (lock) {
            FragmentInfo current = fragmentMap.get(fragmentHashCode);
            if (current != null && current.activityClassName.equals(activityClassName)
                    && current.activityHashCode.equals(activityHashCode)
                    && current.fragmentClassName.equals(fragmentClassName)) {
                current.addCreateViewInfo(start, end);
            } else {
                current = new FragmentInfo(activityClassName, activityHashCode, fragmentClassName, fragmentHashCode);
                current.addCreateViewInfo(start, end);
                fragmentMap.put(fragmentHashCode,current);
            }
        }
    }


    public void onFragment_performActivityCreated(String activityClassName, String activityHashCode,
                                                  String fragmentClassName, String fragmentHashCode, long start, long end) {
        synchronized (lock) {
            FragmentInfo current = fragmentMap.get(fragmentHashCode);
            if (current != null && current.activityClassName.equals(activityClassName)
                    && current.activityHashCode.equals(activityHashCode)
                    && current.fragmentClassName.equals(fragmentClassName)) {
                current.addActivityCreatedInfo(start, end);
            }
        }

    }


    public void onFragment_performStart(String activityClassName, String activityHashCode, String fragmentClassName,
                                        String fragmentHashCode, long start, long end) {
        synchronized (lock) {
            FragmentInfo current = fragmentMap.get(fragmentHashCode);
            if (current != null && current.activityClassName.equals(activityClassName)
                    && current.activityHashCode.equals(activityHashCode)
                    && current.fragmentClassName.equals(fragmentClassName)) {
                current.addStartInfo(start, end);
            } else {
                current = new FragmentInfo(activityClassName, activityHashCode, fragmentClassName, fragmentHashCode);
                current.addStartInfo(start, end);
                fragmentMap.put(fragmentHashCode,current);
            }
        }

    }


    public void onFragment_performResume(String activityClassName, String activityHashCode, String fragmentClassName,
                                         String fragmentHashCode, long start, long end) {
        synchronized (lock) {
            for (FragmentInfo fragmentInfo : fragmentMap.values()) {
                if (fragmentInfo.activityClassName.equals(activityClassName)
                        && fragmentInfo.activityHashCode.equals(activityHashCode)
                        && fragmentInfo.fragmentClassName.equals(fragmentClassName)
                        && fragmentInfo.fragmentHashCode.equals(fragmentHashCode)) {

                    //是否有onResume
                    boolean isFirst = true;
                    for (FragmentLifecycleMethod method :fragmentInfo.fragmentLifecycleMethodList) {
                        if (method.methodName.equals(FragmentLifecycleMethod.ONRESUME)) {
                            isFirst = false;
                        }
                    }

                    for (FragmentLifecycleMethod method :fragmentInfo.fragmentLifecycleMethodList) {
                        if (method.methodName.equals(FragmentLifecycleMethod.ONSTART)) {
                            fragmentInfo.addResumeInfo(start, end);
                        }
                    }

                    if (isFirst) {
                        startNumber++;
                        fragmentInfo.startOrderId = startNumber;
                        fragmentInfos.add(fragmentInfo);
                    }

                    //记录超时
                    long loadTime = getFragmentStartFinishTime(fragmentInfo) - getFragmentStartTime(fragmentInfo);
                    if (loadTime > 300) {
                        boolean isExists = false;
                        for (int h = 0; h < overFragments.size(); h++) {
                            FragmentInfo overFragment = fragmentInfos.get(overFragments.get(h));
                            if (overFragment.fragmentClassName.equals(fragmentInfo.fragmentClassName)) {
                                isExists = true;
                                if (loadTime > getFragmentStartFinishTime(overFragment) - getFragmentStartTime(overFragment)) {
                                    overFragments.remove(h);
                                    overFragments.add(fragmentInfos.size() - 1);
                                }
                                break;
                            }
                        }
                        if (!isExists) {
                            overFragments.add(fragmentInfos.size() - 1);
                        }
                    }

                    break;
                }
            }
        }

        synchronized (stateLock) {
            FragmentState fragmentToShow = null;
            for (FragmentState fragmentState : fragmentStateList) {
                if (fragmentState.fragmentClassName.equals(fragmentClassName)
                        && fragmentState.fragmentHashCode.equals(fragmentHashCode)) {
                    fragmentToShow = fragmentState;
                    break;
                }
            }
            if (fragmentToShow == null) {
                fragmentToShow = new FragmentState(fragmentClassName, fragmentHashCode);
                fragmentStateList.add(fragmentToShow);
            }
            fragmentToShow.hasResumed = true;
            if (fragmentToShow.isShow()) {
                fragmentToShow.showStart = System.currentTimeMillis();
            }
        }

    }


    public void onFragment_performPause(String activityClassName, String activityHashCode, String fragmentClassName,
                                        String fragmentHashCode, long start, long end) {
        synchronized (lock) {
            for (FragmentInfo fragmentInfo : fragmentMap.values()) {
                if (fragmentInfo.activityClassName.equals(activityClassName)
                        && fragmentInfo.activityHashCode.equals(activityHashCode)
                        && fragmentInfo.fragmentClassName.equals(fragmentClassName)
                        && fragmentInfo.fragmentHashCode.equals(fragmentHashCode)) {
                    for (FragmentLifecycleMethod method :fragmentInfo.fragmentLifecycleMethodList) {
                        if (method.methodName.equals(FragmentLifecycleMethod.ONSTART)) {
                            fragmentInfo.addPauseInfo(start, end);
                        }
                    }
                    break;
                }
            }
        }

        synchronized (stateLock) {
            FragmentState fragmentToShow = null;
            for (FragmentState fragmentState : fragmentStateList) {
                if (fragmentState.fragmentClassName.equals(fragmentClassName)
                        && fragmentState.fragmentHashCode.equals(fragmentHashCode)) {
                    fragmentToShow = fragmentState;
                    break;
                }
            }
            if (fragmentToShow == null) {
                fragmentToShow = new FragmentState(fragmentClassName, fragmentHashCode);
                fragmentStateList.add(fragmentToShow);
            }
            if (fragmentToShow.isShow() && fragmentToShow.showStart != 0) {//匹配fragment的显示时间
                addFragmentVisibleInfo(fragmentClassName, fragmentHashCode, fragmentToShow.showStart, System.currentTimeMillis());
            }
            fragmentToShow.hasResumed = false;
            fragmentToShow.showStart = 0;

        }

    }


    public void onFragment_performStop(String activityClassName, String activityHashCode, String fragmentClassName,
                                       String fragmentHashCode, long start, long end) {
        synchronized (lock) {
            for (FragmentInfo fragmentInfo : fragmentMap.values()) {
                if (fragmentInfo.activityClassName.equals(activityClassName)
                        && fragmentInfo.activityHashCode.equals(activityHashCode)
                        && fragmentInfo.fragmentClassName.equals(fragmentClassName)
                        && fragmentInfo.fragmentHashCode.equals(fragmentHashCode)) {
                    for (FragmentLifecycleMethod method :fragmentInfo.fragmentLifecycleMethodList) {
                        if (method.methodName.equals(FragmentLifecycleMethod.ONSTART)) {
                            fragmentInfo.addStopInfo(start, end);
                        }
                    }
                    break;
                }
            }
        }

    }


    public void onFragment_performDestroyView(String activityClassName, String activityHashCode,
                                              String fragmentClassName, String fragmentHashCode, long start, long end) {
        //do nothing

    }


    public void onFragment_performDestroy(String activityClassName, String activityHashCode, String fragmentClassName,
                                          String fragmentHashCode, long start, long end) {
        //do nothing
    }


    public void onFragment_performDetach(String activityClassName, String activityHashCode, String fragmentClassName,
                                         String fragmentHashCode, long start, long end) {
        //do nothing
        synchronized (stateLock) {
            for (FragmentState fragmentState : fragmentStateList) {
                if (fragmentState.fragmentClassName.equals(fragmentClassName)
                        && fragmentState.fragmentHashCode.equals(fragmentHashCode)) {
                    fragmentStateList.remove(fragmentState);
                    break;
                }
            }
        }

    }


    public void onFragment_onHiddenChanged(String activityClassName, String activityHashCode, String fragmentClassName,
                                           String fragmentHashCode, long time, boolean hidden) {
        synchronized (stateLock) {
            FragmentState fragmentToShow = null;
            for (FragmentState fragmentState : fragmentStateList) {
                if (fragmentState.fragmentClassName.equals(fragmentClassName)
                        && fragmentState.fragmentHashCode.equals(fragmentHashCode)) {
                    fragmentToShow = fragmentState;
                    break;
                }
            }
            if (fragmentToShow == null) {
                fragmentToShow = new FragmentState(fragmentClassName, fragmentHashCode);
                fragmentStateList.add(fragmentToShow);
            }
            if (!hidden) {
                fragmentToShow.hasShow = true;
                if (fragmentToShow.isShow()) {
                    fragmentToShow.showStart = System.currentTimeMillis();
                }
            } else {
                if (fragmentToShow.isShow() && fragmentToShow.showStart != 0) {//匹配fragment的显示时间
                    addFragmentVisibleInfo(fragmentClassName, fragmentHashCode, fragmentToShow.showStart, System.currentTimeMillis());
                }
                fragmentToShow.hasShow = false;
                fragmentToShow.showStart = 0;
            }
        }

    }


    public void onFragment_setUserVisibleHint(String activityClassName, String activityHashCode,
                                              String fragmentClassName, String fragmentHashCode, long time, boolean isVisibleToUser) {
        synchronized (stateLock) {
            FragmentState fragmentToShow = null;
            for (FragmentState fragmentState : fragmentStateList) {
                if (fragmentState.fragmentClassName.equals(fragmentClassName)
                        && fragmentState.fragmentHashCode.equals(fragmentHashCode)) {
                    fragmentToShow = fragmentState;
                    break;
                }
            }
            if (fragmentToShow == null) {
                fragmentToShow = new FragmentState(fragmentClassName, fragmentHashCode);
                fragmentStateList.add(fragmentToShow);
            }
            if (isVisibleToUser) {
                fragmentToShow.hasVisible = true;
                if (fragmentToShow.isShow()) {
                    fragmentToShow.showStart = System.currentTimeMillis();
                }
            } else {
                if (fragmentToShow.isShow() && fragmentToShow.showStart != 0) {//匹配fragment的显示时间
                    addFragmentVisibleInfo(fragmentClassName, fragmentHashCode, fragmentToShow.showStart, System.currentTimeMillis());
                }
                fragmentToShow.hasVisible = false;
                fragmentToShow.showStart = 0;
            }
        }
    }


    private void addFragmentVisibleInfo(String fragmentClassName, String fragmentHashCode, long showStart, long shouEnd) {
        synchronized (lock) {
            for (FragmentInfo fragmentInfo : fragmentMap.values()) {
                if (fragmentInfo.fragmentClassName.equals(fragmentClassName)
                        && fragmentInfo.fragmentHashCode.equals(fragmentHashCode)) {
                    for (FragmentLifecycleMethod method :fragmentInfo.fragmentLifecycleMethodList) {
                        if (method.methodName.equals(FragmentLifecycleMethod.ONSTART)) {
                            fragmentInfo.addVisibleInfo(showStart, shouEnd);
                        }
                    }
                    break;
                }
            }
        }
    }


    private static class FragmentState {

        public String fragmentClassName = "";
        public String fragmentHashCode = "";

        public boolean hasResumed = false; //此值由Fragment.onResume 和 Fragment.onPause 函数决定。
        public boolean hasShow = true;//此值由onHiddenChanged函数决定
        public boolean hasVisible = true; //此值由setUserVisibleHint函数决定

        public long showStart = 0;

        public FragmentState(String fragmentClassName, String fragmentHashCode) {
            this.fragmentClassName = fragmentClassName;
            this.fragmentHashCode = fragmentHashCode;
        }


        public String getState() {
            if (hasResumed && hasShow && hasVisible) {
                return fragmentClassName + "," + fragmentHashCode + "," + "显示";
            } else {
                return fragmentClassName + "," + fragmentHashCode + "," + "隐藏";
            }
        }

        public boolean isShow() {
            if (hasResumed && hasShow && hasVisible) {
                return true;
            } else {
                return false;
            }
        }
    }


    private static long getFragmentStartTime(FragmentInfo fragmentInfo) {
        long start = 0;
        for (FragmentLifecycleMethod lifecycleMethod : fragmentInfo.fragmentLifecycleMethodList) {
            if (start == 0 || lifecycleMethod.methodStartTime < start) {
                start = lifecycleMethod.methodStartTime;
            }
        }
        return start;
    }

    private static long getFragmentStartFinishTime(FragmentInfo fragmentInfo) {
        long startFinish = 0;
        for (FragmentLifecycleMethod lifecycleMethod : fragmentInfo.fragmentLifecycleMethodList) {
            if (startFinish == 0 || lifecycleMethod.methodName.equals(FragmentLifecycleMethod.ONRESUME)) {
                startFinish = lifecycleMethod.methodEndTime;
                return startFinish;
            }
        }
        return startFinish;
    }


}
