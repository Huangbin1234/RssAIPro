package com.hb.rssai.view.iView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hb.rssai.bean.ResInfo;
import com.hb.rssai.bean.ResUserInformation;

import java.util.Map;

/**
 * Created by Administrator on 2017/10/28 0028.
 */

public interface IRecordView {

    RecyclerView getRecyclerView();

    SwipeRefreshLayout getSwipeLayout();

    LinearLayoutManager getManager();

    View getIncludeNoData();

    View getIncludeLoadFail();
    Map<String, String> getParams();
    void loadError(Throwable throwable);

    Map<String,String> getInfoParams();

    void setInfoResult(ResInfo resInfo);

    void setListResult(ResUserInformation resUserInformation);

}
