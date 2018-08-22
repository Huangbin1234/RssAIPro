package com.hb.rssai.presenter;

import android.text.TextUtils;

import com.hb.rssai.contract.RegisterContract;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/22 0022.
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {

    private RegisterContract.View mView;

    public RegisterPresenter(RegisterContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }

    private String checkUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            return "请输入账号";
        }
        return null;
    }

    private String checkPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return "请输入密码";
        }
        if (password.length() < 6 || password.length() > 16) {
            return "密码长度应为6~16位，请修改";
        }
        return null;
    }

    private String checkSurePassword(String sPassword) {
        if (TextUtils.isEmpty(sPassword)) {
            return "请再次输入密码";
        }
        if (sPassword.length() < 6 || sPassword.length() > 16) {
            return "密码长度应为6~16位，请修改";
        }
        return null;
    }

    private String checkSame(String psd, String sPsd) {
        if (!sPsd.equals(psd)) {
            return "两次输入的密码不一致";
        }
        return null;
    }

    @Override
    public void register(String name, String psd, String sPsd) {

        String error;
        if ((error = checkUserName(name)) != null) {
            mView.showCheckError(error);
            return;
        }
        if ((error = checkPassword(psd)) != null) {
            mView.showCheckError(error);
            return;
        }
        if ((error = checkSurePassword(sPsd)) != null) {
            mView.showCheckError(error);
            return;
        }
        if ((error = checkSame(psd, sPsd)) != null) {
            mView.showCheckError(error);
            return;
        }
        loginApi.register(getParams(name, psd, sPsd))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    mView.showRegisterSuccess(resBase);
                }, mView::showRegisterFailed);
    }

    public Map<String, String> getParams(String name, String psd, String sPsd) {
        Map<String, String> params = new HashMap<>();
        String jsonParams = "{\"userName\":\"" + name + "\",\"password\":\"" + psd + "\",\"sPassword\":\"" + sPsd + "\"}";
        params.put("jsonParams", jsonParams);
        return params;
    }


    @Override
    public void start() {

    }
}
