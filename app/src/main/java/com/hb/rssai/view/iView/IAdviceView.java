package com.hb.rssai.view.iView;

import com.hb.rssai.bean.ResBase;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/13 0013.
 */

public interface IAdviceView {
    void toFinish();

    void setAddResult(ResBase resBase);

    void loadError(Throwable throwable);

    Map<String, String> getParams();
}
