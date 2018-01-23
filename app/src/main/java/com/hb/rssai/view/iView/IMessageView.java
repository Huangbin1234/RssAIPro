package com.hb.rssai.view.iView;

import com.hb.rssai.bean.ResMessageList;

/**
 * Created by Administrator on 2017/8/18.
 */

public interface IMessageView {

    void loadError(Throwable throwable);

    void setListResult(ResMessageList resMessageList);

    String getUserId();

    int getPageNum();
}
