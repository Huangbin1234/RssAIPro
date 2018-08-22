package com.hb.rssai.presenter;

import android.text.TextUtils;

import com.hb.rssai.contract.LoginContract;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/13 0013.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private LoginContract.View mView;

    public LoginPresenter(LoginContract.View mView) {
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

    @Override
    public void login(String name, String password) {
        String error;
        if ((error = checkUserName(name)) != null) {
            mView.showCheckError(error);
            return;
        }
        if ((error = checkPassword(password)) != null) {
            mView.showCheckError(error);
            return;
        }
        loginApi.doLogin(getParams(name, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resLogin -> {
                    mView.showLoginSuccess(resLogin);
                }, mView::showLoginFailed);
    }

    public Map<String, String> getParams(String name, String password) {
        Map<String, String> params = new HashMap<>();
        String jsonParams = "{\"userName\":\"" + name + "\",\"password\":\"" + password + "\"}";
        params.put("jsonParams", jsonParams);
        return params;
    }

    @Override
    public void start() {

    }
}
