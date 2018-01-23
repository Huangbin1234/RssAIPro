package com.hb.rssai.view.iView;

import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResCollection;
import com.hb.rssai.bean.ResInfo;

/**
 * Created by Administrator on 2017/8/15.
 */

public interface ICollectionView {

    void loadError(Throwable throwable);

    void setDelResult(ResBase resBase);

    void setListResult(ResCollection resCollection);

    void setInfoResult(ResInfo resInfo);

    int getPageNum();

    String getInfoId();

    String getCollectionId();

    String getUserId();
}
