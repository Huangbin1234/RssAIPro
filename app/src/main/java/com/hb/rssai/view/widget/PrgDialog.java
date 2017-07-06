package com.hb.rssai.view.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;

import com.hb.rssai.R;


/**
 * 自定义4.0风格对话框
 * Created by Administrator on 2017/1/12.
 */

public class PrgDialog {
    private ProgressDialog mProgressDialog;
    public Context context;

    public PrgDialog(Context context, boolean isStill) {
        this.context = context;
        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);//设置主题
        mProgressDialog.setMessage(context.getResources().getString(R.string.str_loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();

        mProgressDialog.setCancelable(isStill);//dialog弹出后会点击屏幕或物理返回键，dialog不消失
        mProgressDialog.setCanceledOnTouchOutside(isStill);//dialog弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失

        Point size = new Point();
        mProgressDialog.getWindow().getWindowManager().getDefaultDisplay().getSize(size);//记得用mProgressDialog来得到这个界面的大小，实际上不加就是得到当前监听器匿名类对象的界面宽度
    }

    public void closeDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void setMessage(String str) {
        if (mProgressDialog != null) {
            mProgressDialog.setMessage(context.getResources().getString(R.string.str_loading) + str);
        }
    }
}
