package com.hb.rssai.view.iView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/8/17 0017.
 */

public interface IInformationView {

    RecyclerView getRecyclerView();

    SwipeRefreshLayout getSwipeLayout();

    LinearLayoutManager getManager();

    LinearLayout getLlLoad();

    int getDataType();

    boolean getIsUser();
}
