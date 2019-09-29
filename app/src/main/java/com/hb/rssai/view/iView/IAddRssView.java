package com.hb.rssai.view.iView;

import com.hb.rssai.bean.ResTheme;

/**
 * Created by Administrator on 2017/8/15 0015.
 */

public interface IAddRssView {
    String getRssLink();

    String getRssTitle();

    void addSuccess();

    void setListResult(ResTheme resTheme);

    void showMsg(String s);

    int getPage();

    String getUserID();

}
