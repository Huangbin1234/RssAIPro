package com.hb.rssai.presenter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.hb.rssai.adapter.InfoTestAdapter;
import com.hb.rssai.bean.Information;
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.TipsEvent;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.ITabDataView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/28.
 */

public class TabDataPresenter extends BasePresenter<ITabDataView> {
    private Context mContext;
    private ITabDataView iTabDataView;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private List<ResInformation.RetObjBean.RowsBean> infoList = new ArrayList<>();
    //    private InfoAdapter adapter;
    private InfoTestAdapter adapter;

    private LinearLayout mLlLoad;
    private View include_no_data;
    private View include_load_fail;

    private int page = 1;
    private boolean isEnd = false, isLoad = false;

    public TabDataPresenter(Context mContext, ITabDataView iTabDataView) {
        this.mContext = mContext;
        this.iTabDataView = iTabDataView;
        initView();
    }

    private void initView() {
        mRecyclerView = iTabDataView.getRecyclerView();
        mSwipeRefreshLayout = iTabDataView.getSwipeLayout();
        mLinearLayoutManager = iTabDataView.getManager();

        mLlLoad = iTabDataView.getLlLoad();
        include_no_data = iTabDataView.getIncludeNoData();
        include_load_fail = iTabDataView.getIncludeLoadFail();

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
                        boolean isUser = iTabDataView.getIsUser();
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

    /**
     * 刷新数据
     */
    public void refreshList() {
        page = 1;
        isLoad = true;
        isEnd = false;

        mSwipeRefreshLayout.setRefreshing(true);
        boolean isUser = iTabDataView.getIsUser();
        if (isUser) {
            getUserList();
        } else {
            getList();
        }
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

    private Map<String, String> getListParams() {
        Map<String, String> map = new HashMap<>();
        int dataType = iTabDataView.getDataType();
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
                ResInformation.RetObjBean.RowsBean rowBean = new ResInformation.RetObjBean.RowsBean();
                rowBean.setAuthor(info.getAuthor());
                rowBean.setPubTime(info.getPubTime());
                rowBean.setDataType(info.getDataType());
                rowBean.setAbstractContent(info.getAbstractContent());
                rowBean.setCount((int) info.getCount());
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
                adapter = new InfoTestAdapter(mContext, infoList);
                mRecyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
            if (infoList.size() == list.size()) {
                isEnd = true;
            }
        } else {
            include_load_fail.setVisibility(View.VISIBLE);
            include_no_data.setVisibility(View.GONE);
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void setListResult(ResInformation resInformation) {
        //清除数据缓存
//        System.out.println("列表信息：===========>"+infoList);
        if (infoList != null && page == 1) {
            infoList.clear();
            lastWhereFrom = "";
        }

        //TODO 填充数据
        mLlLoad.setVisibility(View.GONE);
        isLoad = false;
        mSwipeRefreshLayout.setRefreshing(false);
        //TODO 填充数据
        if (resInformation.getRetCode() == 0) {
            include_load_fail.setVisibility(View.GONE);
            include_no_data.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            if (resInformation.getRetObj().getRows() != null && resInformation.getRetObj().getRows().size() > 0) {
//                infoList.addAll(resInformation.getRetObj().getRows());
                insertData(resInformation.getRetObj().getRows());
                if (adapter == null) {
                    adapter = new InfoTestAdapter(mContext, infoList);
                    mRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.init();//更新一下是否显示图片首选项
                    adapter.notifyDataSetChanged();
                }
                //通知更新
                EventBus.getDefault().post(new TipsEvent(1, resInformation.getRetObj().getRows().size()));
            }
            if (infoList.size() == resInformation.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else if (resInformation.getRetCode() == 10013) {//暂无数据
            include_no_data.setVisibility(View.VISIBLE);
            include_load_fail.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            include_no_data.setVisibility(View.GONE);
            include_load_fail.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            T.ShowToast(mContext, resInformation.getRetMsg());
        }
    }

    private void insertData(List<ResInformation.RetObjBean.RowsBean> rows) {
        ResInformation.RetObjBean.RowsBean lastBean = null;
//        ResInformation.RetObjBean.RowsBean nextBean = null;
        ResInformation.RetObjBean.RowsBean curBean = null;
        Map<Integer, ResInformation.RetObjBean.RowsBean> map = new TreeMap<>();

        for (int i = 0; i < rows.size(); i++) {
            curBean = rows.get(i);
            if (i == 0) {
                if (!TextUtils.isEmpty(lastWhereFrom) && !lastWhereFrom.equals(curBean.getWhereFrom())) {
                    //如果当前不等于上一个
                    ResInformation.RetObjBean.RowsBean rowsBean = new ResInformation.RetObjBean.RowsBean();
                    rowsBean.setWhereFrom(curBean.getWhereFrom());
                    rowsBean.setSubscribeImg(curBean.getSubscribeImg());
                    rowsBean.setPubTime(curBean.getPubTime());
                    rowsBean.setViewType(4);
                    map.put(i, rowsBean);
                }
            }
            if (i > 0) {
                lastBean = rows.get(i - 1);
            }
//            if (i > 0 && i < rows.size() - 1) {
//                nextBean = rows.get(i + 1);
//            }

            if ((i == 0 && page == 1) || (null != lastBean && !curBean.getWhereFrom().equals(lastBean.getWhereFrom()))) {
                //如果当前不等于上一个
                ResInformation.RetObjBean.RowsBean rowsBean = new ResInformation.RetObjBean.RowsBean();
                rowsBean.setWhereFrom(curBean.getWhereFrom());
                rowsBean.setSubscribeImg(curBean.getSubscribeImg());
                rowsBean.setPubTime(curBean.getPubTime());
                rowsBean.setViewType(4);
                map.put(i, rowsBean);
            }
//                        else if (null != nextBean && !curBean.getWhereFrom().equals(nextBean.getWhereFrom())) {
//                            //如果当前不等于下一个
//                            ResInformation.RetObjBean.RowsBean rowsBean = new ResInformation.RetObjBean.RowsBean();
//                            rowsBean.setPubTime(curBean.getPubTime());
//                            rowsBean.setViewType(5);
//                            map.put(i, rowsBean);
//                        }
        }

        Iterator<Map.Entry<Integer, ResInformation.RetObjBean.RowsBean>> entries = map.entrySet().iterator();
        int count = 0;
        while (entries.hasNext()) {
            Map.Entry<Integer, ResInformation.RetObjBean.RowsBean> entry = entries.next();
            int key = entry.getKey() + count;
            rows.add(key, entry.getValue());
            count++;
        }

        infoList.addAll(rows);
        lastWhereFrom = infoList.get(infoList.size() - 1).getWhereFrom();
    }

    private String lastWhereFrom = "";

    class TempBean {
        private int key;
        private ResInformation.RetObjBean.RowsBean rowsBean;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public ResInformation.RetObjBean.RowsBean getRowsBean() {
            return rowsBean;
        }

        public void setRowsBean(ResInformation.RetObjBean.RowsBean rowsBean) {
            this.rowsBean = rowsBean;
        }
    }
}
