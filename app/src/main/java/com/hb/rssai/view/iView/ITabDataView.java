package com.hb.rssai.view.iView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/12/28.
 */

public interface ITabDataView {
    RecyclerView getRecyclerView();

    SwipeRefreshLayout getSwipeLayout();

    LinearLayoutManager getManager();

    int getDataType();

    LinearLayout getLlLoad();

    View getIncludeNoData();

    View getIncludeLoadFail();

    boolean getIsUser();
}
