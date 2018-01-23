package com.hb.rssai.presenter;

import android.text.TextUtils;

import com.hb.rssai.view.iView.ILoginView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/13 0013.
 */

public class LoginPresenter extends BasePresenter<ILoginView> {
    private ILoginView iLoginView;

    public LoginPresenter(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
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

    public void login() {
        String error;
        String uName = iLoginView.getUserName();
        String uPsd = iLoginView.getPassword();
        if ((error = checkUserName(uName)) != null) {
            iLoginView.setCheckError(error);
            return;
        }
        if ((error = checkPassword(uPsd)) != null) {
            iLoginView.setCheckError(error);
            return;
        }
        loginApi.doLogin(iLoginView.getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resLogin -> {
                    iLoginView.setLoginResult(resLogin);
                }, iLoginView::loadError);
    }
}
