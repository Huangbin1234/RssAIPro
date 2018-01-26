package com.hb.rssai.view.iView;

import com.hb.rssai.bean.ResBase;

/**
 * Created by Administrator on 2018/1/27 0027.
 */

public interface IForgetView {
    String getType();

    String getEmail();

    String getUserName();

    String getMobile();

    void showFindResult(ResBase resBase);

    void loadError(Throwable throwable);

    void setCheckError(String error);
}
