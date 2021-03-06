package com.hb.widget;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hb.generalupdate.R;
import com.hb.update.Config;
import com.hb.util.DownloadManagerUtil;
import com.hb.util.StringUtils;

/**
 * 自定义对话框类
 *
 * @author hb
 */
public class UpdateDialog {
    private Context mContext;
    private android.app.AlertDialog adialog;
    private TextView titleView;
    private TextView update_tv_content;
    private LinearLayout buttonLayout;
    private ProgressBar progressx;
    private TextView update_alert_tv_url_down;
    private TextView update_alert_tv_back_down;
    private TextView update_tv_title;
    private String strHit = null;//下载地址

    private DownloadManagerUtil downloadManagerUtil;

    long downloadId = 0;

    public UpdateDialog(Context context) {
        if (null == context) {
            return;
        }
        this.mContext = context;
        downloadManagerUtil = new DownloadManagerUtil(mContext);
        adialog = new Builder(context, R.style.CustomProgressDialog).create();
        adialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 返回取消对话框
        adialog.setCancelable(false);
        adialog.show();

        initView();
        initData();
    }

    private void initData() {
        update_alert_tv_url_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == update_alert_tv_url_down.getHint()) {
                    strHit = Config.APK_DOWNLOAD_URL;
                } else {
                    strHit = update_alert_tv_url_down.getHint().toString();
                }
                //跳转打开浏览器下载APK
                if (StringUtils.isBlank(strHit)) {
                    return;
                }
                Uri uri = Uri.parse(strHit);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
        update_alert_tv_back_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转打开浏览器下载APK
                strHit = Config.APK_DOWNLOAD_URL;
                if (StringUtils.isBlank(strHit)) {
                    return;
                }
                //TODO 跳转到后台下载
                if (downloadId != 0) {
                    downloadManagerUtil.clearCurrentTask(downloadId);
                }
                downloadId = downloadManagerUtil.download(strHit, Config.getAppName(mContext) + " v" + Config.NEW_VER_CODE, Config.UPDATE_CONTENT);
                dismiss();
            }
        });
    }

    private void initView() {
        // 使用window.setContentView,替换对话框窗口布局
        Window window = adialog.getWindow();
        window.setContentView(R.layout.alert_dialog);
        titleView = window.findViewById(R.id.update_alert_title);
        update_tv_content = window.findViewById(R.id.update_tv_content);
        buttonLayout = window
                .findViewById(R.id.update_alert_buttonLayout);
        progressx = window
                .findViewById(R.id.update_alert_progressx);
        update_alert_tv_url_down = window
                .findViewById(R.id.update_alert_tv_url_down);
        update_tv_title = window
                .findViewById(R.id.update_tv_title);
        update_alert_tv_back_down = window
                .findViewById(R.id.update_alert_tv_back_down);

    }

    public void setAlertTitle(int resId) {
        titleView.setText(resId);
    }

    public void setAlertTitle(String title) {
        titleView.setText(title);
    }

    public void setContent(int resId) {
        update_tv_content.setText(resId);
    }

    public void setContent(String message) {
        update_tv_content.setText(message);
    }

    public void setTitle(String title) {
        update_tv_title.setText(title);
    }

    //跳转浏览器设置隐藏URL
    public void setUrlHit(String hitText) {
        update_alert_tv_url_down.setHint(hitText);
    }

    // 显示或隐藏进度条
    public void setViewProgress(boolean flag) {
        if (flag) {
            progressx.setVisibility(View.VISIBLE);
        } else {
            progressx.setVisibility(View.GONE);
        }
    }

    // 获取进度条ProgressBar
    public ProgressBar getProcessbar() {
        return progressx;
    }

    // 获取进度百分比TextView
    public TextView getTextView() {
        return update_tv_content;
    }

    /**
     * 确认按钮
     *
     * @param text
     * @param listener
     */
    public void setPositiveButton(String text,
                                  final View.OnClickListener listener) {
        Button button = new Button(mContext);
        LinearLayout.LayoutParams params = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.LEFT;
        params.weight = 1;
        button.setLayoutParams(params);
        button.setBackgroundResource(R.drawable.alertdialog_button);
        button.setText(text);
        button.setTextColor(Color.WHITE);
        button.setTextSize(16);
        button.setOnClickListener(listener);
        buttonLayout.addView(button);
    }

    /**
     * 取消按钮
     *
     * @param text
     * @param listener
     */
    public void setNegativeButton(String text,
                                  final View.OnClickListener listener) {
        Button button = new Button(mContext);
        LinearLayout.LayoutParams params = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        params.weight = 1;
        button.setLayoutParams(params);
        button.setBackgroundResource(R.drawable.alertdialog_button);
        button.setText(text);
        button.setTextColor(Color.WHITE);
        button.setTextSize(16);
        button.setOnClickListener(listener);
        if (buttonLayout.getChildCount() > 0) {
            params.setMargins(20, 0, 0, 0);
            button.setLayoutParams(params);
            buttonLayout.addView(button, 1);
        } else {
            button.setLayoutParams(params);
            buttonLayout.addView(button);
        }
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        if (adialog.isShowing()) {
            adialog.dismiss();
        }
    }
}