package com.hb.rssai.view.iView;

import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/8/18 0018.
 */

public interface ISourceListView {
    String getSubscribeId();

    RecyclerView getRecyclerView();

    SwipeRefreshLayout getSwipeLayout();

    NestedScrollView getNestLayout();

    LinearLayoutManager getManager();

    LinearLayout getLlLoad();
}
