package com.hb.rssai.contract;

import com.hb.rssai.bean.Evaluate;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResCollectionBean;
import com.hb.rssai.bean.ResInfo;
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.bean.ResShareCollection;

/**
 * Created by Administrator
 * on 2018/8/24
 */
public interface RichTextContract {
    interface View extends BaseView<Presenter> {
        void showUpdateResult(ResBase resBase);

        void showListResult(ResInformation resInfo);

        void showAddResult(ResShareCollection resShareCollection);

        void showUpdateEvaluateResult(ResBase resBase);

        void showInfoResult(ResInfo resInfo);

        void showCollectionInfoIdResult(ResCollectionBean resCollectionBean);

        void loadError(Throwable throwable);

        void loadEvaluateError(Throwable throwable);
    }

    interface Presenter extends BasePresenter {
        void updateCount(String informationId);

        void getLikeByTitle(String title);

        void add(String newTitle, String newLink, String informationId, ResCollectionBean.RetObjBean mRetObjBean, String userId);

        void updateEvaluateCount(String informationId, String evaluateType, Evaluate evaluate);

        void getInformation(String informationId);

        void getCollectionByInfoId(String informationId, String userId);
    }
}
