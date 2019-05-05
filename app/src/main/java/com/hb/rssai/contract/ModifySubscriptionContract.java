package com.hb.rssai.contract;

import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResDataGroup;

import java.util.Map;

/**
 * Created by Administrator
 * on 2019/5/5
 */
public interface ModifySubscriptionContract {

    interface View extends BaseView<Presenter> {
        void showModifyResult(ResBase resBase);

        void loadError(Throwable throwable);

        void setDataGroupResult(ResDataGroup resDataGroup);
    }

    interface Presenter extends BasePresenter {
        void modifySubscription(Map<String,Object> params);
    }
}
