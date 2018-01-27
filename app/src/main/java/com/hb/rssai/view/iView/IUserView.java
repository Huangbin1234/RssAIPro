package com.hb.rssai.view.iView;


import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResUser;

/**
 * Created by Administrator on 2017/9/23 0023.
 */

public interface IUserView {

    String getFilePath();

    void setUserInfoResult(ResUser resUser);

    void setUpdateResult(ResUser resBase);

    void setAvatarResult(ResBase resBase);

    void loadError(Throwable throwable);

    String getUserId();

    String getSex();

    String getBirth();

    String getNickName();

    String getEtType();

    String getEmail();
}
