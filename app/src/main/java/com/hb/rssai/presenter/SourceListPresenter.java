package com.hb.rssai.presenter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hb.rssai.adapter.SourceListAdapter;
import com.hb.rssai.adapter.SourceListNewAdapter;
import com.hb.rssai.bean.ResCardSubscribe;
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.ISourceListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/18 0018.
 */

public class SourceListPresenter extends BasePresenter<ISourceListView> {
    private Context mContext;
    private ISourceListView iSourceListView;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private LinearLayout mLlLoad;

    private int page = 1;
    private boolean isEnd = false, isLoad = false;
    private SourceListAdapter adapter;
    List<ResInformation.RetObjBean.RowsBean> infoList = new ArrayList<>();
    List<List<ResCardSubscribe.RetObjBean.RowsBean>> infoListCard = new ArrayList<>();
    private SourceListNewAdapter cardAdapter;

    public SourceListPresenter(Context context, ISourceListView iSourceListView) {
        mContext = context;
        this.iSourceListView = iSourceListView;
        initView();
    }

    private void initView() {
        mRecyclerView = iSourceListView.getRecyclerView();
        mSwipeRefreshLayout = iSourceListView.getSwipeLayout();
        mLinearLayoutManager = iSourceListView.getManager();
        mLlLoad = iSourceListView.getLlLoad();

        mSwipeRefreshLayout.setOnRefreshListener(() -> refreshList());
        //TODO 设置上拉加载更多
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (cardAdapter == null) {
                    isLoad = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                }
                // 在最后两条的时候就自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= cardAdapter.getItemCount()) {
                    // 加载更多
                    if (!isEnd && !isLoad) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        page++;
//                        getListById();
                        getListCardById();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    /**
     * 刷新数据
     */
    public void refreshList() {
        page = 1;
        isLoad = true;
        isEnd = false;
        if (infoList != null) {
            infoList.clear();
        }
        if (infoListCard != null) {
            infoListCard.clear();
        }
        mSwipeRefreshLayout.setRefreshing(true);
//        getListById();
        getListCardById();
    }

    public void getListById() {
        informationApi.getListById(getListParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInformation -> {
                    setListResult(resInformation);
                }, this::loadError);
    }

    public void getListCardById() {
        informationApi.listCardById(getListParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resCardSubscribe -> {
                    setListCardResult(resCardSubscribe);
                }, this::loadError);
    }

    private void setListCardResult(ResCardSubscribe resCardSubscribe) {
        //TODO 填充数据
        mLlLoad.setVisibility(View.GONE);
        isLoad = false;
        mSwipeRefreshLayout.setRefreshing(false);
        //TODO 填充数据
        if (resCardSubscribe.getRetCode() == 0) {
            if (resCardSubscribe.getRetObj().getRows() != null && resCardSubscribe.getRetObj().getRows().size() > 0) {
                infoListCard.addAll(resCardSubscribe.getRetObj().getRows());
                if (cardAdapter == null) {
                    cardAdapter = new SourceListNewAdapter(mContext, infoListCard);
                    mRecyclerView.setAdapter(cardAdapter);
                } else {
                    cardAdapter.notifyDataSetChanged();
                }
            }
            if (infoListCard.size() == resCardSubscribe.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else {
            T.ShowToast(mContext, resCardSubscribe.getRetMsg());
        }
    }

    private Map<String, String> getListParams() {
        Map<String, String> map = new HashMap<>();
        String subscribeId = iSourceListView.getSubscribeId();
        String jsonParams = "{\"subscribeId\":\"" + subscribeId + "\",\"page\":\"" + page + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private void loadError(Throwable throwable) {
        mLlLoad.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

    private void setListResult(ResInformation resInformation) {
        //TODO 填充数据
        mLlLoad.setVisibility(View.GONE);
        isLoad = false;
        mSwipeRefreshLayout.setRefreshing(false);
        //TODO 填充数据
        if (resInformation.getRetCode() == 0) {
            if (resInformation.getRetObj().getRows() != null && resInformation.getRetObj().getRows().size() > 0) {
                infoList.addAll(resInformation.getRetObj().getRows());
                if (adapter == null) {
                    adapter = new SourceListAdapter(mContext, infoList);
                    mRecyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
            if (infoList.size() == resInformation.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else {
            T.ShowToast(mContext, resInformation.getRetMsg());
        }
    }
}
