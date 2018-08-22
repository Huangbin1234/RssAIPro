package com.hb.rssai.contract;

import com.hb.rssai.bean.ResLogin;

public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void showCheckError(String error);

        void showLoginSuccess(ResLogin bean);

        void showLoginFailed(Throwable throwable);
    }

    interface Presenter extends BasePresenter {
        void login(String name, String password);
    }
}
