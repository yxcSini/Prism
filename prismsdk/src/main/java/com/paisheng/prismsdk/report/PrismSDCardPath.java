package com.paisheng.prismsdk.report;

import android.os.Environment;
import android.text.TextUtils;

import com.tencent.wstt.gt.datatool.util.FileUtil;
import com.tencent.wstt.gt.service.PrismConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrismSDCardPath {
    // 日期，到ms
    private static SimpleDateFormat saveDateMsFormatter =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    // Prism根路径
    public static String PRISM_ROOT_PATH = getSdcardPath() + "/Prism/null";
    // Prism报告路径
    public static String PRISM_REPORT_PATH = PRISM_ROOT_PATH + "/report";
    //Prism报告源码路径
    public static String PRISM_RESULT_PATH = PRISM_ROOT_PATH + "/result/";
    //Prism报告data路径
    public static String PRISM_RESULT_DATA_PATH = PRISM_ROOT_PATH + "/result/data/data.js";
    public static final String CT_S_Sdcard_Sign_Storage_emulated = "storage/emulated/";
    public static final String CT_S_Sdcard_Sign_Storage_sdcard = "storage/sdcard";
    // 根据Nexus5 Android6.01适配
    public static final String CT_S_Sdcard_Sign_Storage_emulated_0 = "storage/emulated/0";
    public static final String CT_S_Sdcard_Sign_sdcard = "sdcard";
    private static String CD_S_SdcardPath = "";
    private static String CD_S_SdcardPathAbsolute = "";

    public static void init(String packageName) {
        PRISM_ROOT_PATH = getSdcardPath() + "/Prism/" + packageName;
        PRISM_REPORT_PATH = PRISM_ROOT_PATH + "/report";
        PRISM_RESULT_PATH = PRISM_ROOT_PATH + "/result/";
        PRISM_RESULT_DATA_PATH = PRISM_ROOT_PATH + "/result/data/data.js";
    }

    public static String getSdcardPath() {
        if (TextUtils.isEmpty(CD_S_SdcardPath)) {
            CD_S_SdcardPath = Environment.getExternalStorageDirectory().getPath();
        }

        CD_S_SdcardPath = checkAndReplaceEmulatedPath(CD_S_SdcardPath);
        return CD_S_SdcardPath;
    }

    public static String getSaveDateMs() {
        Date date = new Date();
        return saveDateMsFormatter.format(date);
    }

    public static String getAbsoluteSdcardPath() {
        if (TextUtils.isEmpty(CD_S_SdcardPathAbsolute)) {
            CD_S_SdcardPathAbsolute = Environment.getExternalStorageDirectory().getAbsolutePath();
        }

        // 先试试默认的目录，如果创建目录失败再试其他方案
        String testFileName = getSaveDateMs();
        File testF = new File(CD_S_SdcardPathAbsolute + "/GT/" + testFileName + "/");
        if (testF.mkdirs()) {
            FileUtil.deleteFile(testF);
            return CD_S_SdcardPathAbsolute;
        }

        // 默认路径不可用，尝试其他方案
        CD_S_SdcardPathAbsolute = checkAndReplaceEmulatedPath(CD_S_SdcardPathAbsolute);
        return CD_S_SdcardPathAbsolute;
    }

    public static File getSdcardPathFile() {
        return new File(getSdcardPath());
    }

    public static String checkAndReplaceEmulatedPath(String strSrc) {
        String result = strSrc;
        Pattern p = Pattern.compile("/?storage/emulated/\\d{1,2}");
        Matcher m = p.matcher(strSrc);
        if (m.find()) {
            result= strSrc.replace(CT_S_Sdcard_Sign_Storage_emulated, CT_S_Sdcard_Sign_Storage_sdcard);
            // 如果目录建立失败，最后尝试Nexus5 Android6.01适配
            String testFileName = getSaveDateMs();
            File testFile = new File(CD_S_SdcardPathAbsolute + "/Prism/report/" + testFileName + "/");
            if (testFile.mkdirs()) {
                FileUtil.deleteFile(testFile);
            } else {
                result = strSrc.replace(CT_S_Sdcard_Sign_Storage_emulated_0, CT_S_Sdcard_Sign_sdcard);

                // test
                File testF = new File(result + "/Prism/" + testFileName + "/");
                if (testF.mkdirs()) {
                    FileUtil.deleteFile(testF);
                }
            }
        }

        return result;
    }

    public static String getSrcData(String packageName) {
        return PrismConfig.gtrDirPath + "/" + packageName + ".txt";
    }

    public static String getReportFile(String filePath) {
        return PrismSDCardPath.PRISM_REPORT_PATH + "/" + filePath + "/data.js";
    }
}
