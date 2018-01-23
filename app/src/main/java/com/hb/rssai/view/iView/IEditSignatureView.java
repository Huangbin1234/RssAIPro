package com.hb.rssai.view.iView;

import com.hb.rssai.bean.ResUser;

/**
 * Created by Administrator on 2017/9/27 0027.
 */

public interface IEditSignatureView {

    void setUpdateResult(ResUser resBase);

    void loadError(Throwable throwable);

    String getNewSignature();

    String getOldSignature();

    void setCheckResult(String error);

}
