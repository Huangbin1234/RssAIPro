package com.hb.rssai.presenter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hb.rssai.adapter.InfoAdapter;
import com.hb.rssai.bean.Information;
import com.hb.rssai.bean.ResDataGroup;
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IInformationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/17 0017.
 */

public class InformationPresenter extends BasePresenter<IInformationView> {
    private Context mContext;
    private IInformationView iInformationView;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;

    private int page = 1;
    private boolean isEnd = false, isLoad = false;
    private InfoAdapter adapter;
    List<ResInformation.RetObjBean.RowsBean> infoList = new ArrayList<>();
    private LinearLayout mLlLoad;

    public InformationPresenter(Context context, IInformationView iInformationView) {
        mContext = context;
        this.iInformationView = iInformationView;
        initView();
    }

    private void initView() {
        mRecyclerView = iInformationView.getRecyclerView();
        mSwipeRefreshLayout = iInformationView.getSwipeLayout();
        mLinearLayoutManager = iInformationView.getManager();
        mLlLoad = iInformationView.getLlLoad();


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
                        boolean isUser = iInformationView.getIsUser();
                        if (isUser) {
                            getUserList();
                        } else {
                            getList();
                        }
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

    public void getList() {
        informationApi.getList(getListParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInformation -> {
                    setListResult(resInformation);
                }, this::loadError);
    }

    public void getUserList() {
        informationApi.getUserInformation(getListParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInformation -> {
                    setListResult(resInformation);
                }, this::loadError);
    }

    public void getDataGroupList() {
        dataGroupApi.getDataGroupList(getDataGroupListParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resDataGroup -> {
                    setDataGroupResult(resDataGroup);
                }, this::loadError);
    }

    List<ResDataGroup.RetObjBean.RowsBean> mRowsBeanList = new ArrayList<>();
    List<ResDataGroup.RetObjBean.RowsBean> mMeRowsBeanList = new ArrayList<>();

    private void setDataGroupResult(ResDataGroup resDataGroup) {
        if (resDataGroup != null && resDataGroup.getRetCode() == 0) {
            for (ResDataGroup.RetObjBean.RowsBean bean : resDataGroup.getRetObj().getRows()) {
                if (bean.getGroupType() == 0) {
                    mRowsBeanList.add(bean);
                } else if (bean.getGroupType() == 1) {
                    mMeRowsBeanList.add(bean);
                }
            }
        }
    }

    public List<ResDataGroup.RetObjBean.RowsBean> getGroupList() {
        return mRowsBeanList;
    }

    public List<ResDataGroup.RetObjBean.RowsBean> getMeGroupList() {
        return mMeRowsBeanList;
    }

    private HashMap<String, String> getDataGroupListParams() {
        //参数可以不要
        HashMap<String, String> map = new HashMap<>();
        String jsonParams = "{\"page\":\"" + page + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    /**
     * 刷新数据
     */
    public void refreshList() {
        page = 1;
        isLoad = true;
        isEnd = false;
        if (infoList != null) {
            infoList.clear();
        }
        mSwipeRefreshLayout.setRefreshing(true);
        boolean isUser = iInformationView.getIsUser();
        if (isUser) {
            getUserList();
        } else {
            getList();
        }
    }

    private Map<String, String> getListParams() {
        Map<String, String> map = new HashMap<>();
        int dataType = iInformationView.getDataType();
        String jsonParams = "{\"dataType\":\"" + dataType + "\",\"page\":\"" + page + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private void loadError(Throwable throwable) {
        mLlLoad.setVisibility(View.GONE);

        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);

        List<Information> list = LiteOrmDBUtil.getQueryAll(Information.class);
        if (list != null && list.size() > 0) {
            T.ShowToast(mContext, "启用离线模式");
            //TODO 填充数据
            mLlLoad.setVisibility(View.GONE);
            isLoad = false;
            mSwipeRefreshLayout.setRefreshing(false);
            //TODO 填充数据

            for (Information info : list) {
                ResInformation.RetObjBean.RowsBean rowBean=new ResInformation.RetObjBean.RowsBean();
                rowBean.setAuthor(info.getAuthor());
                rowBean.setPubTime(info.getPubTime());
                rowBean.setDataType(info.getDataType());
                rowBean.setAbstractContent(info.getAbstractContent());
                rowBean.setCount((int)info.getCount());
                rowBean.setLink(info.getLink());
                rowBean.setWhereFrom(info.getWhereFrom());
                rowBean.setTitle(info.getTitle());
                rowBean.setContent(info.getContent());
                rowBean.setDeleteFlag(info.isDeleteFlag());
                rowBean.setImageUrls(info.getImageUrls());
                rowBean.setId(info.getId());
                rowBean.setOprTime(info.getOprTime());
                rowBean.setClickGood(info.getClickGood());
                rowBean.setClickNotGood(info.getClickNotGood());
                infoList.add(rowBean);
            }

            if (adapter == null) {
                adapter = new InfoAdapter(mContext, infoList);
                mRecyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
            if (infoList.size() == list.size()) {
                isEnd = true;
            }
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }


    private void setListResult(ResInformation resInformation) {
        //TODO 填充数据
        mLlLoad.setVisibility(View.GONE);
        isLoad = false;
        mSwipeRefreshLayout.setRefreshing(false);
        //TODO 填充数据
        if (resInformation.getRetCode() == 0) {
            if (resInformation.getRetObj().getRows() != null && resInformation.getRetObj().getRows().size() > 0) {
                infoList.addAll(resInformation.getRetObj().getRows());
                if (adapter == null) {
                    adapter = new InfoAdapter(mContext, infoList);
                    mRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
            if (infoList.size() == resInformation.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else {
            T.ShowToast(mContext, resInformation.getRetMsg());
        }
    }
}
