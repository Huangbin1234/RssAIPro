package com.hb.rssai.view.iView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/8/15.
 */

public interface ICollectionView {
    RecyclerView getRecyclerView();

    SwipeRefreshLayout getSwipeLayout();

    LinearLayoutManager getManager();

    String getCollectionId();

    View getIncludeNoData();

    View getIncludeLoadFail();
}
