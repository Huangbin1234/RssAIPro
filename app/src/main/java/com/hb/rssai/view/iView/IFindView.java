package com.hb.rssai.view.iView;

import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2017/8/15.
 */

public interface IFindView {
    RecyclerView getFfFindRecyclerView();

    RecyclerView getFfTopicRecyclerView();

    RecyclerView getFfHotRecyclerView();

    SwipeRefreshLayout getFfSwipeLayout();

    LinearLayoutManager getFindMoreManager();

    NestedScrollView getNestScrollView();
}
