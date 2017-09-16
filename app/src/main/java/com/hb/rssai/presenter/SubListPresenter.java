package com.hb.rssai.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hb.rssai.adapter.SubListAdapter;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.ISubListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/17 0017.
 */

public class SubListPresenter extends BasePresenter<ISubListView> {
    private Context mContext;
    private ISubListView mISubListView;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private List<ResFindMore.RetObjBean.RowsBean> resLists = new ArrayList<>();


    private int page = 1;
    private boolean isEnd = false, isLoad = false;
    private SwipeRefreshLayout mSwipeLayout;
    private SubListAdapter mSubListAdapter;
    private LinearLayout mSubLl;

    public SubListPresenter(Context context, ISubListView ISubListView) {
        mContext = context;
        mISubListView = ISubListView;
        initView();
    }

    private void initView() {
        mRecyclerView = mISubListView.getRecyclerView();
        mLayoutManager = mISubListView.getManager();
        mSwipeLayout = mISubListView.getSwipeLayout();
        mSubLl = mISubListView.getLlEmptyView();

        //TODO 设置下拉刷新
        mSwipeLayout.setOnRefreshListener(() -> refreshList());
        //TODO 设置上拉加载更多
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mSubListAdapter == null) {
                    isLoad = false;
                    mSwipeLayout.setRefreshing(false);
                    return;
                }
                // 在最后两条的时候就自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= mSubListAdapter.getItemCount()) {
                    // 加载更多
                    if (!isEnd && !isLoad) {
                        mSwipeLayout.setRefreshing(true);
                        page++;
                        getUserSubscribeList();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
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
        if (resLists != null) {
            resLists.clear();
        }
        mSwipeLayout.setRefreshing(true);
        getUserSubscribeList();
    }

    public void getUserSubscribeList() {
        findApi.userSubscribeList(getUserSubscribeParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resFindMore -> {
                    setUserSubscribeResult(resFindMore);
                }, this::loadError);
    }

    private void setUserSubscribeResult(ResFindMore resFindMore) {
        isLoad = false;
        mSwipeLayout.setRefreshing(false);
        mSubLl.setVisibility(View.GONE);
        //TODO 填充数据
        if (resFindMore.getRetCode() == 0) {
            if (resFindMore.getRetObj().getRows() != null && resFindMore.getRetObj().getRows().size() > 0) {
                resLists.addAll(resFindMore.getRetObj().getRows());
                if (mSubListAdapter == null) {
                    mSubListAdapter = new SubListAdapter(mContext, resLists, (Activity) mContext);
                    mRecyclerView.setAdapter(mSubListAdapter);
                } else {
                    mSubListAdapter.notifyDataSetChanged();
                }
            }
            if (resLists.size() == resFindMore.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else {
            T.ShowToast(mContext, resFindMore.getRetMsg());
        }
    }

    private void loadError(Throwable throwable) {
        mSwipeLayout.setRefreshing(false);
        mSubLl.setVisibility(View.GONE);
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

    private Map<String, String> getUserSubscribeParams() {
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        boolean isTag = mISubListView.getIsTag();
        String jsonParams = "{\"userId\":\"" + userId + "\",\"isTag\":\"" + isTag + "\",\"page\":\"" + page + "\",\"size\":\"" + Constant.SUBSCRIBE_PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }
}
