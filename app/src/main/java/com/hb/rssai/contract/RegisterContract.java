package com.hb.rssai.contract;

import com.hb.rssai.bean.ResBase;

/**
 * Created by Administrator
 * 2018/8/23 0023
 */
public interface RegisterContract {
    interface View extends BaseView<Presenter> {

        void showCheckError(String error);

        void showRegisterSuccess(ResBase resBase);

        void showRegisterFailed(Throwable throwable);

    }

    interface Presenter extends BasePresenter {
        void register(String name, String psd, String sPsd);
    }
}
