package com.hb.rssai.presenter;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hb.rssai.adapter.FindMoreAdapter;
import com.hb.rssai.adapter.RecommendAdapter;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IFindView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/15.
 */

public class FindPresenter extends BasePresenter<IFindView> {
    private Context mContext;
    private IFindView iFindView;

    private RecommendAdapter recommendAdapter;
    private FindMoreAdapter findMoreAdapter;

    private RecyclerView mFfTopicRecyclerView;
    private RecyclerView mFfHotRecyclerView;
    private RecyclerView mFfFindRecyclerView;
    private SwipeRefreshLayout swipeLayout;
    private List<ResFindMore.RetObjBean.RowsBean> resFindMores = new ArrayList<>();
    private List<ResFindMore.RetObjBean.RowsBean> resRecommends = new ArrayList<>();
    private LinearLayoutManager findMoreManager;
    private NestedScrollView mNestRefresh;
    private int page = 1;
    private boolean isEnd = false, isLoad = false;

    public FindPresenter(Context mContext, IFindView iFindView) {
        this.mContext = mContext;
        this.iFindView = iFindView;
        initView();
    }

    private void initView() {
        mFfTopicRecyclerView = iFindView.getFfTopicRecyclerView();
        mFfHotRecyclerView = iFindView.getFfHotRecyclerView();
        mFfFindRecyclerView = iFindView.getFfFindRecyclerView();
        swipeLayout = iFindView.getFfSwipeLayout();
        findMoreManager = iFindView.getFindMoreManager();
        mNestRefresh = iFindView.getNestScrollView();

        //TODO 设置下拉刷新
        swipeLayout.setOnRefreshListener(() -> refreshList());
        mNestRefresh.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (v.getChildAt(0) != null && isBottomForNestedScrollView(v, scrollY)) {
                    // 加载更多
                    if (!isEnd && !isLoad) {
                        swipeLayout.setRefreshing(true);
                        page++;
                        findMoreList();
                    }
                }
            }
        });
        //TODO 设置上拉加载更多
//        mFfFindRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            int lastVisibleItem;
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (findMoreAdapter == null) {
//                    isLoad = false;
//                    swipeLayout.setRefreshing(false);
//                    return;
//                }
//                // 在最后两条的时候就自动加载
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= findMoreAdapter.getItemCount()) {
//                    // 加载更多
//                    if (!isEnd && !isLoad) {
//                        swipeLayout.setRefreshing(true);
//                        page++;
//                        findMoreList();
//                    }
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastVisibleItem = findMoreManager.findLastVisibleItemPosition();
//            }
//        });
    }

    // TODO: 判断是不是在底部
    private boolean isBottomForNestedScrollView(NestedScrollView v, int scrollY) {
        return (scrollY + v.getHeight()) == (v.getChildAt(0).getHeight() + v.getPaddingTop() + v.getPaddingBottom());
    }

    /**
     * 刷新数据
     */
    public void refreshList() {
        page = 1;
        isLoad = true;
        isEnd = false;
        if (resFindMores != null) {
            resFindMores.clear();
        }
        swipeLayout.setRefreshing(true);
        findMoreList();
        recommendList();
    }

    public void findMoreList() {
        if (iFindView != null) {
            findApi.findMoreList(getFindMoreParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resFindMore -> {
                        setFindMoreResult(resFindMore);
                    }, this::loadError);
        }
    }

    public void recommendList() {
        if (iFindView != null) {
            findApi.recommendList(getRecommendParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resFindMore -> {
                        setRecommendResult(resFindMore);
                    }, this::loadError);
        }
    }

    private void loadError(Throwable throwable) {
        swipeLayout.setRefreshing(false);
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

    private void setFindMoreResult(ResFindMore resFindMore) {
        isLoad = false;
        swipeLayout.setRefreshing(false);
        //TODO 填充数据
        if (resFindMore.getRetCode() == 0) {
            if (resFindMore.getRetObj().getRows() != null && resFindMore.getRetObj().getRows().size() > 0) {
                resFindMores.addAll(resFindMore.getRetObj().getRows());
                if (findMoreAdapter == null) {
                    findMoreAdapter = new FindMoreAdapter(mContext, resFindMores);
                    mFfFindRecyclerView.setAdapter(findMoreAdapter);
                } else {
                    findMoreAdapter.notifyDataSetChanged();
                }
            }
            if (resFindMores.size() == resFindMore.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else {
            T.ShowToast(mContext, resFindMore.getRetMsg());
        }
    }

    private void setRecommendResult(ResFindMore resFindMore) {
        //TODO 填充数据
        if (resFindMore.getRetCode() == 0) {
            if (resFindMore.getRetObj().getRows() != null && resFindMore.getRetObj().getRows().size() > 0) {
                if (resRecommends.size() > 0) {
                    resRecommends.clear();
                }
                resRecommends = resFindMore.getRetObj().getRows();
                if (recommendAdapter == null) {
                    recommendAdapter = new RecommendAdapter(mContext, resRecommends);
                    mFfHotRecyclerView.setAdapter(recommendAdapter);
                } else {
                    findMoreAdapter.notifyDataSetChanged();
                }
            }
        } else {
            T.ShowToast(mContext, resFindMore.getRetMsg());
        }
    }

    private Map<String, String> getFindMoreParams() {
        Map<String, String> map = new HashMap<>();
        String jsonParams = "{\"page\":\"" + page + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private Map<String, String> getRecommendParams() {
        Map<String, String> map = new HashMap<>();
        String jsonParams = "{\"page\":\"" + page + "\",\"size\":\"" + Constant.RECOMMEND_PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }
}
