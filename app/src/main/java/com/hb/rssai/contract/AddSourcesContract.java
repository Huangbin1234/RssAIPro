package com.hb.rssai.contract;

import com.hb.rssai.bean.ResTheme;

/**
 * Created by Administrator
 * on 2018/8/27
 */
public interface AddSourcesContract {

    interface View extends BaseView<Presenter> {

        void showAddSuccess();

        void showListResult(ResTheme resTheme);

        void showMsg(String s);
    }

    interface Presenter extends BasePresenter {

        void getList(int page);

        void addRss(String rssLink, String rssTitle, String userId);

        void addOpmlRss(String rssLink, String rssTitle, String userId);

        void updateImage(String rssTitle, String userId);

    }
}
