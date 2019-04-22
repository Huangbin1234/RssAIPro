package com.hb.rssai.presenter;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.hb.rssai.adapter.RssSourceAdapter;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.RssSourceEvent;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.ISubscriptionView;
import com.hb.rssai.view.widget.FullyGridLayoutManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
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
    private List<ResFindMore.RetObjBean.RowsBean> userSubscribes = new ArrayList<>();
    private List<ResFindMore.RetObjBean.RowsBean> officeSuscribes = new ArrayList<>();

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
//        if (userSubscribes != null) {
//            userSubscribes.clear();
//        }
//        if (officeSuscribes != null) {
//            officeSuscribes.clear();
//        }
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

    public void updateUsSort() {
        findApi.updateUsSort(getUpdateParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setUpdateUsSortResult(resBase);
                }, this::loadError);
    }

    private void setUpdateUsSortResult(ResBase resBase) {
        if (resBase.getRetCode() == 0) {
            EventBus.getDefault().post(new RssSourceEvent(0));
        }
        T.ShowToast(mContext, resBase.getRetMsg());
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

    private Map<String, String> getUpdateParams() {
        Map<String, String> map = new HashMap<>();
        String usId = mISubscriptionView.getUsId();
        String jsonParams = "{\"usId\":\"" + usId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private Map<String, String> getDelParams() {
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
//        String usId = mISubscriptionView.getUserId();
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
        if (userSubscribes != null && page == 1) {
            userSubscribes.clear();
        }
        if (null == resFindMore.getRetObj() || resFindMore.getRetObj().getRows() == null || resFindMore.getRetObj().getRows().size() <= 0) {
            if (null != adapter) {
                userSubscribes.clear();
                adapter.notifyDataSetChanged();
            }
        }
        //TODO 填充数据
        if (resFindMore.getRetCode() == 0) {
            if (resFindMore.getRetObj().getRows() != null && resFindMore.getRetObj().getRows().size() > 0) {
                userSubscribes.addAll(resFindMore.getRetObj().getRows());
                if (adapter == null) {
                    adapter = new RssSourceAdapter(mContext, userSubscribes, mISubscriptionView.getFragment());
                    subscribeRecyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
            if (userSubscribes.size() == resFindMore.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else if (resFindMore.getRetCode() == 10013) {
//            if (officeSuscribes == null || officeSuscribes.size() <= 0) {
//                T.ShowToast(mContext, resFindMore.getRetMsg());
//            }
        } else {
            T.ShowToast(mContext, resFindMore.getRetMsg());
        }

        isLoad = false;
        swipeLayout.setRefreshing(false);
    }

    private void setSubscribeResult(ResFindMore resFindMore) {
        if (officeSuscribes != null && page == 1) {
            officeSuscribes.clear();
        }

        if (null == resFindMore.getRetObj() || resFindMore.getRetObj().getRows() == null || resFindMore.getRetObj().getRows().size() <= 0) {
            if (null != topicAdapter) {
                officeSuscribes.clear();
                topicAdapter.notifyDataSetChanged();
            }
        }
        //TODO 填充数据
        if (resFindMore.getRetCode() == 0) {
            if (resFindMore.getRetObj().getRows() != null && resFindMore.getRetObj().getRows().size() > 0) {
                officeSuscribes.addAll(resFindMore.getRetObj().getRows());
                if (topicAdapter == null) {
                    topicAdapter = new RssSourceAdapter(mContext, officeSuscribes, mISubscriptionView.getFragment());
                    topicRecyclerView.setAdapter(topicAdapter);
                } else {
                    topicAdapter.notifyDataSetChanged();
                }
            }
            if (officeSuscribes.size() == resFindMore.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else if (resFindMore.getRetCode() == 10013) {
//            if (userSubscribes == null || userSubscribes.size() <= 0) {
//                T.ShowToast(mContext, resFindMore.getRetMsg());
//            }
        } else {
            T.ShowToast(mContext, resFindMore.getRetMsg());
        }
        isLoad = false;
        swipeLayout.setRefreshing(false);
    }

    private void loadError(Throwable throwable) {
        swipeLayout.setRefreshing(false);
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            if (((HttpException) throwable).response().code() == 401) {
                T.ShowToast(mContext, Constant.MSG_NO_LOGIN);
            } else {
                T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
            }
        } else {
            T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
        }
    }
}
