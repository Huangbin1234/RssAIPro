package com.hb.rssai.view.iView;

import com.hb.rssai.bean.Evaluate;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResCollectionBean;
import com.hb.rssai.bean.ResInfo;
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.bean.ResShareCollection;

/**
 * Created by Administrator on 2017/8/25.
 */

public interface IRichTextView {

    void loadError(Throwable throwable);

    void loadEvaluateError(Throwable throwable);

    void setUpdateResult(ResBase resBase);

    void setListResult(ResInformation resInfo);

    void setAddResult(ResShareCollection resShareCollection);

    void setUpdateEvaluateResult(ResBase resBase);

    void setInfoResult(ResInfo resInfo);

    void setCollectionInfoIdResult(ResCollectionBean resCollectionBean);

    ResCollectionBean.RetObjBean getRetObjBean();

    String getInfoId();

    String getInfoTitle();

    String getUserId();

    String getUrl();

    String getEvaluateType();

    Evaluate getEvaluate();

}
