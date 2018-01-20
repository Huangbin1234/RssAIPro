package com.hb.rssai.presenter;

import com.hb.rssai.view.iView.ILoginView;

import java.util.Map;

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

    public void login() {
        Map<String, String> params = iLoginView.getParams();
        if (params == null) {
            return;
        }
        loginApi.doLogin(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resLogin -> {
                    iLoginView.setLoginResult(resLogin);
                }, iLoginView::loadError);
    }
}
