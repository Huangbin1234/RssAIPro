package com.hb.rssai.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.hb.rssai.adapter.CollectionAdapter;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResCollection;
import com.hb.rssai.bean.ResInfo;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.common.RichTextActivity;
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
//        if (resCollections != null) {
//            resCollections.clear();
//        }
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

    public void del() {
        collectionApi.del(getDelParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setDelResult(resBase);
                }, this::loadError);
    }

    private Map<String, String> getDelParams() {
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String id = iCollectionView.getCollectionId();
        String jsonParams = "{\"userId\":\"" + userId + "\",\"id\":\"" + id + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private void setDelResult(ResBase resBase) {
        if (resBase.getRetCode() == 0) {
            //TODO 确认删除
            refreshList();
        } else {
            T.ShowToast(mContext, resBase.getRetMsg());
        }
    }

    private Map<String, String> getListParams() {
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String jsonParams = "{\"userId\":\"" + userId + "\",\"page\":\"" + page + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private ResCollection.RetObjBean.RowsBean bean;
    private void setListResult(ResCollection resCollection) {
        if (resCollections != null && page == 1) {
            resCollections.clear();
        }
        isLoad = false;
        swipeRefreshLayout.setRefreshing(false);
        //TODO 填充数据
        if (resCollection.getRetCode() == 0) {
            iCollectionView.getIncludeNoData().setVisibility(View.GONE);
            iCollectionView.getIncludeLoadFail().setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            if (resCollection.getRetObj().getRows() != null && resCollection.getRetObj().getRows().size() > 0) {
                resCollections.addAll(resCollection.getRetObj().getRows());
                if (adapter == null) {
                    adapter = new CollectionAdapter(mContext, resCollections);
                    recyclerView.setAdapter(adapter);
                    adapter.setMyOnItemClickedListener(new CollectionAdapter.MyOnItemClickedListener() {
                        @Override
                        public void onItemClicked(ResCollection.RetObjBean.RowsBean rowsBean) {
                            bean=rowsBean;
                            if (!TextUtils.isEmpty(rowsBean.getInformationId())) {
                                infoId = rowsBean.getInformationId();
                                getInformation(); //获取消息
                            } else {
                                Intent intent = new Intent(mContext, ContentActivity.class);
                                intent.putExtra(ContentActivity.KEY_URL, rowsBean.getLink());
                                intent.putExtra(ContentActivity.KEY_TITLE, rowsBean.getTitle());
                                intent.putExtra(ContentActivity.KEY_INFORMATION_ID, rowsBean.getInformationId());
                                mContext.startActivity(intent);
//                                T.ShowToast(mContext, "抱歉，文章链接已失效，无法打开！");
                            }
                        }
                    });
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
            if (resCollections.size() == resCollection.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else if (resCollection.getRetCode() == 10013) {//暂无数据
            iCollectionView.getIncludeNoData().setVisibility(View.VISIBLE);
            iCollectionView.getIncludeLoadFail().setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        } else {
            iCollectionView.getIncludeNoData().setVisibility(View.GONE);
            iCollectionView.getIncludeLoadFail().setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            T.ShowToast(mContext, resCollection.getRetMsg());
        }
    }


    private void loadError(Throwable throwable) {
        iCollectionView.getIncludeLoadFail().setVisibility(View.VISIBLE);
        iCollectionView.getIncludeNoData().setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        swipeRefreshLayout.setRefreshing(false);
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

    public void getInformation() {
        informationApi.getInformation(getInfoParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInfo -> {
                    setInfoResult(resInfo);
                }, this::loadError);
    }

    private void setInfoResult(ResInfo resInfo) {
        if (resInfo.getRetCode() == 0) {
            Intent intent = new Intent(mContext, RichTextActivity.class);
            intent.putExtra("abstractContent", resInfo.getRetObj().getAbstractContent());
            intent.putExtra(ContentActivity.KEY_TITLE, resInfo.getRetObj().getTitle());
            intent.putExtra("whereFrom", resInfo.getRetObj().getWhereFrom());
            intent.putExtra("pubDate", resInfo.getRetObj().getPubTime());
            intent.putExtra("url", resInfo.getRetObj().getLink());
            intent.putExtra("id", resInfo.getRetObj().getId());
            intent.putExtra("clickGood", resInfo.getRetObj().getClickGood());
            intent.putExtra("clickNotGood", resInfo.getRetObj().getClickNotGood());
            mContext.startActivity(intent);
        } else {
//            T.ShowToast(mContext, "抱歉，文章链接已失效，无法打开！");
            Intent intent = new Intent(mContext, ContentActivity.class);
            intent.putExtra(ContentActivity.KEY_URL, bean.getLink());
            intent.putExtra(ContentActivity.KEY_TITLE, bean.getTitle());
            intent.putExtra(ContentActivity.KEY_INFORMATION_ID, bean.getInformationId());
            mContext.startActivity(intent);
        }
    }

    private String infoId = "";

    private Map<String, String> getInfoParams() {
        Map<String, String> map = new HashMap<>();
        String informationId = infoId;
        String jsonParams = "{\"informationId\":\"" + informationId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }
}
