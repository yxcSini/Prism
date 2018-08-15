package com.paisheng.prismsdk.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.paisheng.prismsdk.R;
import com.paisheng.prismsdk.ReportTaskManager;
import com.paisheng.prismsdk.report.PrismSDCardPath;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReportSelectDialog {
    private Context context;
    private Dialog dialog;
    private ListView listView;
    private Button cancelBt;
    private Display display;

    public ReportSelectDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public ReportSelectDialog builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_select_dialog, null);

        view.setMinimumWidth(display.getWidth());

        dialog = new Dialog(context, R.style.PopupMenuDialogStyle);
        dialog.setContentView(view);

        listView = view.findViewById(R.id.lv_select);
        view.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final File file = new File(PrismSDCardPath.PRISM_REPORT_PATH);
        final String[] filePaths = arraryReverse(file.list());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.select_dialog_item,filePaths);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReportTaskManager.changeReport((Activity) context
                        , PrismSDCardPath.getReportFile(filePaths[position]));

            }
        });

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        return this;
    }

    private String[] arraryReverse(String[] data) {
        if (data == null || data.length == 0) {
            return new String[]{};
        }

        List<String> list = Arrays.asList(data);
        Collections.reverse(list);
        return (String[]) list.toArray();
    }

    public ReportSelectDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public ReportSelectDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public void show() {
        dialog.show();
    }
}