package com.hb.rssai.presenter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hb.rssai.adapter.MessageAdapter;
import com.hb.rssai.bean.ResMessageList;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IMessageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/18.
 */

public class MessagePresenter extends BasePresenter<IMessageView> {
    private Context mContext;
    private IMessageView iMessageView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;

    private int page = 1;
    private boolean isEnd = false, isLoad = false;
    private List<ResMessageList.RetObjBean.RowsBean> mMessages = new ArrayList<>();
    private MessageAdapter adapter;

    public MessagePresenter(Context mContext, IMessageView iMessageView) {
        this.mContext = mContext;
        this.iMessageView = iMessageView;
        initView();
    }

    private void initView() {
        recyclerView = iMessageView.getRecyclerView();
        swipeRefreshLayout = iMessageView.getSwipeLayout();
        linearLayoutManager = iMessageView.getManager();

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
        if (mMessages != null) {
            mMessages.clear();
        }
        swipeRefreshLayout.setRefreshing(true);
        getList();
    }

    public void getList() {
        messageApi.list(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resMessageList -> {
                    setListResult(resMessageList);
                }, this::loadError);
    }


    private void setListResult(ResMessageList resMessageList) {
        isLoad = false;
        swipeRefreshLayout.setRefreshing(false);
        //TODO 填充数据
        if (resMessageList.getRetCode() == 0) {
            if (resMessageList.getRetObj().getRows() != null && resMessageList.getRetObj().getRows().size() > 0) {
                this.mMessages.addAll(resMessageList.getRetObj().getRows());
                if (adapter == null) {
                    adapter = new MessageAdapter(mContext, mMessages);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
            if (this.mMessages.size() == resMessageList.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else {
            T.ShowToast(mContext, resMessageList.getRetMsg());
        }
    }

    private void loadError(Throwable throwable) {
        swipeRefreshLayout.setRefreshing(false);
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

    private Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String jsonParams = "{\"userId\":\"" + userId + "\",\"page\":\"" + page + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }
}
