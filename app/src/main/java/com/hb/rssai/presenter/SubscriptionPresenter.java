package com.hb.rssai.presenter;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.hb.rssai.adapter.RssSourceAdapter;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.ISubscriptionView;
import com.hb.rssai.view.widget.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/15 0015.
 */

public class SubscriptionPresenter extends BasePresenter<ISubscriptionView> {
    private ISubscriptionView mISubscriptionView;
    private Context mContext;
    private int page = 1;
    private boolean isEnd = false, isLoad = false;
    private List<ResFindMore.RetObjBean.RowsBean> resFindMores = new ArrayList<>();
    private List<ResFindMore.RetObjBean.RowsBean> resTopMores = new ArrayList<>();

    private RecyclerView subscribeRecyclerView;
    private RecyclerView topicRecyclerView;
    private SwipeRefreshLayout swipeLayout;
    private FullyGridLayoutManager subscribeManager;
    private RssSourceAdapter adapter;
    private NestedScrollView mNestRefresh;
    private RssSourceAdapter topicAdapter;


    public SubscriptionPresenter(Context context, ISubscriptionView ISubscriptionView) {
        mISubscriptionView = ISubscriptionView;
        mContext = context;
        initView();
    }

    private void initView() {
        subscribeRecyclerView = mISubscriptionView.getRecyclerView();
        topicRecyclerView = mISubscriptionView.getTopicRecyclerView();
        swipeLayout = mISubscriptionView.getSwipeLayout();
        subscribeManager = mISubscriptionView.getManager();
        mNestRefresh = mISubscriptionView.getNestScrollView();

        //TODO 设置下拉刷新
        swipeLayout.setOnRefreshListener(() -> refreshList());
//        mNestRefresh.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//                if (v.getChildAt(0) != null && isBottomForNestedScrollView(v, scrollY)) {
//                    // 加载更多
//                    if (!isEnd && !isLoad) {
//                        swipeLayout.setRefreshing(true);
//                        page++;
//                        getUserSubscribeList();
//                    }
//                }
//            }
//        });
        //TODO 设置上拉加载更多
//        subscribeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            int lastVisibleItem;
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (adapter == null) {
//                    isLoad = false;
//                    swipeLayout.setRefreshing(false);
//                    return;
//                }
//                // 在最后两条的时候就自动加载
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= adapter.getItemCount()) {
//                    // 加载更多
//                    if (!isEnd && !isLoad) {
//                        swipeLayout.setRefreshing(true);
//                        page++;
//                        getUserSubscribeList();
//                    }
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastVisibleItem = subscribeManager.findLastVisibleItemPosition();
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
        } if (resTopMores != null) {
            resTopMores.clear();
        }
        swipeLayout.setRefreshing(true);
        getUserSubscribeList();
        getSubscribeList();
    }

    public void getUserSubscribeList() {
        findApi.userSubscribeList(getUserSubscribeParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resFindMore -> {
                    setUserSubscribeResult(resFindMore);
                }, this::loadError);
    }

    public void getSubscribeList() {
        findApi.userSubscribeList(getSubscribeParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resFindMore -> {
                    setSubscribeResult(resFindMore);
                }, this::loadError);
    }

    public void delSubscription() {
        findApi.delSubscription(getDelParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setDelResult(resBase);
                }, this::loadError);
    }

    private void setDelResult(ResBase resBase) {
        mISubscriptionView.update();
        T.ShowToast(mContext, resBase.getRetMsg());
    }

    private Map<String, String> getDelParams() {
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
//        String usId = mISubscriptionView.getUsId();
        String subscribeId = mISubscriptionView.getSubscribeId();
        String jsonParams = "{\"subscribeId\":\"" + subscribeId + "\",\"usId\":\"" + userId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }


    private Map<String, String> getUserSubscribeParams() {
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String jsonParams = "{\"userId\":\"" + userId + "\",\"isTag\":\"" + true + "\",\"page\":\"" + page + "\",\"size\":\"" + Constant.SUBSCRIBE_PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private Map<String, String> getSubscribeParams() {
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String jsonParams = "{\"userId\":\"" + userId + "\",\"isTag\":\"" + false + "\",\"page\":\"" + page + "\",\"size\":\"" + Constant.SUBSCRIBE_PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private void setUserSubscribeResult(ResFindMore resFindMore) {
        isLoad = false;
        swipeLayout.setRefreshing(false);
        //TODO 填充数据
        if (resFindMore.getRetCode() == 0) {
            if (resFindMore.getRetObj().getRows() != null && resFindMore.getRetObj().getRows().size() > 0) {
                resFindMores.addAll(resFindMore.getRetObj().getRows());
                if (adapter == null) {
                    adapter = new RssSourceAdapter(mContext, resFindMores, mISubscriptionView.getFragment());
                    subscribeRecyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
            if (resFindMores.size() == resFindMore.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else {
            T.ShowToast(mContext, resFindMore.getRetMsg());
        }
    }

    private void setSubscribeResult(ResFindMore resFindMore) {
        isLoad = false;
        swipeLayout.setRefreshing(false);
        //TODO 填充数据
        if (resFindMore.getRetCode() == 0) {
            if (resFindMore.getRetObj().getRows() != null && resFindMore.getRetObj().getRows().size() > 0) {
                resTopMores.addAll(resFindMore.getRetObj().getRows());
                if (topicAdapter == null) {
                    topicAdapter = new RssSourceAdapter(mContext, resTopMores, mISubscriptionView.getFragment());
                    topicRecyclerView.setAdapter(topicAdapter);
                } else {
                    topicAdapter.notifyDataSetChanged();
                }
            }
            if (resTopMores.size() == resFindMore.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else {
            T.ShowToast(mContext, resFindMore.getRetMsg());
        }
    }


    private void loadError(Throwable throwable) {
        swipeLayout.setRefreshing(false);
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

}
