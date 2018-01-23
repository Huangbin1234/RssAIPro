package com.hb.rssai.view.iView;

import android.view.MenuItem;

import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResCollectionBean;
import com.hb.rssai.bean.ResInfo;
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.bean.ResShareCollection;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/25.
 */

public interface IRichTextView {

    Map<String, String> getUpdateParams();

    void loadError(Throwable throwable);

    void setUpdateResult(ResBase resBase);

    Map<String, String> getParams();

    void setListResult(ResInformation resInfo);

    Map<String, String> getAddParams();

    void setAddResult(ResShareCollection resShareCollection);

    void loadEvaluateError(Throwable throwable);

    Map<String, String> getUpdateEvaluateParams();

    void setUpdateEvaluateResult(ResBase resBase);

    Map<String, String> getInfoParams();

    void setInfoResult(ResInfo resInfo);

    Map<String, String> getCollectionByInfoIdParams();

    void setCollectionInfoIdResult(ResCollectionBean resCollectionBean);

}
