package com.tencent.wstt.gt.datatool.util;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <br> ClassName:   FileUtil
 * <br> Description: 新增GT默认路径和文件创建方法
 * <br>
 * <br> Author:      yexiaochuan
 * <br> Date:        2018/5/30 15:41
 */
public class FileUtil {
    private static String MESSAGE = "";

    public static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }

    /**
     * 复制单个文件
     *
     * @param srcFileName  待复制的文件名
     * @param destFileName 目标文件名
     * @param overlay      如果目标文件存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyFile(String srcFileName, String destFileName, boolean overlay) throws IOException {
        File srcFile = new File(srcFileName);

        // 判断源文件是否存在  
        if (!srcFile.exists()) {
            MESSAGE = "源文件：" + srcFileName + "不存在！";
            throw new IOException(MESSAGE);
        } else if (!srcFile.isFile()) {
            MESSAGE = "复制文件失败，源文件：" + srcFileName + "不是一个文件！";
            throw new IOException(MESSAGE);
        }

        // 判断目标文件是否存在  
        File destFile = new File(destFileName);
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖  
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件  
                new File(destFileName).delete();
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录  
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在  
                if (!destFile.getParentFile().mkdirs()) {
                    MESSAGE = "复制文件失败：" + destFile.getAbsolutePath() + " 创建目标文件所在目录失败";
                    throw new IOException(MESSAGE);
                }
            }
        }

        // 复制文件  
        int byteread = 0; // 读取的字节数  
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } finally {
            if (out != null)
                out.close();
            if (in != null)
                in.close();
        }
    }

    /**
     * 复制整个目录的内容
     *
     * @param srcDirName  待复制目录的目录名
     * @param destDirName 目标目录名
     * @param overlay     如果目标目录存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectory(String srcDirName, String destDirName,
                                        boolean overlay) throws IOException {
        // 判断源目录是否存在  
        File srcDir = new File(srcDirName);
        if (!srcDir.exists()) {
            MESSAGE = "复制目录失败：源目录" + srcDirName + "不存在！";
            System.out.println(MESSAGE);
            return false;
        } else if (!srcDir.isDirectory()) {
            MESSAGE = "复制目录失败：" + srcDirName + "不是目录！";
            System.out.println(MESSAGE);
            return false;
        }

        // 如果目标目录名不是以文件分隔符结尾，则加上文件分隔符  
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        File destDir = new File(destDirName);
        // 如果目标文件夹存在  
        if (destDir.exists()) {
            // 如果允许覆盖则删除已存在的目标目录  
            if (overlay) {
                new File(destDirName).delete();
            } else {
                MESSAGE = "复制目录失败：目的目录" + destDirName + "已存在！";
                System.out.println(MESSAGE);
                return false;
            }
        } else {
            // 创建目的目录  
            if (!destDir.mkdirs()) {
                System.out.println("复制目录失败：创建目的目录失败！" + destDir.getAbsolutePath());
                return false;
            }
        }

        boolean flag = true;
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 复制文件  
            if (files[i].isFile()) {
                flag = copyFile(files[i].getAbsolutePath(),
                        destDirName + files[i].getName(), overlay);
                if (!flag)
                    break;
            } else if (files[i].isDirectory()) {
                flag = copyDirectory(files[i].getAbsolutePath(),
                        destDirName + files[i].getName(), overlay);
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            MESSAGE = "复制目录" + srcDirName + "至" + destDirName + "失败！";
            System.out.println(MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    public static class SDCardPathHelper {
        // GTR测试数据的默认导出路径
        public static final String GTR_DATA_PATH = SDCardPathHelper.getSdcardPath() + "/Prism";
        // GTR测试数据的默认导出路径
        public static final String GTR_DATAJS_PATH_NAME = GTR_DATA_PATH + "/report/data/data.js";
        // GTR测试数据的默认导出路径
        public static final String GTR_WX_DATAJS_PATH_NAME = SDCardPathHelper.getSdcardPath() + "/Prism/report/data/data.js";
        public static final String CT_S_Sdcard_Sign_Storage_emulated = "storage/emulated/";
        public static final String CT_S_Sdcard_Sign_Storage_sdcard = "storage/sdcard";
        // 根据Nexus5 Android6.01适配
        public static final String CT_S_Sdcard_Sign_Storage_emulated_0 = "storage/emulated/0";
        public static final String CT_S_Sdcard_Sign_sdcard = "sdcard";

        private static String CD_S_SdcardPath = "";
        private static String CD_S_SdcardPathAbsolute = "";

        public static String getSdcardPath() {
            if (TextUtils.isEmpty(CD_S_SdcardPath)) {
                CD_S_SdcardPath = Environment.getExternalStorageDirectory().getPath();
            }

//            CD_S_SdcardPath = checkAndReplaceEmulatedPath(CD_S_SdcardPath);
            return CD_S_SdcardPath;
        }

        // 日期，到ms
        private static SimpleDateFormat saveDateMsFormatter =
                new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US);

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
                File testFile = new File(CD_S_SdcardPathAbsolute + "/GT/" + testFileName + "/");
                if (testFile.mkdirs()) {
                    FileUtil.deleteFile(testFile);
                } else {
                    result = strSrc.replace(CT_S_Sdcard_Sign_Storage_emulated_0, CT_S_Sdcard_Sign_sdcard);

                    // test
                    File testF = new File(result + "/GT/" + testFileName + "/");
                    if (testF.mkdirs()) {
                        FileUtil.deleteFile(testF);
                    }
                }
            }

            return result;
        }
    }
}  

