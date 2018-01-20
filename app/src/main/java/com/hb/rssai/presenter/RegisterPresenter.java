package com.hb.rssai.presenter;

import com.hb.rssai.view.iView.IRegisterView;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/22 0022.
 */

public class RegisterPresenter extends BasePresenter<IRegisterView> {
    private IRegisterView mIRegisterView;

    public RegisterPresenter(IRegisterView IRegisterView) {
        mIRegisterView = IRegisterView;
    }

    public void register() {
        Map<String, String> params = mIRegisterView.getParams();
        if (params == null) {
            return;
        }
        loginApi.register(mIRegisterView.getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    mIRegisterView.setRegResult(resBase);
                }, mIRegisterView::loadError);
    }

}
