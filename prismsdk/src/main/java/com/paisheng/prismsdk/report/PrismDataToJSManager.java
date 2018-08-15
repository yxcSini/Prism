package com.paisheng.prismsdk.report;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.tencent.wstt.gt.datatool.GTRAnalysis;
import com.tencent.wstt.gt.datatool.util.FileUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PrismDataToJSManager {
    private static final int BUFFER_SIZE = 1 << 20;
    private static GTRAnalysis gtrAnalysis = null;

    /**
     *<br> Description: 解析成js文件
     *<br> Author:      yexiaochuan
     *<br> Date:        2018/5/30 15:41
     * @param fileName    应用包名
     * @return
     * @throws Exception
     */
    public static Boolean toAnalysis(String fileName) throws Exception {
        long startTime0 = System.currentTimeMillis();
        File dataDir = new File(fileName);
        if (!dataDir.exists()) {
            throw new Exception("file is not exists :" + dataDir.getAbsolutePath());
        }
        if (dataDir.getAbsolutePath().endsWith(".txt")) {
            // 解析数据
            gtrAnalysis = getGTRAnalysis(dataDir.getAbsolutePath());
            long startTime = System.currentTimeMillis();
            Log.i("adam", " 序列化数据时间 =" + (startTime - startTime0) + "ms");
        } else {
            throw new Exception("file is not txt :" + dataDir.getAbsolutePath());
        }

        String srcDes = PrismSDCardPath.PRISM_REPORT_PATH +
                "/" + PrismSDCardPath.getSaveDateMs() + "/data.js";
        long startTime1 = System.currentTimeMillis();
        toDataJs(srcDes);
        long startTime2 = System.currentTimeMillis();
        Log.i("adam", "写入数据时间 =" + (startTime2 - startTime1) + "ms");
        FileUtil.deleteFile(dataDir);
        FileUtil.copyFile(srcDes,PrismSDCardPath.PRISM_RESULT_DATA_PATH,true);
        return true;
    }

    public static GTRAnalysis getGTRAnalysis(String dataFilePath) throws Exception {
        File file = new File(dataFilePath);
        if (!file.exists()) {
            throw new Exception("dataFilePath is not exists:" + dataFilePath);
        } else {
            GTRAnalysis gtrAnalysis = new GTRAnalysis();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));

            for (String dataLine = bufferedReader.readLine(); dataLine != null; dataLine = bufferedReader.readLine()) {
                try {
                    if (dataLine.length() > 0) {
                        gtrAnalysis.distribute(dataLine.split("\\^"));
                    }
                } catch (Exception e) {
                    System.out.println("ErrorData:" + dataLine);
                    e.printStackTrace();
                }
            }

            return gtrAnalysis;
        }
    }

    private static void appendJSList(BufferedWriter writer, String varName, ArrayList list) throws IOException {
        appendVariableName(writer, varName);
        appendList(writer, list);
        writer.write(";\n");
    }

    private static void appendList(BufferedWriter writer, ArrayList list) throws IOException {
        writer.write("[");

        if (!list.isEmpty()) {
            writer.write(JSON.toJSONString(list.get(0)));

            for (int i = 1; i < list.size(); i++) {
                writer.write(",");
                writer.write(JSON.toJSONString(list.get(i)));
            }

            list.clear();
        }

        writer.write("]");
    }

    private static void appendJSObject(BufferedWriter writer, String varName, Object obj) throws IOException {
        appendVariableName(writer, varName);
        writer.write(JSON.toJSONString(obj));
        writer.write(";\n");
    }

    private static void appendRawString(BufferedWriter writer, String data) throws IOException {
        writer.write(data);
        writer.write(";\n");
    }

    private static void appendVariableName(BufferedWriter writer, String varName) throws IOException {
        writer.write("var ");
        writer.write(varName);
        writer.write("=");
    }

    private static void toDataJs(File des) {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(des), BUFFER_SIZE);
            writer.write("\n\n\n");

            appendJSObject(writer, "appInfo", gtrAnalysis.getAppInfo());
            appendJSObject(writer, "deviceInfo", gtrAnalysis.getDeviceInfo());
            appendJSList(writer, "frames", gtrAnalysis.getFrames());
            appendJSList(writer, "normalInfos", gtrAnalysis.getNormalInfos());
            appendJSList(writer, "gtrThreadInfos", gtrAnalysis.getGtrThreadInfos());

            appendJSList(writer, "frontBackStates", gtrAnalysis.getFrontBackStates());
            appendJSObject(writer, "frontBackInfo", gtrAnalysis.getFrontBackInfo());
            appendJSList(writer, "lowSMInfos", gtrAnalysis.getLowSMInfos());
            appendJSList(writer, "allBlockInfos", gtrAnalysis.getAllBlockInfos());
            appendJSList(writer, "bigBlockIDs", gtrAnalysis.getBigBlockIDs());

            appendJSList(writer, "pageLoadInfos", gtrAnalysis.getPageLoadInfos());
            appendJSList(writer, "overActivityInfos", gtrAnalysis.getOverActivityInfos());
            appendJSList(writer, "overViewDraws", gtrAnalysis.getOverViewDraws());
            appendJSList(writer, "operationInfos", gtrAnalysis.getOperationInfos());
            appendJSList(writer, "viewBuildInfos", gtrAnalysis.getViewBuildInfos());

            appendJSList(writer, "overViewBuilds", gtrAnalysis.getOverViewBuilds());
            appendJSList(writer, "fragmentInfos", gtrAnalysis.getFragmentInfos());
            appendJSList(writer, "overFragments", gtrAnalysis.getOverFragments());
            appendJSList(writer, "allGCInfos", gtrAnalysis.getAllGCInfos());
            appendJSList(writer, "explicitGCs", gtrAnalysis.getExplicitGCs());

            appendJSList(writer, "diskIOInfos", gtrAnalysis.getDiskIOInfos());
            appendJSList(writer, "fileActionInfos", gtrAnalysis.getFileActionInfos());
            appendJSList(writer, "fileActionInfosInMainThread", gtrAnalysis.getFileActionInfosInMainThread());
            appendJSList(writer, "dbActionInfos", gtrAnalysis.getDbActionInfos());

            appendJSList(writer, "dbActionInfosInMainThread", gtrAnalysis.getDbActionInfosInMainThread());
            appendJSList(writer, "logInfos", gtrAnalysis.getLogInfos());
            appendJSList(writer, "flagInfo", gtrAnalysis.getFlagList());
            appendJSList(writer, "tagInfo", gtrAnalysis.getUserTagInfos());

            writer.write("\n\n\n");

            String data = "//基础性能\nvar tableBaseData_base= frontBackInfo;\n" +
                    "//卡顿检测\nvar tableBaseData_lowSM = lowSMInfos;\n" +
                    "var tableBaseData_bigBlock = bigBlockIDs;\n" +
                    "//页面测速\nvar tableBaseData_overActivity = overActivityInfos;\nvar tableBaseData_allPage = pageLoadInfos;\n" +
                    "//Fragment测速\nvar tableBaseData_overFragment = overFragments;\nvar tableBaseData_allFragment = fragmentInfos;\n" +
                    "//布局检测\nvar tableBaseData_overViewBuild = overViewBuilds;\nvar tableBaseData_overViewDraw = overViewDraws;\n" +
                    "//GC检测\nvar tableBaseData_explicitGC = explicitGCs;\n" +
                    "//IO检测\nvar tableBaseData_fileActionInMainThread = fileActionInfosInMainThread;\n" +
                    "var tableBaseData_dbActionInMainThread = dbActionInfosInMainThread;\nvar tableBaseData_db = dbActionInfos;\n" +
                    "//关键日志\nvar tableBaseData_logcat = logInfos;\n";
            appendRawString(writer, data);

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer == null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void toDataJs(String desPath) throws IOException {
        File des = new File(desPath);
        if (toCreateFileDir(des)) {
            toDataJs(des);
        } else {
            throw new IOException("ErrorData: 文件创建失败，请开启系统读写权限后重试");
        }
    }

    private static boolean toCreateFileDir(File des) {
        try {
            if (des.exists()) {
                des.delete();
            }
            des.getParentFile().mkdirs();
            des.createNewFile();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
