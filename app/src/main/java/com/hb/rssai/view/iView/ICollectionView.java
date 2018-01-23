package com.hb.rssai.view.iView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResCollection;
import com.hb.rssai.bean.ResInfo;

import java.util.Map;

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

    Map<String,String> getListParams();

    void loadError(Throwable throwable);

    void setDelResult(ResBase resBase);

    Map<String,String> getDelParams();

    void setListResult(ResCollection resCollection);

    Map<String,String> getInfoParams();

    void setInfoResult(ResInfo resInfo);

}
