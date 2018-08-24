package com.hb.rssai.contract;

import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResShareCollection;

/**
 * Created by Administrator
 * on 2018/8/24
 */
public interface ContentContract {
    interface View extends BaseView<Presenter> {
        void showAddSuccess(ResShareCollection resShareCollection);

        void showAddFailed(Throwable throwable);

        void showUpdateSuccess(ResBase resBase);

        void showUpdateFailed(Throwable throwable);
    }

    interface Presenter extends BasePresenter {
        void updateCount(String informationId);

        void add(String newTitle,String newLink,  String informationId,String userId);
    }
}
