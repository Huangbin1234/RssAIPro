package com.hb.rssai.view.iView;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2017/9/22.
 */

public interface ISearchInfoView {

    String getKeyWords();

    RecyclerView getInfoRecyclerView();

    LinearLayoutManager getInfoManager();
}
