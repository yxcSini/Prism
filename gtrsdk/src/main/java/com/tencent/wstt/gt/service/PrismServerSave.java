package com.tencent.wstt.gt.service;

import com.tencent.wstt.gt.collector.GTRCollector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * <br> ClassName:   PrismServerSave
 * <br> Description: Prism数据源保存规则
 * <br>
 * <br> Author:      yexiaochuan
 * <br> Date:        2018/6/6 14:01
 */
public class PrismServerSave {
    //bufferedWriter缓存列表，防止一直做文件打开操作
    private static HashMap<String,BufferedWriter> bufferedWriters = new HashMap<>();

    /**
     * 写入数据
     * @param packageName
     * @param startTestTime
     * @param pid
     * @param data
     * @throws Exception
     */
    public static void saveData(String packageName,long startTestTime,int pid,String data) throws Exception {
        if (packageName==null){
            throw new Exception("packageName is null");
        }
        BufferedWriter bufferedWriter = getBufferedWriter(packageName,startTestTime,pid);
        if (bufferedWriter == null) {
            return;
        }
        try {
            bufferedWriter.write("\n"+data);
            bufferedWriter.flush();
        }catch (Exception e){
            //如果写失败，可能是由于文件被删除导致的，我们重新初始化bufferedWriter
            putBufferedWriter(packageName,startTestTime,pid);
            BufferedWriter bufferedWriter1 = getBufferedWriter(packageName,startTestTime,pid);
            if (bufferedWriter1 != null) {
                bufferedWriter1.write("\n"+data);
                bufferedWriter1.flush();
            }
        }
    }


    /**
     * 初始化BufferedWriter
     * @param packageName
     * @param startTestTime
     * @param pid
     * @return
     * @throws IOException
     */
    public static BufferedWriter getBufferedWriter(String packageName, long startTestTime, int pid) throws IOException {
        String key = packageName+startTestTime+pid;
        if (bufferedWriters.get(key) == null) {
           putBufferedWriter(packageName,startTestTime,pid);
        }

        return bufferedWriters.get(key);
    }

    public static void putBufferedWriter(String packageName, long startTestTime, int pid) throws IOException {
        String key = packageName+startTestTime+pid;
        File dataFile = new File(getSaveFilePath(packageName));
        if (dataFile.exists()) {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dataFile, true),"utf-8"));
            bufferedWriters.put(key,bufferedWriter);
        }
    }

    public static void initBufferedWriter(String packageName) throws IOException {
        String key = packageName;
        //创建文件
        File dataFile = new File(getSaveFilePath(packageName));
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            dataFile.createNewFile();
        }
    }

    public static String getSaveFilePath(String packageName){
        String dataFilePath = PrismConfig.gtrDirPath+packageName+".txt";
        return dataFilePath;
    }

    public static String getSaveFileDir(){
        String dataFilePath = PrismConfig.gtrDirPath+GTRCollector.applicationContext+".txt";
        return dataFilePath;
    }
}
