package com.tencent.wstt.gt.datatool;

import com.alibaba.fastjson.JSON;
import com.tencent.wstt.gt.datatool.util.FileUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Main {


    //获取数据目录：
    private static File nowDir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
//    private static File nowDir = new File("D:\\Android\\demo\\PrismReport");

    public static final String dataDirPath = new File(nowDir, "data").getAbsolutePath();
    public static final String srcDirPath = new File(nowDir, "src").getAbsolutePath();
    public static final String resultDirPath = new File(nowDir, "result").getAbsolutePath();
    public static final String resultDataFilePath = resultDirPath + "/data/data.js";


    public static final ArrayList<GTRAnalysis> dataAnalysises = new ArrayList<>();

    public static void main(String[] args) throws Exception {


        //GTRAnalysis：读取数据文件，装载数据对象
        File dataDir = new File(dataDirPath);
        if (!dataDir.exists()) {
            throw new Exception("dataDirPath is not exists :" + dataDirPath);
        }
        File[] dataFiles = dataDir.listFiles();
        for (File temp : dataFiles) {
            if (temp.getName().endsWith(".txt")) {
                GTRAnalysis gtrAnalysis = getGTRAnalysis(temp.getAbsolutePath());
                dataAnalysises.add(gtrAnalysis);
            }
        }

        //GTRDataHandler：将对象数据转化为HTML数据
        GTRAnalysis gtrAnalysis = dataAnalysises.get(0);//TODO 暂时只分析第一个文件
        FileUtil.copyDirectory(srcDirPath, resultDirPath, true);//拷贝HTML库文件

        File resultDataFile = new File(resultDataFilePath);
        if (resultDataFile.exists()) {
            resultDataFile.delete();
        }
        resultDataFile.getParentFile().mkdirs();
        resultDataFile.createNewFile();

        toDataJs(resultDataFile,gtrAnalysis);
//        appendHtmlData("");
//        appendHtmlData("");
//        appendHtmlData("");
//        appendHtmlData("var appInfo = " + JSON.toJSONString(gtrAnalysis.getAppInfo()) + ";");
//        appendHtmlData("var deviceInfo = " + JSON.toJSONString(gtrAnalysis.getDeviceInfo()) + ";");
//        appendHtmlData("var frames = " + JSON.toJSONString(gtrAnalysis.getFrames()) + ";");
//        appendHtmlData("var normalInfos = " + JSON.toJSONString(gtrAnalysis.getNormalInfos()) + ";");
//        appendHtmlData("var gtrThreadInfos = " + JSON.toJSONString(gtrAnalysis.getGtrThreadInfos()) + ";");
//        appendHtmlData("var frontBackStates = " + JSON.toJSONString(gtrAnalysis.getFrontBackStates()) + ";");
//        appendHtmlData("var frontBackInfo = " + JSON.toJSONString(gtrAnalysis.getFrontBackInfo()) + ";");
//        appendHtmlData("var lowSMInfos = " + JSON.toJSONString(gtrAnalysis.getLowSMInfos()) + ";");
//        appendHtmlData("var allBlockInfos = " + JSON.toJSONString(gtrAnalysis.getAllBlockInfos()) + ";");
//        appendHtmlData("var bigBlockIDs = " + JSON.toJSONString(gtrAnalysis.getBigBlockIDs()) + ";");
//        appendHtmlData("var pageLoadInfos = " + JSON.toJSONString(gtrAnalysis.getPageLoadInfos()) + ";");
//        appendHtmlData("var overActivityInfos = " + JSON.toJSONString(gtrAnalysis.getOverActivityInfos()) + ";");
//        appendHtmlData("var overViewDraws = " + JSON.toJSONString(gtrAnalysis.getOverViewDraws()) + ";");
//        appendHtmlData("var operationInfos = " + JSON.toJSONString(gtrAnalysis.getOperationInfos()) + ";");
//        appendHtmlData("var viewBuildInfos = " + JSON.toJSONString(gtrAnalysis.getViewBuildInfos()) + ";");
//        appendHtmlData("var overViewBuilds = " + JSON.toJSONString(gtrAnalysis.getOverViewBuilds()) + ";");
//        appendHtmlData("var fragmentInfos = " + JSON.toJSONString(gtrAnalysis.getFragmentInfos()) + ";");
//        appendHtmlData("var overFragments = " + JSON.toJSONString(gtrAnalysis.getOverFragments()) + ";");
//        appendHtmlData("var allGCInfos = " + JSON.toJSONString(gtrAnalysis.getAllGCInfos()) + ";");
//        appendHtmlData("var explicitGCs = " + JSON.toJSONString(gtrAnalysis.getExplicitGCs()) + ";");
//        appendHtmlData("var diskIOInfos = " + JSON.toJSONString(gtrAnalysis.getDiskIOInfos()) + ";");
//        appendHtmlData("var fileActionInfos = " + JSON.toJSONString(gtrAnalysis.getFileActionInfos()) + ";");
//        appendHtmlData("var fileActionInfosInMainThread = " + JSON.toJSONString(gtrAnalysis.getFileActionInfosInMainThread()) + ";");
//        appendHtmlData("var dbActionInfos = " + JSON.toJSONString(gtrAnalysis.getDbActionInfos()) + ";");
//        appendHtmlData("var dbActionInfosInMainThread = " + JSON.toJSONString(gtrAnalysis.getDbActionInfosInMainThread()) + ";");
//        appendHtmlData("var logInfos = " + JSON.toJSONString(gtrAnalysis.getLogInfos()) + ";");
//
//        appendHtmlData("");
//        appendHtmlData("");
//        appendHtmlData("");
//
//
//        appendHtmlData("//基础性能");
//        appendHtmlData("var tableBaseData_base= frontBackInfo;");
//        appendHtmlData("//卡顿检测");
//        appendHtmlData("var tableBaseData_lowSM = lowSMInfos;");
//        appendHtmlData("var tableBaseData_bigBlock = bigBlockIDs;");
//        appendHtmlData("//页面测速");
//        appendHtmlData("var tableBaseData_overActivity = overActivityInfos;");
//        appendHtmlData("var tableBaseData_allPage = pageLoadInfos;");
//        appendHtmlData("//Fragment测速");
//        appendHtmlData("var tableBaseData_overFragment = overFragments;");
//        appendHtmlData("var tableBaseData_allFragment = fragmentInfos;");
//        appendHtmlData("//布局检测");
//        appendHtmlData("var tableBaseData_overViewBuild = overViewBuilds;");
//        appendHtmlData("var tableBaseData_overViewDraw = overViewDraws;");
//        appendHtmlData("//GC检测");
//        appendHtmlData("var tableBaseData_explicitGC = explicitGCs;");
//        appendHtmlData("//IO检测");
//        appendHtmlData("var tableBaseData_fileActionInMainThread = fileActionInfosInMainThread;");
//        appendHtmlData("var tableBaseData_dbActionInMainThread = dbActionInfosInMainThread;");
//        appendHtmlData("var tableBaseData_db = dbActionInfos;");
//        appendHtmlData("//关键日志");
//        appendHtmlData("var tableBaseData_logcat = logInfos;");


//        //基础信息：
//        appendHtmlData("var normalInfoArray = " + JSON.toJSONString(gtrAnalysis.getNormalInfos()) + ";");
//        appendHtmlData("var operationInfoArray = " + JSON.toJSONString(gtrAnalysis.getOperationInfos()) + ";");
//        appendHtmlData("var viewBuildInfoArray = " + JSON.toJSONString(gtrAnalysis.getViewBuildInfos()) + ";");
//        appendHtmlData("var screenStateArray = " + JSON.toJSONString(gtrAnalysis.getScreenStates()) + ";");
//        appendHtmlData("var frontBackStateArray = " + JSON.toJSONString(gtrAnalysis.getFrontBackStates()) + ";");

//        appendHtmlData("var baseSummaryChart_data = " + GTRDataHandler.get_baseSummaryChart_data(gtrAnalysis) + ";");
//        appendHtmlData("var blockLowSMTable_data = " + GTRDataHandler.get_blockLowSMTable_data(gtrAnalysis) + ";");
//        appendHtmlData("var blockBigTable_data = " + GTRDataHandler.get_blockBigTable_data(gtrAnalysis) + ";");
//        appendHtmlData("var pageLoadOverTable_data = " + GTRDataHandler.get_pageLoadOverTable_data(gtrAnalysis) + ";");
//        appendHtmlData("var pageLoadAllTable_data = " + GTRDataHandler.get_pageLoadAllTable_data(gtrAnalysis) + ";");
//        appendHtmlData("var ioAllFileTable_data = " + GTRDataHandler.get_ioAllFileTable_data(diskIOInfos, appInfo) + ";");


        System.out.println("数据报告已生成：" + resultDirPath);


    }

    private static final int BUFFER_SIZE = 1 << 20;

    private static void toDataJs(File des, GTRAnalysis gtrAnalysis) {
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

//    static GTRAnalysis getGTRAnalysis(String dataFilePath) throws Exception {
//        File file = new File(dataFilePath);
//        if (!file.exists()) {
//            throw new Exception("dataFilePath is not exists:" + dataFilePath);
//        }
//        GTRAnalysis gtrAnalysis = new GTRAnalysis();
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
//        String dataLine = bufferedReader.readLine();
//        while (dataLine != null) {
//            try {
//                if (dataLine.length() > 0) {
//                    if (dataLine.startsWith(gtClearDataFlag)) {
//                        gtrAnalysis.clear();
//                    } else {
//                        gtrAnalysis.distribute(dataLine.split(separator));
//                    }
//                }
//            } catch (Exception e) {
//                System.out.println("ErrorData:" + dataLine);
//                e.printStackTrace();
//            }
//
//            dataLine = bufferedReader.readLine();
//        }
//        return gtrAnalysis;
//    }

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
    /**
     * 以追加的形式写入HTML数据
     */
    static BufferedWriter bufferedWriter;

    static void appendHtmlData(String data) throws IOException {
        if (bufferedWriter == null) {
            File resultDataFile = new File(resultDataFilePath);
            if (resultDataFile.exists()) {
                resultDataFile.delete();
            }
            resultDataFile.getParentFile().mkdirs();
            resultDataFile.createNewFile();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultDataFile, true), "utf-8"));
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(data).append("\n");
        bufferedWriter.write(stringBuilder.toString());
        bufferedWriter.flush();
    }


}
