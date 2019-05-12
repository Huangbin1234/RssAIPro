package com.hb.rssai.contract;

import com.rss.bean.Information;

import java.util.List;

/**
 * Created by Administrator
 * 2019/5/12 0012
 */
public interface OfflineListContract {
    interface View extends BaseView<Presenter> {
        void showList(List<Information> list);
        void showError();

        void showNoData();
    }

    interface Presenter extends BasePresenter {
        void getList(String link);
    }
}
