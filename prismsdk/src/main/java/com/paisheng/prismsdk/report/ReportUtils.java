package com.paisheng.prismsdk.report;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <br> ClassName:   ReportUtils
 * <br> Description:
 * <br>
 * <br> Author:      yexiaochuan
 * <br> Date:        2018/8/2 14:21
 */
public class ReportUtils {

    public static void copyAssets(Context context, String assetDir) {
        copyAssets(context, assetDir,PrismSDCardPath.PRISM_RESULT_PATH);
    }

    private static void copyAssets(Context context, String assetDir, String dir) {
        String[] files;
        try {
            files = context.getResources().getAssets().list(assetDir);
        } catch (IOException e1) {
            return;
        }

        File mWorkingPath = new File(dir);
        if (!mWorkingPath.exists()) {
            mWorkingPath.mkdirs();
        }

        for (String fileName : files) {
            try {
                if (fileName.contains(".")) {
                    writeToFile(context.getAssets(),assetDir,mWorkingPath,fileName);
                    continue;
                }

                if (0 == assetDir.length()) {
                    copyAssets(context, fileName, dir + fileName + "/");
                } else {
                    copyAssets(context, assetDir + "/" + fileName, dir + "/"
                            + fileName + "/");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeToFile(AssetManager assetManager, String assetDir, File mWorkingPath, String fileName) throws IOException {
        File outFile = new File(mWorkingPath, fileName);
        if (outFile.exists()) {
            outFile.delete();
        }

        InputStream in = null;
        if (0 != assetDir.length())
            in = assetManager.open(assetDir + "/" + fileName);
        else
            in = assetManager.open(fileName);
        OutputStream out = new FileOutputStream(outFile);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
