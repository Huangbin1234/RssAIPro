package com.hb.rssai.presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hb.rssai.adapter.SearchSubscribeAdapter;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.ISearchSubscribeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/22.
 */

public class SearchSubscribePresenter extends BasePresenter {
    private Context mContext;
    private ISearchSubscribeView iSearchSubscribeView;
    private SearchSubscribeAdapter mAdapter;

    private List<ResFindMore.RetObjBean.RowsBean> resSubscribes = new ArrayList<>();
    private int page = 1;
    private boolean isEnd = false, isLoad = false;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView subscribeRecyclerView;

    public SearchSubscribePresenter(Context mContext, ISearchSubscribeView iSearchSubscribeView) {
        this.mContext = mContext;
        this.iSearchSubscribeView = iSearchSubscribeView;
        initView();
    }

    private void initView() {
        subscribeRecyclerView = iSearchSubscribeView.getSubscribeRecyclerView();
        mLinearLayoutManager = iSearchSubscribeView.getSubscribeManager();

        //TODO 设置上拉加载更多
        subscribeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mAdapter == null) {
                    isLoad = false;
                    return;
                }
                // 在最后两条的时候就自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= mAdapter.getItemCount()) {
                    // 加载更多
                    if (!isEnd && !isLoad) {
                        page++;
                        getSubscribeLike();
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
    private String keyWord = "";

    public void refreshInfoList(String val) {
        keyWord = val;
        page = 1;
        isLoad = true;
        isEnd = false;
        if (resSubscribes != null) {
            resSubscribes.clear();
        }
        getSubscribeLike();
    }

    public void getSubscribeLike() {
        findApi.getLikeByName(getSubscribeParams()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resFindMore -> {
                    setSubscribeLikeResult(resFindMore);
                }, this::loadError);
    }

    private Map<String, String> getSubscribeParams() {
        Map<String, String> map = new HashMap<>();
        String name = keyWord;
        String jsonParams = "{\"name\":\"" + name + "\",\"page\":\"" + page + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    public void setSubscribeLikeResult(ResFindMore resFindMore) {
        isLoad = false;
        if (resFindMore.getRetCode() == 0) {
            if (resFindMore.getRetObj().getRows() != null && resFindMore.getRetObj().getRows().size() > 0) {
                resSubscribes.addAll(resFindMore.getRetObj().getRows());
                if (mAdapter == null) {
                    mAdapter = new SearchSubscribeAdapter(mContext, resSubscribes);
                    mAdapter.setOnItemClickedListener(rowsBean1 -> {
                        //TODO
//                        Intent intent = new Intent(mContext, RichTextActivity.class);//创建Intent对象
//                        intent.putExtra(ContentActivity.KEY_TITLE, rowsBean1.getTitle());
//                        intent.putExtra(ContentActivity.KEY_URL, rowsBean1.getLink());
//                        intent.putExtra(ContentActivity.KEY_INFORMATION_ID, rowsBean1.getId());
//                        intent.putExtra("pubDate", rowsBean1.getPubTime());
//                        intent.putExtra("whereFrom", rowsBean1.getWhereFrom());
//                        intent.putExtra("abstractContent", rowsBean1.getAbstractContent());
//                        intent.putExtra("id", rowsBean1.getId());
//                        mContext.startActivity(intent);//将Intent传递给Activity
                    });
                    subscribeRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
                if (resSubscribes.size() == resFindMore.getRetObj().getTotal()) {
                    isEnd = true;
                }
            }
        } else {
            T.ShowToast(mContext, resFindMore.getRetMsg());
        }
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }
}
