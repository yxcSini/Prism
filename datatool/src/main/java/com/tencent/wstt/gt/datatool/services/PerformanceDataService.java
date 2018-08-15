package com.tencent.wstt.gt.datatool.services;


import com.alibaba.fastjson.JSON;
import com.tencent.wstt.gt.datatool.GTRAnalysis;
import com.tencent.wstt.gt.datatool.util.FileUtil;

import java.io.*;
import java.util.ArrayList;

/**
 * Description: 对外提供接口服务。
 * Author: suwenlong
 * Date: 2018/7/17
 **/
public class PerformanceDataService {

    public static final String DATA_DIR = "data";
    public static final String SRC_DIR = "src";
    public static final String RESULT_DIR = "result";
    public static final String DATA_JS_DIR = "/data/data.js";


    /**
     * Description: 生成data.js文件。
     * txt 文件放在baseDir下面；
     * Author: suwenlong
     * Date: 2018/7/17
     **/
    public static void produceDataJs(String baseDir, String fileName) {

        String dataDirPath = baseDir;
        String resultDirPath = new File(baseDir, RESULT_DIR).getAbsolutePath();
        String resultDataFilePath = resultDirPath + DATA_JS_DIR;

        File dataFile = new File(baseDir,fileName);
        if (!dataFile.exists()) {
            System.out.println("Produce-Data-Js-Error| dataFile or dataDir not exist. fileName:" + dataDirPath + fileName);
            return;
        }
        try {
            GTRAnalysis gtrAnalysis = getGTRAnalysis(dataFile.getAbsolutePath());

            File dataJSFile = createNewDataJS(resultDataFilePath);
            toDataJs(dataJSFile, gtrAnalysis);

        } catch (Exception e) {
            System.out.println("Data-JS-Create-Exception | filePath:" + baseDir + fileName);
        }
        System.out.println("Data-JS-Create-Success | filePath: " + baseDir + fileName);
    }

    /**
     * Description: 检查data.js文件是否存在， 存在则删除； 否则，创建新文件；
     * Author: suwenlong
     * Date: 2018/7/20
    **/
    private static File createNewDataJS(String resultDataFilePath) throws IOException {
        File resultDataFile = new File(resultDataFilePath);
        if (resultDataFile.exists()) {
            resultDataFile.delete();
        }
        resultDataFile.getParentFile().mkdirs();
        resultDataFile.createNewFile();
        return resultDataFile;
    }


    public static void toDataJs(File des, GTRAnalysis gtrAnalysis) {
        BufferedWriter writer = null;
//        int BUFFER_SIZE = 1 << 20;
        try {
            writer = new BufferedWriter(new FileWriter(des));
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

    private static void appendJSObject(BufferedWriter writer, String varName, Object obj) throws IOException {
        appendVariableName(writer, varName);
        writer.write(JSON.toJSONString(obj));
        writer.write(";\n");
    }

    private static void appendVariableName(BufferedWriter writer, String varName) throws IOException {
        writer.write("var ");
        writer.write(varName);
        writer.write("=");
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

    private static void appendRawString(BufferedWriter writer, String data) throws IOException {
        writer.write(data);
        writer.write(";\n");
    }

    public static final String separator = "_&&GTR&_";
    public static final String separatorFile = "_&&GTRFile&_";
    public static final String gtClearDataFlag = "--gtClearData---";


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
}
