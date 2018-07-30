package com.hb.rssai.view.iView;

import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResMessageList;
import com.hb.rssai.bean.ResShareCollection;
import com.hb.rssai.bean.ResUser;

/**
 * Created by Administrator on 2017/8/20 0020.
 */

public interface IMineView {

    String getInformationId();

    String getSubscribeId();

    void setResult(ResUser resUser);

    void setAddResult(ResShareCollection resShareCollection);

    void setAddSubscribeResult(ResBase resBase);

    void setMessageListResult(ResMessageList resMessageList);

    void showLoadUserError();
}
