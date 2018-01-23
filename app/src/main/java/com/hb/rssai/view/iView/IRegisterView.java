package com.hb.rssai.view.iView;

import com.hb.rssai.bean.ResBase;

/**
 * Created by Administrator on 2017/8/22 0022.
 */

public interface IRegisterView {

    void setRegResult(ResBase resBase);

    void loadError(Throwable throwable);

    void setCheckError(String error);

    String getUserName();

    String getPassword();

    String getSurePassword();
}
