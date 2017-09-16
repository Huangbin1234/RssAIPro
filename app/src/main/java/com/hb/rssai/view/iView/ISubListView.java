package com.hb.rssai.view.iView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/9/17 0017.
 */

public interface ISubListView {
    RecyclerView getRecyclerView();
    LinearLayoutManager getManager();

    SwipeRefreshLayout getSwipeLayout();

    boolean getIsTag();

    LinearLayout getLlEmptyView();
}
