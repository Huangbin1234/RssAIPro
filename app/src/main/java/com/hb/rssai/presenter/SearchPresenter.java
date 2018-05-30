package com.hb.rssai.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hb.rssai.adapter.LikeAdapter;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.common.RichTextActivity;
import com.hb.rssai.view.iView.ISearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/22.
 */

public class SearchPresenter extends BasePresenter<ISearchView> {
    private Context mContext;
    private ISearchView iSearchView;
    private ResInformation likeByTitleResult;
    private ResBase likeByNameResult;
    private LikeAdapter likeAdapter;

    private RecyclerView infoRecyclerView;
    private RecyclerView subscribeRecyclerView;
    private List<ResInformation.RetObjBean.RowsBean> resInfos = new ArrayList<>();

    private int page = 1;
    private boolean isEnd = false, isLoad = false;
    private LinearLayoutManager mLinearLayoutManager;

    public SearchPresenter(Context mContext, ISearchView iSearchView) {
        this.mContext = mContext;
        this.iSearchView = iSearchView;
        initView();
    }

    private void initView() {
        infoRecyclerView = iSearchView.getInfoRecyclerView();
        subscribeRecyclerView = iSearchView.getSubscribeRecyclerView();
        mLinearLayoutManager = iSearchView.getInfoManager();

        //TODO 设置上拉加载更多
        infoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (likeAdapter == null) {
                    isLoad = false;
                    return;
                }
                // 在最后两条的时候就自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= likeAdapter.getItemCount()) {
                    // 加载更多
                    if (!isEnd && !isLoad) {
                        page++;
                        getInfoLike();
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

    public void getInfoLike() {
        informationApi.getSearchInfo(getInfoParams()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInfo -> {
                    setInfoLikeResult(resInfo);
                }, this::loadError);
    }

    private Map<String, String> getInfoParams() {
        Map<String, String> map = new HashMap<>();
        String title = iSearchView.getKeyWords();
        String jsonParams = "{\"title\":\"" + title + "\",\"page\":\"" + page + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

    public void setInfoLikeResult(ResInformation resInformation) {
        isLoad = false;
        if (resInformation.getRetCode() == 0) {
            if (resInformation.getRetObj().getRows() != null && resInformation.getRetObj().getRows().size() > 0) {
                resInfos.addAll(resInformation.getRetObj().getRows());
                if (likeAdapter == null) {
                    likeAdapter = new LikeAdapter(mContext, resInfos);
                    likeAdapter.setOnItemClickedListener(rowsBean1 -> {
                        //TODO
                        Intent intent = new Intent(mContext, RichTextActivity.class);//创建Intent对象
                        intent.putExtra(ContentActivity.KEY_TITLE, rowsBean1.getTitle());
                        intent.putExtra(ContentActivity.KEY_URL, rowsBean1.getLink());
                        intent.putExtra(ContentActivity.KEY_INFORMATION_ID, rowsBean1.getId());
                        intent.putExtra("pubDate", rowsBean1.getPubTime());
                        intent.putExtra("whereFrom", rowsBean1.getWhereFrom());
                        intent.putExtra("abstractContent", rowsBean1.getAbstractContent());
                        intent.putExtra("id", rowsBean1.getId());
                        mContext.startActivity(intent);//将Intent传递给Activity
                    });
                    infoRecyclerView.setAdapter(likeAdapter);
                } else {
                    likeAdapter.notifyDataSetChanged();
                }
                if (resInfos.size() == resInformation.getRetObj().getTotal()) {
                    isEnd = true;
                }
            }
        } else {
            T.ShowToast(mContext, resInformation.getRetMsg());
        }
    }

}
