package com.paisheng.prismsdk.hook;

import android.app.Activity;
import android.util.Log;

import com.tencent.wstt.gt.GTConfig;
import com.tencent.wstt.gt.client.GTRClient;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class FragmentAopList {
    @Around("execution(* android.support.v4.app.Fragment.onAttach(..))")
    public void onAttach(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        joinPoint.proceed();
        long end = System.currentTimeMillis();
        Log.d("aopLog", joinPoint.getThis().getClass().getName() + joinPoint.getThis().hashCode() + " onAttach: " + "-" + start + "-" + end);

        String activityClassName = "";
        String activityHashCode = "";
        String fragmentClassName = "";
        String fragmentHashCode = "";
        Object fragment = joinPoint.getThis();
        Activity activity = null;

        fragmentClassName = fragment.getClass().getName();
        fragmentHashCode = "" + joinPoint.getThis().hashCode();
        activity = ((android.support.v4.app.Fragment) fragment).getActivity();

        if (activity != null) {
            activityClassName  = activity.getClass().getName();
            activityHashCode = "" + activity.hashCode();
        }


        GTRClient.pushData(new StringBuilder()
                .append("Fragment.onAttach")
                .append(GTConfig.separator).append(activityClassName)
                .append(GTConfig.separator).append(activityHashCode)
                .append(GTConfig.separator).append(fragmentClassName)
                .append(GTConfig.separator).append(fragmentHashCode)
                .append(GTConfig.separator).append(start)
                .append(GTConfig.separator).append(end)
                .toString());
    }

    @Around("execution(* android.support.v4.app.Fragment.performCreate(..))")
    public void performCreate(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        joinPoint.proceed();
        long end = System.currentTimeMillis();
        Log.d("aopLog", joinPoint.getThis().getClass().getName() + joinPoint.getThis().hashCode() + " performCreate: " + "-" + start + "-" + end);

        String activityClassName = "";
        String activityHashCode = "";
        String fragmentClassName = "";
        String fragmentHashCode = "";
        Object fragment = joinPoint.getThis();
        Activity activity = null;

        fragmentClassName = fragment.getClass().getName();
        fragmentHashCode = "" + joinPoint.getThis().hashCode();
        activity = ((android.support.v4.app.Fragment) fragment).getActivity();

        if (activity != null) {
            activityClassName  = activity.getClass().getName();
            activityHashCode = "" + activity.hashCode();
        }

        GTRClient.pushData(new StringBuilder()
                .append("Fragment.performCreate")
                .append(GTConfig.separator).append(activityClassName)
                .append(GTConfig.separator).append(activityHashCode)
                .append(GTConfig.separator).append(fragmentClassName)
                .append(GTConfig.separator).append(fragmentHashCode)
                .append(GTConfig.separator).append(start)
                .append(GTConfig.separator).append(end)
                .toString());
    }

    @Around("execution(* android.support.v4.app.Fragment.performCreateView(..))")
    public Object performCreateView(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object o = joinPoint.proceed();
        long end = System.currentTimeMillis();
        Log.d("aopLog", joinPoint.getThis().getClass().getName() + joinPoint.getThis().hashCode() + " performCreateView: " + "-" + start + "-" + end);

        String activityClassName = "";
        String activityHashCode = "";
        String fragmentClassName = "";
        String fragmentHashCode = "";
        Object fragment = joinPoint.getThis();
        Activity activity = null;

        fragmentClassName = fragment.getClass().getName();
        fragmentHashCode = "" + joinPoint.getThis().hashCode();
        activity = ((android.support.v4.app.Fragment) fragment).getActivity();

        if (activity != null) {
            activityClassName  = activity.getClass().getName();
            activityHashCode = "" + activity.hashCode();
        }

        GTRClient.pushData(new StringBuilder()
                .append("Fragment.performCreateView")
                .append(GTConfig.separator).append(activityClassName)
                .append(GTConfig.separator).append(activityHashCode)
                .append(GTConfig.separator).append(fragmentClassName)
                .append(GTConfig.separator).append(fragmentHashCode)
                .append(GTConfig.separator).append(start)
                .append(GTConfig.separator).append(end)
                .toString());
        return o;
    }

    @Around("execution(* android.support.v4.app.Fragment.performActivityCreated(..))")
    public void performActivityCreated(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        joinPoint.proceed();
        long end = System.currentTimeMillis();
        Log.d("aopLog", joinPoint.getThis().getClass().getName() + joinPoint.getThis().hashCode() + " performActivityCreated: " + "-" + start + "-" + end);

        String activityClassName = "";
        String activityHashCode = "";
        String fragmentClassName = "";
        String fragmentHashCode = "";
        Object fragment = joinPoint.getThis();
        Activity activity = null;

        fragmentClassName = fragment.getClass().getName();
        fragmentHashCode = "" + joinPoint.getThis().hashCode();
        activity = ((android.support.v4.app.Fragment) fragment).getActivity();

        if (activity != null) {
            activityClassName  = activity.getClass().getName();
            activityHashCode = "" + activity.hashCode();
        }

        GTRClient.pushData(new StringBuilder()
                .append("Fragment.performActivityCreated")
                .append(GTConfig.separator).append(activityClassName)
                .append(GTConfig.separator).append(activityHashCode)
                .append(GTConfig.separator).append(fragmentClassName)
                .append(GTConfig.separator).append(fragmentHashCode)
                .append(GTConfig.separator).append(start)
                .append(GTConfig.separator).append(end)
                .toString());
    }

    @Around("execution(* android.support.v4.app.Fragment.performStart(..))")
    public void performStart(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        joinPoint.proceed();
        long end = System.currentTimeMillis();
        Log.d("aopLog", joinPoint.getThis().getClass().getName() + joinPoint.getThis().hashCode() + " performStart: " + "-" + start + "-" + end);

        String activityClassName = "";
        String activityHashCode = "";
        String fragmentClassName = "";
        String fragmentHashCode = "";
        Object fragment = joinPoint.getThis();
        Activity activity = null;

        fragmentClassName = fragment.getClass().getName();
        fragmentHashCode = "" + joinPoint.getThis().hashCode();
        activity = ((android.support.v4.app.Fragment) fragment).getActivity();

        if (activity != null) {
            activityClassName  = activity.getClass().getName();
            activityHashCode = "" + activity.hashCode();
        }

        GTRClient.pushData(new StringBuilder()
                .append("Fragment.performStart")
                .append(GTConfig.separator).append(activityClassName)
                .append(GTConfig.separator).append(activityHashCode)
                .append(GTConfig.separator).append(fragmentClassName)
                .append(GTConfig.separator).append(fragmentHashCode)
                .append(GTConfig.separator).append(start)
                .append(GTConfig.separator).append(end)
                .toString());
    }

    @Around("execution(* android.support.v4.app.Fragment.performResume(..))")
    public void performResume(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        joinPoint.proceed();
        long end = System.currentTimeMillis();
        Log.d("aopLog", joinPoint.getThis().getClass().getName() + joinPoint.getThis().hashCode() + " performResume: " + "-" + start + "-" + end);

        String activityClassName = "";
        String activityHashCode = "";
        String fragmentClassName = "";
        String fragmentHashCode = "";
        Object fragment = joinPoint.getThis();
        Activity activity = null;

        fragmentClassName = fragment.getClass().getName();
        fragmentHashCode = "" + joinPoint.getThis().hashCode();
        activity = ((android.support.v4.app.Fragment) fragment).getActivity();

        if (activity != null) {
            activityClassName  = activity.getClass().getName();
            activityHashCode = "" + activity.hashCode();
        }

        GTRClient.pushData(new StringBuilder()
                .append("Fragment.performResume")
                .append(GTConfig.separator).append(activityClassName)
                .append(GTConfig.separator).append(activityHashCode)
                .append(GTConfig.separator).append(fragmentClassName)
                .append(GTConfig.separator).append(fragmentHashCode)
                .append(GTConfig.separator).append(start)
                .append(GTConfig.separator).append(end)
                .toString());
    }

    @Around("execution(* android.support.v4.app.Fragment.performPause(..))")
    public void performPause(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        joinPoint.proceed();
        long end = System.currentTimeMillis();
        Log.d("aopLog", joinPoint.getThis().getClass().getName() + joinPoint.getThis().hashCode() + " performPause: " + "-" + start + "-" + end);

        String activityClassName = "";
        String activityHashCode = "";
        String fragmentClassName = "";
        String fragmentHashCode = "";
        Object fragment = joinPoint.getThis();
        Activity activity = null;

        fragmentClassName = fragment.getClass().getName();
        fragmentHashCode = "" + joinPoint.getThis().hashCode();
        activity = ((android.support.v4.app.Fragment) fragment).getActivity();

        if (activity != null) {
            activityClassName  = activity.getClass().getName();
            activityHashCode = "" + activity.hashCode();
        }

        GTRClient.pushData(new StringBuilder()
                .append("Fragment.performPause")
                .append(GTConfig.separator).append(activityClassName)
                .append(GTConfig.separator).append(activityHashCode)
                .append(GTConfig.separator).append(fragmentClassName)
                .append(GTConfig.separator).append(fragmentHashCode)
                .append(GTConfig.separator).append(start)
                .append(GTConfig.separator).append(end)
                .toString());
    }

    @Around("execution(* android.support.v4.app.Fragment.performStop(..))")
    public void performStop(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        joinPoint.proceed();
        long end = System.currentTimeMillis();
        Log.d("aopLog", joinPoint.getThis().getClass().getName() + joinPoint.getThis().hashCode() + " performStop: " + "-" + start + "-" + end);

        String activityClassName = "";
        String activityHashCode = "";
        String fragmentClassName = "";
        String fragmentHashCode = "";
        Object fragment = joinPoint.getThis();
        Activity activity = null;

        fragmentClassName = fragment.getClass().getName();
        fragmentHashCode = "" + joinPoint.getThis().hashCode();
        activity = ((android.support.v4.app.Fragment) fragment).getActivity();
        if (activity != null) {
            activityClassName  = activity.getClass().getName();
            activityHashCode = "" + activity.hashCode();
        }

        GTRClient.pushData(new StringBuilder()
                .append("Fragment.performStop")
                .append(GTConfig.separator).append(activityClassName)
                .append(GTConfig.separator).append(activityHashCode)
                .append(GTConfig.separator).append(fragmentClassName)
                .append(GTConfig.separator).append(fragmentHashCode)
                .append(GTConfig.separator).append(start)
                .append(GTConfig.separator).append(end)
                .toString());
    }

    @Around("execution(* android.support.v4.app.Fragment.performDestroyView(..))")
    public void performDestroyView(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        joinPoint.proceed();
        long end = System.currentTimeMillis();
        Log.d("aopLog", joinPoint.getThis().getClass().getName() + joinPoint.getThis().hashCode() + " performDestroyView: " + "-" + start + "-" + end);

        String activityClassName = "";
        String activityHashCode = "";
        String fragmentClassName = "";
        String fragmentHashCode = "";
        Object fragment = joinPoint.getThis();
        Activity activity = null;

        fragmentClassName = fragment.getClass().getName();
        fragmentHashCode = "" + joinPoint.getThis().hashCode();
        activity = ((android.support.v4.app.Fragment) fragment).getActivity();

        if (activity != null) {
            activityClassName  = activity.getClass().getName();
            activityHashCode = "" + activity.hashCode();
        }

        GTRClient.pushData(new StringBuilder()
                .append("Fragment.performDestroyView")
                .append(GTConfig.separator).append(activityClassName)
                .append(GTConfig.separator).append(activityHashCode)
                .append(GTConfig.separator).append(fragmentClassName)
                .append(GTConfig.separator).append(fragmentHashCode)
                .append(GTConfig.separator).append(start)
                .append(GTConfig.separator).append(end)
                .toString());
    }

    @Around("execution(* android.support.v4.app.Fragment.performDestroy(..))")
    public void performDestroy(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        joinPoint.proceed();
        long end = System.currentTimeMillis();
        Log.d("aopLog", joinPoint.getThis().getClass().getName() + joinPoint.getThis().hashCode() + " performDestroy: " + "-" + start + "-" + end);

        String activityClassName = "";
        String activityHashCode = "";
        String fragmentClassName = "";
        String fragmentHashCode = "";
        Object fragment = joinPoint.getThis();
        Activity activity = null;

        fragmentClassName = fragment.getClass().getName();
        fragmentHashCode = "" + joinPoint.getThis().hashCode();
        activity = ((android.support.v4.app.Fragment) fragment).getActivity();

        if (activity != null) {
            activityClassName  = activity.getClass().getName();
            activityHashCode = "" + activity.hashCode();
        }

        GTRClient.pushData(new StringBuilder()
                .append("Fragment.performDestroy")
                .append(GTConfig.separator).append(activityClassName)
                .append(GTConfig.separator).append(activityHashCode)
                .append(GTConfig.separator).append(fragmentClassName)
                .append(GTConfig.separator).append(fragmentHashCode)
                .append(GTConfig.separator).append(start)
                .append(GTConfig.separator).append(end)
                .toString());
    }

    @Around("execution(* android.support.v4.app.Fragment.performDetach(..))")
    public void performDetach(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        joinPoint.proceed();
        long end = System.currentTimeMillis();
        Log.d("aopLog", joinPoint.getThis().getClass().getName() + joinPoint.getThis().hashCode() + " performDetach: " + "-" + start + "-" + end);

        String activityClassName = "";
        String activityHashCode = "";
        String fragmentClassName = "";
        String fragmentHashCode = "";
        Object fragment = joinPoint.getThis();
        Activity activity = null;

        fragmentClassName = fragment.getClass().getName();
        fragmentHashCode = "" + joinPoint.getThis().hashCode();
        activity = ((android.support.v4.app.Fragment) fragment).getActivity();

        if (activity != null) {
            activityClassName  = activity.getClass().getName();
            activityHashCode = "" + activity.hashCode();
        }

        GTRClient.pushData(new StringBuilder()
                .append("Fragment.performDetach")
                .append(GTConfig.separator).append(activityClassName)
                .append(GTConfig.separator).append(activityHashCode)
                .append(GTConfig.separator).append(fragmentClassName)
                .append(GTConfig.separator).append(fragmentHashCode)
                .append(GTConfig.separator).append(start)
                .append(GTConfig.separator).append(end)
                .toString());
    }

    @Around("execution(* android.support.v4.app.Fragment.onHiddenChanged(..))")
    public void onHiddenChanged(ProceedingJoinPoint joinPoint) throws Throwable {
        long time = System.currentTimeMillis();
        Object hidden = joinPoint.getArgs()[0];
        joinPoint.proceed();
        Log.d("aopLog", joinPoint.getThis().getClass().getName() + joinPoint.getThis().hashCode() + " onHiddenChanged: " + "-" + time + "-" + hidden);

        String activityClassName = "";
        String activityHashCode = "";
        String fragmentClassName = "";
        String fragmentHashCode = "";
        Object fragment = joinPoint.getThis();
        Activity activity = null;

        fragmentClassName = fragment.getClass().getName();
        fragmentHashCode = "" + joinPoint.getThis().hashCode();
        activity = ((android.support.v4.app.Fragment) fragment).getActivity();

        if (activity != null) {
            activityClassName  = activity.getClass().getName();
            activityHashCode = "" + activity.hashCode();
        }

        GTRClient.pushData(new StringBuilder()
                .append("Fragment.onHiddenChanged")
                .append(GTConfig.separator).append(activityClassName)
                .append(GTConfig.separator).append(activityHashCode)
                .append(GTConfig.separator).append(fragmentClassName)
                .append(GTConfig.separator).append(fragmentHashCode)
                .append(GTConfig.separator).append(time)
                .append(GTConfig.separator).append(hidden)
                .toString());
    }

    @Around("execution(* android.support.v4.app.Fragment.setUserVisibleHint(..))")
    public void setUserVisibleHint(ProceedingJoinPoint joinPoint) throws Throwable {
        long time = System.currentTimeMillis();
        Object isVisible = joinPoint.getArgs()[0];
        joinPoint.proceed();
        Log.d("aopLog", joinPoint.getThis().getClass().getName() + joinPoint.getThis().hashCode() + " setUserVisibleHint: " + "-" + time + "-" + isVisible);

        String activityClassName = "";
        String activityHashCode = "";
        String fragmentClassName = "";
        String fragmentHashCode = "";
        Object fragment = joinPoint.getThis();
        Activity activity = null;

        fragmentClassName = fragment.getClass().getName();
        fragmentHashCode = "" + joinPoint.getThis().hashCode();
        activity = ((android.support.v4.app.Fragment) fragment).getActivity();

        if (activity != null) {
            activityClassName  = activity.getClass().getName();
            activityHashCode = "" + activity.hashCode();
        }

        GTRClient.pushData(new StringBuilder()
                .append("Fragment.setUserVisibleHint")
                .append(GTConfig.separator).append(activityClassName)
                .append(GTConfig.separator).append(activityHashCode)
                .append(GTConfig.separator).append(fragmentClassName)
                .append(GTConfig.separator).append(fragmentHashCode)
                .append(GTConfig.separator).append(time)
                .append(GTConfig.separator).append(isVisible)
                .toString());
    }
}
