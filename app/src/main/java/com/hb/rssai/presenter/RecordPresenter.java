package com.hb.rssai.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.hb.rssai.adapter.RecordAdapter;
import com.hb.rssai.bean.ResCollection;
import com.hb.rssai.bean.ResInfo;
import com.hb.rssai.bean.ResUserInformation;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.common.RichTextActivity;
import com.hb.rssai.view.iView.IRecordView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/10/28 0028.
 */

public class RecordPresenter extends BasePresenter<IRecordView> {
    private IRecordView mIRecordView;
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private int page = 1;
    private boolean isEnd = false, isLoad = false;
    private LinearLayoutManager mManager;
    private RecordAdapter adapter;
    private List<ResUserInformation.RetObjBean.RowsBean> userInformations = new ArrayList<>();

    public RecordPresenter(Context mContext, IRecordView IRecordView) {
        this.mContext = mContext;
        mIRecordView = IRecordView;
        initView();
    }

    private void initView() {
        mSwipeRefreshLayout = mIRecordView.getSwipeLayout();
        mRecyclerView = mIRecordView.getRecyclerView();
        mManager = mIRecordView.getManager();

        mSwipeRefreshLayout.setOnRefreshListener(() -> refreshList());
        //TODO 设置上拉加载更多
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (adapter == null) {
                    isLoad = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                }
                // 在最后两条的时候就自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= adapter.getItemCount()) {
                    // 加载更多
                    if (!isEnd && !isLoad) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        page++;
                        getList();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mManager.findLastVisibleItemPosition();
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
        if (userInformations != null) {
            userInformations.clear();
        }
        mSwipeRefreshLayout.setRefreshing(true);
        getList();
    }

    public void getList() {
        informationApi.findUserInformation(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resUserInformation -> {
                    setListResult(resUserInformation);
                }, this::loadError);
    }
    private String infoId = "";
    private ResUserInformation.RetObjBean.RowsBean bean;
    private void setListResult(ResUserInformation resUserInformation) {
        isLoad = false;
        mSwipeRefreshLayout.setRefreshing(false);
        //TODO 填充数据
        if (resUserInformation.getRetCode() == 0) {
            mIRecordView.getIncludeNoData().setVisibility(View.GONE);
            mIRecordView.getIncludeLoadFail().setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            if (resUserInformation.getRetObj().getRows() != null && resUserInformation.getRetObj().getRows().size() > 0) {
                this.userInformations.addAll(resUserInformation.getRetObj().getRows());
                if (adapter == null) {
                    adapter = new RecordAdapter(mContext, userInformations);
                    mRecyclerView.setAdapter(adapter);
                    adapter.setMyOnItemClickedListener(new RecordAdapter.MyOnItemClickedListener() {
                        @Override
                        public void onItemClicked(ResUserInformation.RetObjBean.RowsBean rowsBean) {
                            bean=rowsBean;
                            if (!TextUtils.isEmpty(rowsBean.getInformationId())) {
                                infoId = rowsBean.getInformationId();
                                getInformation(); //获取消息
                            } else {
                                Intent intent = new Intent(mContext, ContentActivity.class);
                                intent.putExtra(ContentActivity.KEY_URL, rowsBean.getInformationLink());
                                intent.putExtra(ContentActivity.KEY_TITLE, rowsBean.getInformationTitle());
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
            if (this.userInformations.size() == resUserInformation.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else if (resUserInformation.getRetCode() == 10013) {//暂无数据
            mIRecordView.getIncludeNoData().setVisibility(View.VISIBLE);
            mIRecordView.getIncludeLoadFail().setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mIRecordView.getIncludeNoData().setVisibility(View.GONE);
            mIRecordView.getIncludeLoadFail().setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            T.ShowToast(mContext, resUserInformation.getRetMsg());
        }
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
            intent.putExtra(ContentActivity.KEY_URL, bean.getInformationLink());
            intent.putExtra(ContentActivity.KEY_TITLE, bean.getInformationTitle());
            intent.putExtra(ContentActivity.KEY_INFORMATION_ID, bean.getInformationId());
            mContext.startActivity(intent);
        }
    }

    private void loadError(Throwable throwable) {
        mIRecordView.getIncludeLoadFail().setVisibility(View.VISIBLE);
        mIRecordView.getIncludeNoData().setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);

        mSwipeRefreshLayout.setRefreshing(false);
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

    private Map<String, String> getInfoParams() {
        Map<String, String> map = new HashMap<>();
        String informationId = infoId;
        String jsonParams = "{\"informationId\":\"" + informationId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }
}
