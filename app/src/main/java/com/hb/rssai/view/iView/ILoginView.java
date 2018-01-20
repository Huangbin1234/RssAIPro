package com.hb.rssai.view.iView;

import com.hb.rssai.bean.ResLogin;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/13 0013.
 */

public interface ILoginView extends IBaseView{

    Map<String,String> getParams();
    void setLoginResult(ResLogin bean);
    void loadError(Throwable throwable);
}
