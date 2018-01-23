package com.hb.rssai.view.iView;

import com.hb.rssai.bean.ResLogin;

/**
 * Created by Administrator on 2017/8/13 0013.
 */

public interface ILoginView extends IBaseView {

    void setLoginResult(ResLogin bean);

    void loadError(Throwable throwable);

    void setCheckError(String error);

    String getUserName();

    String getPassword();

}
