package com.hb.rssai.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hb.rssai.R;
import com.hb.rssai.adapter.SourceListAdapter;
import com.hb.rssai.adapter.SourceListCardAdapter;
import com.hb.rssai.app.ProjectApplication;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResCardSubscribe;
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.bean.ResSubscription;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.RssSourceEvent;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.LoginActivity;
import com.hb.rssai.view.iView.ISourceListView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/18 0018.
 */

public class SourceListPresenter extends BasePresenter<ISourceListView> {
    private Context mContext;
    private ISourceListView iSourceListView;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
//    private LinearLayout mLlLoad;

    private int page = 1;
    private boolean isEnd = false, isLoad = false;
    private SourceListAdapter adapter;
    List<ResInformation.RetObjBean.RowsBean> infoList = new ArrayList<>();
    List<List<ResCardSubscribe.RetObjBean.RowsBean>> infoListCard = new ArrayList<>();
//    private SourceListCardAdapter cardAdapter;
    private NestedScrollView mNestView;
    private boolean isCheck;

    public SourceListPresenter(Context context, ISourceListView iSourceListView) {
        mContext = context;
        this.iSourceListView = iSourceListView;
        initView();
    }

    private void initView() {
        mRecyclerView = iSourceListView.getRecyclerView();
        mSwipeRefreshLayout = iSourceListView.getSwipeLayout();
        mNestView = iSourceListView.getNestLayout();
        mLinearLayoutManager = iSourceListView.getManager();
//        mLlLoad = iSourceListView.getLlLoad();


        mSwipeRefreshLayout.setOnRefreshListener(() -> refreshList());
        //TODO 设置上拉加载更多

        mNestView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (v.getChildAt(0) != null && isBottomForNestedScrollView(v, scrollY)) {
                // 加载更多
                if (!isEnd && !isLoad) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    page++;
                    getListCardById();
                }
            }
        });
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
        if (infoList != null) {
            infoList.clear();
        }
        if (infoListCard != null) {
            infoListCard.clear();
        }
        mSwipeRefreshLayout.setRefreshing(true);
        getListCardById();
    }

    public void subscribe(View v) {
        if (!TextUtils.isEmpty(SharedPreferencesUtil.getString(mContext, Constant.TOKEN, ""))) {
            //TODO 先去查询服务器上此条数据
            findMoreListById(v);
        } else {
            //跳转到登录
            T.ShowToast(mContext, Constant.MSG_NO_LOGIN);
            Intent intent = new Intent(ProjectApplication.mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ProjectApplication.mContext.startActivity(intent);
        }
    }

    public void findMoreListById(View v) {
        if (iSourceListView != null) {
            findApi.findMoreListById(getFindMoreByIdParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resSubscription -> {
                        setFindMoreByIdResult(resSubscription, v);
                    }, iSourceListView::loadError);
        }
    }

    public void addSubscription(View v) {
        findApi.subscribe(getSubscribeParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setAddResult(resBase, v);
                }, iSourceListView::loadError);
    }

    public void getListCardById() {
        informationApi.listCardById(getListParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resCardSubscribe -> {
                    iSourceListView.setListCardResult(resCardSubscribe);
                }, iSourceListView::loadError);
    }

    public void delSubscription(View v) {
        findApi.delSubscription(getDelParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setDelResult(resBase, v);
                }, iSourceListView::loadError);
    }

    private void setFindMoreByIdResult(ResSubscription resSubscription, View v) {
        if (resSubscription.getRetCode() == 0) {
            if (resSubscription.getRetObj().isDeleteFlag()) {
                addSubscription(v);
            } else { //如果发现没有被删除
                if (TextUtils.isEmpty(resSubscription.getRetObj().getUserId())) {//如果也没有被添加过
                    addSubscription(v);
                } else {//如果被添加过
                    String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
                    if (userId.equals(resSubscription.getRetObj().getUserId())) {//如果是等于当前登录ID
                        delSubscription(v);
                    } else {//不等于
                        addSubscription(v);
                    }
                }
            }
        } else if (resSubscription.getRetCode() == 10013) {
            //从来没订阅过
            addSubscription(v);
        } else {
            T.ShowToast(mContext, resSubscription.getRetMsg());
        }
    }

    private void setDelResult(ResBase resBase, View v) {
        T.ShowToast(mContext, resBase.getRetMsg());
        if (resBase.getRetCode() == 0) {
            EventBus.getDefault().post(new RssSourceEvent(0));
        }
        if (resBase.getRetCode() == 0) {
            ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_add);
        } else {
            ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_cancel);
        }
    }

    private void setAddResult(ResBase resBase, View v) {
        T.ShowToast(mContext, resBase.getRetMsg());
        if (resBase.getRetCode() == 0) {
            EventBus.getDefault().post(new RssSourceEvent(0));
        }
        if (resBase.getRetCode() == 0) {
            ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_cancel);
        } else {
            ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_add);
        }
    }

//    private void setListCardResult(ResCardSubscribe resCardSubscribe) {
//        //TODO 填充数据
//        mLlLoad.setVisibility(View.GONE);
//        isLoad = false;
//        mSwipeRefreshLayout.setRefreshing(false);
//        //TODO 填充数据
//        if (resCardSubscribe.getRetCode() == 0) {
//            if (resCardSubscribe.getRetObj().getRows() != null && resCardSubscribe.getRetObj().getRows().size() > 0) {
//                infoListCard.addAll(resCardSubscribe.getRetObj().getRows());
//                if (cardAdapter == null) {
//                    cardAdapter = new SourceListCardAdapter(mContext, infoListCard);
//                    mRecyclerView.setAdapter(cardAdapter);
//                } else {
//                    cardAdapter.notifyDataSetChanged();
//                }
//            }
//            int sum = 0;
//            for (List<ResCardSubscribe.RetObjBean.RowsBean> rBean : infoListCard) {
//                sum += rBean.size();
//            }
//
//            if (sum >= resCardSubscribe.getRetObj().getTotal()) {
//                isEnd = true;
//            }
//        } else {
//            T.ShowToast(mContext, resCardSubscribe.getRetMsg());
//        }
//    }

    private Map<String, String> getFindMoreByIdParams() {
        Map<String, String> map = new HashMap<>();
        String subscribeId = iSourceListView.getSubscribeId();
        String jsonParams = "{\"subscribeId\":\"" + subscribeId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private Map<String, String> getDelParams() {
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String subscribeId = iSourceListView.getSubscribeId();
        String jsonParams = "{\"subscribeId\":\"" + subscribeId + "\",\"usId\":\"" + userId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private Map<String, String> getSubscribeParams() {
        Map<String, String> map = new HashMap<>();
        String subscribeId = iSourceListView.getSubscribeId();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String jsonParams = "{\"userId\":\"" + userId + "\",\"subscribeId\":\"" + subscribeId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }


    private Map<String, String> getListParams() {
        Map<String, String> map = new HashMap<>();
        String subscribeId = iSourceListView.getSubscribeId();
        String jsonParams = "{\"subscribeId\":\"" + subscribeId + "\",\"page\":\"" + page + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }
}
