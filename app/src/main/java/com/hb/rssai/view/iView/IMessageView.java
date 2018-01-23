package com.hb.rssai.view.iView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hb.rssai.bean.ResMessageList;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/18.
 */

public interface IMessageView {

    Map<String, String> getParams(int pageSize,int pageNum);

    void loadError(Throwable throwable);

    void setListResult(ResMessageList resMessageList);

}
