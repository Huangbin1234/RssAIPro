package com.hb.rssai.contract;

import com.hb.rssai.bean.ResBase;

/**
 * Created by Administrator
 * on 2018/8/24
 */
public interface ForgetContract {
    interface View extends BaseView<Presenter> {

        void showRetrieveSuccess(ResBase resBase);

        void showRetrieveFailed(Throwable throwable);

        void showCheckError(String error);
    }

    interface Presenter extends BasePresenter {
        void retrievePassword(String type, String email, String userName, String mobile);
    }
}
