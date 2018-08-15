package com.paisheng.prismsdk;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.paisheng.prismsdk.report.PrismDataToJSManager;
import com.paisheng.prismsdk.report.PrismSDCardPath;
import com.paisheng.prismsdk.report.server.WebService;
import com.paisheng.prismsdk.view.MainActivity;
import com.paisheng.psliblite.dialog.CommonDialog;
import com.tencent.wstt.gt.datatool.util.FileUtil;

import java.lang.ref.WeakReference;

public class ReportTaskManager {
    private static boolean isRun = false;

    public static void publishReport(Activity context, String fileName) {
        if (isRun) {
            Toast.makeText(context,"当前有正在发布的任务，请稍后再试",Toast.LENGTH_SHORT).show();
        } else {
            isRun = true;
            new publishTask(context,fileName).execute();
        }
    }

    public static void changeReport(Activity context, String fileName) {
        if (isRun) {
            Toast.makeText(context,"当前有正在发布的任务，请稍后再试",Toast.LENGTH_SHORT).show();
        } else {
            isRun = true;
            new reportTask(context,fileName).execute();
        }
    }

    private static class publishTask extends AsyncTask<Integer, Integer, String> {
        WeakReference<Activity> mContext;
        String fileName;

        public publishTask(Activity activity, String filePath) {
            mContext = new WeakReference<>(activity);
            fileName = filePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isDead()) {
                Toast.makeText(mContext.get(), "发布中，发布成功后会TOAST通知", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(Integer... integers) {
            if (isDead()) {
                return "failure";
            }
            try {
                PrismDataToJSManager.toAnalysis(fileName);
            } catch (Exception e) {
                e.printStackTrace();
                if (!isDead()) {
                    return e.getMessage();
                }
                return "failure";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);
            isRun = false;
            if (isDead()) {
                return ;
            }

            ((MainActivity)mContext.get()).isRun = false;

            if (TextUtils.isEmpty(message)) {
                WebService.start(mContext.get());
                Toast.makeText(mContext.get(), "发布成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext.get(), "发布失败：" + message, Toast.LENGTH_LONG).show();
            }
        }

        boolean isDead() {
            return mContext == null || mContext.get() == null || mContext.get().isFinishing();
        }
    }

    private static class reportTask extends AsyncTask<Integer, Integer, String> {
        WeakReference<Activity> mContext;
        String fileName;

        public reportTask(Activity activity, String filePath) {
            mContext = new WeakReference<>(activity);
            fileName = filePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isDead()) {
                Toast.makeText(mContext.get(), "报告加载中，请稍后", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(Integer... integers) {
            if (isDead()) {
                return "failure";
            }
            try {
                FileUtil.copyFile(fileName, PrismSDCardPath.PRISM_RESULT_DATA_PATH,true);
            } catch (Exception e) {
                e.printStackTrace();
                if (!isDead()) {
                    return e.getMessage();
                }
                return "failure";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);
            isRun = false;
            if (isDead()) {
                return ;
            }

            ((MainActivity)mContext.get()).isRun = false;

            if (TextUtils.isEmpty(message)) {
                WebService.start(mContext.get());
                Toast.makeText(mContext.get(), "报告加载成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext.get(), "报告加载失败：" + message, Toast.LENGTH_LONG).show();
            }
        }

        boolean isDead() {
            return mContext == null || mContext.get() == null || mContext.get().isFinishing();
        }
    }
}
