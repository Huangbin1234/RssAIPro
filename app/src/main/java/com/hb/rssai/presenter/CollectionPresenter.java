package com.hb.rssai.presenter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hb.rssai.adapter.CollectionAdapter;
import com.hb.rssai.bean.ResCollection;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.ICollectionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/15.
 */

public class CollectionPresenter extends BasePresenter<ICollectionView> {
    private Context mContext;
    private ICollectionView iCollectionView;
    private CollectionAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;

    private int page = 1;
    private boolean isEnd = false, isLoad = false;
    private List<ResCollection.RetObjBean.RowsBean> resCollections = new ArrayList<>();

    public CollectionPresenter(Context mContext, ICollectionView iCollectionView) {
        this.mContext = mContext;
        this.iCollectionView = iCollectionView;
        initView();
    }


    private void initView() {
        recyclerView = iCollectionView.getRecyclerView();
        swipeRefreshLayout = iCollectionView.getSwipeLayout();
        linearLayoutManager = iCollectionView.getManager();

        swipeRefreshLayout.setOnRefreshListener(() -> refreshList());
        //TODO 设置上拉加载更多
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (adapter == null) {
                    isLoad = false;
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }
                // 在最后两条的时候就自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= adapter.getItemCount()) {
                    // 加载更多
                    if (!isEnd && !isLoad) {
                        swipeRefreshLayout.setRefreshing(true);
                        page++;
                        getList();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
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
        if (resCollections != null) {
            resCollections.clear();
        }
        swipeRefreshLayout.setRefreshing(true);
        getList();
    }

    public void getList() {
        collectionApi.list(getListParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resCollection -> {
                    setListResult(resCollection);
                }, this::loadError);
    }

    private Map<String, String> getListParams() {
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String jsonParams = "{\"userId\":\"" + userId + "\",\"page\":\"" + page + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }


    private void setListResult(ResCollection resCollection) {
        isLoad = false;
        swipeRefreshLayout.setRefreshing(false);
        //TODO 填充数据
        if (resCollection.getRetCode() == 0) {
            if (resCollection.getRetObj().getRows() != null && resCollection.getRetObj().getRows().size() > 0) {
                resCollections.addAll(resCollection.getRetObj().getRows());
                if (adapter == null) {
                    adapter = new CollectionAdapter(mContext, resCollections);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
            if (resCollections.size() == resCollection.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else {
            T.ShowToast(mContext, resCollection.getRetMsg());
        }
    }


    private void loadError(Throwable throwable) {
        swipeRefreshLayout.setRefreshing(false);
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }
}
