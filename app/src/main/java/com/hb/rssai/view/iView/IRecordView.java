package com.hb.rssai.view.iView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/10/28 0028.
 */

public interface IRecordView {

    RecyclerView getRecyclerView();

    SwipeRefreshLayout getSwipeLayout();

    LinearLayoutManager getManager();

    View getIncludeNoData();

    View getIncludeLoadFail();
}
