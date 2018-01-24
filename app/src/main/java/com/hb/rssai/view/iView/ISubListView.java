package com.hb.rssai.view.iView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResFindMore;

/**
 * Created by Administrator on 2017/9/17 0017.
 */

public interface ISubListView {

    Object getIsTag();

    void loadError(Throwable throwable);

    String getUserId();

    void setDelResult(ResBase resBase);

    void setUserSubscribeResult(ResFindMore resFindMore);

    void setUpdateUsSortResult(ResBase resBase);

    ResFindMore.RetObjBean.RowsBean getClickBean();

    int getPageNum();

}
