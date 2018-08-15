package com.paisheng.prismsdk;

import android.util.Log;

import com.paisheng.prismsdk.monitor.TagMonitor;
import com.tencent.wstt.gt.GTConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class TagAspect {
    @Around("execution(@com.paisheng.prism.anotation.SpyVoid * *(..))")
    public void spyvoid(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        joinPoint.proceed();
        long end = System.currentTimeMillis();

        String className;
        String hashcode;

        if (joinPoint.getThis() == null) {
            className = joinPoint.getSourceLocation().getFileName().replace(".java","");
            hashcode = "00000000";
        } else {
            className = joinPoint.getThis().getClass().getName();
            hashcode = joinPoint.getThis().hashCode() + "";
        }

        String content = "tagCollect" +
                GTConfig.separator + start +
                GTConfig.separator + end +
                GTConfig.separator + joinPoint.getSignature().getName() +
                GTConfig.separator + className +
                GTConfig.separator + hashcode;

        TagMonitor.start(content);
    }

    @Around("execution(@com.paisheng.prism.anotation.SpyObject * *(..))")
    public Object spyobject(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object o = joinPoint.proceed();
        long end = System.currentTimeMillis();

        String content = "tagCollect" +
                GTConfig.separator + start +
                GTConfig.separator + end +
                GTConfig.separator + joinPoint.getSignature().getName() +
                GTConfig.separator + joinPoint.getThis().getClass().getName() +
                GTConfig.separator + joinPoint.getThis().hashCode();

        TagMonitor.start(content);
        return o;
    }
}
