package com.hb.rssai.presenter;

import com.hb.rssai.constants.Constant;
import com.hb.rssai.contract.OfflineListContract;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.StringUtil;
import com.rss.bean.Information;
import com.rss.util.FeedReader;

import java.util.Date;
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
    public void getList(String link, String subscribeId) {
        singleThreadExecutor.execute(() -> {
            //先获取本地数据
            try {
                List<Information> localInfoList = LiteOrmDBUtil.getQueryByWhere(Information.class, "subscribeId", new String[]{subscribeId});
                if (null != localInfoList && localInfoList.size() > 0) {
                    String oprTime = localInfoList.get(0).getOprTime();
                    Date date = new Date();
                    int t = StringUtil.randomNumber(5, 28);
                    if ((date.getTime() - Constant.sdf.parse(oprTime).getTime()) <= t * Constant.DEFAULT_COLLECTION_TIME) {
                        mView.showList(localInfoList);
                        //更新一下本地存储的时间
                        LiteOrmDBUtil.updateWhere(Information.class, "subscribeId", new String[]{subscribeId}, com.hb.rssai.bean.Information.COL_OPR_TIME, new String[]{Constant.sdf.format(date)});
                    } else {
                        getData(link, subscribeId);
                    }
                } else {
                    getData(link, subscribeId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void getData(String link, String subscribeId) {
        //如果本地数据不存在则去获取
        List<Information> infoList = feedReader.getInfoList(link, subscribeId);
        if (null != infoList && infoList.size() > 0) {
            //清除本地数据
            LiteOrmDBUtil.deleteWhere(Information.class, "subscribeId", new String[]{subscribeId});
            //存储数据到本地
            LiteOrmDBUtil.insertAll(infoList);
            mView.showList(infoList);
        } else if (infoList.size() <= 0) {
            mView.showNoData();
        } else {
            mView.showError();
        }
    }
}
