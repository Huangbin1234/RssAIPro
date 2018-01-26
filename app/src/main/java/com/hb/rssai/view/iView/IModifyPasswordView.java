package com.hb.rssai.view.iView;

import com.hb.rssai.bean.ResBase;

/**
 * Created by Administrator on 2018/1/27 0027.
 */

public interface IModifyPasswordView {
    void showModifyResult(ResBase resBase);

    void loadError(Throwable throwable);

    String getOldPsd();

    String getNewPsd();

    String getNewSPsd();

    void setCheckError(String error);

}
