package com.hb.rssai.view.iView;

import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.hb.rssai.bean.ResCardSubscribe;

/**
 * Created by Administrator on 2017/8/18 0018.
 */

public interface ISourceListView {
    String getSubscribeId();

    void loadError(Throwable throwable);

    void setListCardResult(ResCardSubscribe resCardSubscribe);

    int getPageNum();
}
