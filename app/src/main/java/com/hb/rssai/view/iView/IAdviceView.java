package com.hb.rssai.view.iView;

import com.hb.rssai.bean.ResBase;

/**
 * Created by Administrator on 2017/8/13 0013.
 */

public interface IAdviceView {

    void setAddResult(ResBase resBase);

    void loadError(Throwable throwable);

    String getEtContent();

    void setCheckResult(String error);
}
