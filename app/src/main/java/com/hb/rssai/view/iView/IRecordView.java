package com.hb.rssai.view.iView;

import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResInfo;
import com.hb.rssai.bean.ResUserInformation;

/**
 * Created by Administrator on 2017/10/28 0028.
 */

public interface IRecordView {

    void loadError(Throwable throwable);

    void setInfoResult(ResInfo resInfo);

    void setListResult(ResUserInformation resUserInformation);

    String getUserId();

    String getInfoId();

    int getPageNum();

    void showDeleteResult(ResBase resBase);
}
