package com.hb.rssai.view.iView;

import android.view.View;

import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResFindMore;

/**
 * Created by Administrator on 2017/8/15.
 */

public interface IFindView {

    void setFindMoreResult(ResFindMore resFindMore);

    void setRecommendResult(ResFindMore resFindMore);

    void setAddResult(ResBase resBase, View v, boolean isRecommend);

    void setDelResult(ResBase resBase, View v, boolean isRecommend);

    String getRowsBeanId();

    String getPage();

    String getRecommendPage();

    void showFindError();

    void showLoadError();

    String getUserID();

    void showToast(String retMsg);
}
