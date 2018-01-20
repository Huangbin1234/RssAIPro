package com.hb.rssai.view.iView;

import com.hb.rssai.bean.ResBase;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/22 0022.
 */

public interface IRegisterView {
    void toFinish();
    Map<String, String> getParams();
    void setRegResult(ResBase resBase);
    void loadError(Throwable throwable);
}
