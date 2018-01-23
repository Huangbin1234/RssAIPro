package com.hb.rssai.view.iView;

import com.hb.rssai.bean.ResDataGroup;

/**
 * Created by Administrator on 2017/10/3 0003.
 */

public interface IOfficeView {

    void loadError(Throwable throwable);

    void setDataGroupResult(ResDataGroup resDataGroup);
}
