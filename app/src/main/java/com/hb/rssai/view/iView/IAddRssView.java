package com.hb.rssai.view.iView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hb.rssai.view.widget.FullyGridLayoutManager;

/**
 * Created by Administrator on 2017/8/15 0015.
 */

public interface IAddRssView {
    String getRssLink();

    String getRssTitle();

    RecyclerView getRecyclerView();

    SwipeRefreshLayout getSwipeLayout();

    FullyGridLayoutManager getManager();

    View getIncludeNoData();

    View getIncludeLoadFail();

    void showPop(int i,String title);
}
