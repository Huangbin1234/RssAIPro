package com.hb.rssai.view.iView;

import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.hb.rssai.view.widget.FullyGridLayoutManager;

/**
 * Created by Administrator on 2017/8/15 0015.
 */

public interface ISubscriptionView {

    RecyclerView getRecyclerView();

    SwipeRefreshLayout getSwipeLayout();

    FullyGridLayoutManager getManager();

    Fragment getFragment();

    String getUsId();

    void update();

    NestedScrollView getNestScrollView();
}
