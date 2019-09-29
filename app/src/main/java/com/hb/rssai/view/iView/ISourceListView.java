package com.hb.rssai.view.iView;

import com.hb.rssai.bean.ResCardSubscribe;
import com.hb.rssai.bean.ResSubscription;

/**
 * Created by Administrator on 2017/8/18 0018.
 */

public interface ISourceListView {
    String getSubscribeId();

    void loadError(Throwable throwable);

    void setListCardResult(ResCardSubscribe resCardSubscribe);

    int getPageNum();

    void setSubscription(ResSubscription resSubscription);
}
