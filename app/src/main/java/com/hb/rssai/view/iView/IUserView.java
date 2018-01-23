package com.hb.rssai.view.iView;


import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResUser;

import java.util.Map;

/**
 * Created by Administrator on 2017/9/23 0023.
 */

public interface IUserView {

    String getFilePath();

    Map<String, String> getParams();

    Map<String, String> getUpdateParams();

    void setUserInfoResult(ResUser resUser);

    void setUpdateResult(ResUser resBase);

    void setAvatarResult(ResBase resBase);

    void loadError(Throwable throwable);
}
