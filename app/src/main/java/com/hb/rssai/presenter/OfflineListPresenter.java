package com.hb.rssai.presenter;

import com.hb.rssai.contract.OfflineListContract;
import com.rss.bean.Information;
import com.rss.util.FeedReader;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator
 * 2019/5/12 0012
 */
public class OfflineListPresenter extends BasePresenter<OfflineListContract.View> implements OfflineListContract.Presenter {
    OfflineListContract.View mView;
    FeedReader feedReader;
    ExecutorService singleThreadExecutor;

    public OfflineListPresenter(OfflineListContract.View view) {
        this.mView = view;
        this.mView.setPresenter(this);
        feedReader = new FeedReader();
        singleThreadExecutor = Executors.newCachedThreadPool();
    }

    @Override
    public void start() {
    }

    @Override
    public void getList(String link) {
        singleThreadExecutor.execute(() -> {
            List<Information> infoList = feedReader.getInfoList(link);
            if (null != infoList&&infoList.size() > 0) {
                mView.showList(infoList);
            } else if (infoList.size() <= 0) {
                mView.showNoData();
            } else {
                mView.showError();
            }
        });
    }
}
