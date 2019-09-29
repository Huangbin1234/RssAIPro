package com.hb.rssai.util;

import android.content.Intent;
import android.text.TextUtils;

import com.hb.rssai.app.ProjectApplication;
import com.hb.rssai.event.MainEvent;
import com.hb.rssai.view.common.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Administrator
 * on 2018/9/26
 */
public class CommonHandler {
    public static void actionOpr(int code, String msg) {
        switch (code) {
            case 401:
                //跳转到登录 token 失效
                T.ShowToast(ProjectApplication.getApplication(), "登录过期，请重新登录");
                Intent intent = new Intent(ProjectApplication.mContext, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ProjectApplication.mContext.startActivity(intent);

                EventBus.getDefault().post(new MainEvent(1));
                break;
            default:
                if (!TextUtils.isEmpty(msg)) {
                    T.ShowToast(ProjectApplication.getApplication(), msg);
                }
                break;
        }
    }

    /**
     * 捕获 Throwable错误异常
     *
     * @param throwable
     */
    public static void actionThrowable(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof Error) {
            T.ShowToast(ProjectApplication.mContext, "发生错误");
        } else if (throwable instanceof NumberFormatException) {
            T.ShowToast(ProjectApplication.mContext, "数字格式异常");
        } else if (throwable instanceof IndexOutOfBoundsException) {
            T.ShowToast(ProjectApplication.mContext, "数组越界异常");
        } else if (throwable instanceof NullPointerException) {
            T.ShowToast(ProjectApplication.mContext, "空指针异常");
        } else if (throwable instanceof TimeoutException) {
            T.ShowToast(ProjectApplication.mContext, "连接超时");
        } else if (throwable instanceof ConnectException) {
            T.ShowToast(ProjectApplication.mContext, "连接服务器异常");
        } else if (throwable instanceof SocketTimeoutException) {
            T.ShowToast(ProjectApplication.mContext, "连接超时");
        } else if (throwable instanceof SecurityException) {
            T.ShowToast(ProjectApplication.mContext, "安全异常");
        } else if (throwable instanceof UnsupportedOperationException) {
            T.ShowToast(ProjectApplication.mContext, "非法操作");
        } else if (throwable instanceof StringIndexOutOfBoundsException) {
            T.ShowToast(ProjectApplication.mContext, "字符串下标越界异常");
        } else if (throwable instanceof UnknownHostException) {
            T.ShowToast(ProjectApplication.mContext, "未知主机异常");
        } else {
            T.ShowToast(ProjectApplication.mContext, throwable.getMessage());
        }
    }
}
