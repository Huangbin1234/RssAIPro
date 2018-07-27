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
import com.hb.rssai.adapter.FindMoreAdapter;
import com.hb.rssai.adapter.RecommendAdapter;
import com.hb.rssai.app.ProjectApplication;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.bean.ResSubscription;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.RssSourceEvent;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.LoginActivity;
import com.hb.rssai.view.iView.IFindView;
import com.hb.rssai.view.subscription.SourceCardActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/15.
 */

public class FindPresenter extends BasePresenter<IFindView> {
    private Context mContext;
    private IFindView iFindView;

    private RecommendAdapter recommendAdapter;
    private FindMoreAdapter findMoreAdapter;

    private RecyclerView mFfHotRecyclerView;
    private RecyclerView mFfFindRecyclerView;
    private SwipeRefreshLayout swipeLayout;
    private List<ResFindMore.RetObjBean.RowsBean> resFindMores = new ArrayList<>();
    private List<ResFindMore.RetObjBean.RowsBean> resRecommends = new ArrayList<>();
    private LinearLayoutManager findMoreManager;
    private NestedScrollView mNestRefresh;
    private int page = 1;
    private int recommendPage = 1;
    private boolean isEnd = false, isLoad = false;
    private boolean isRecommendEnd = false, isRecommendLoad = false;
    private LinearLayout mLlRecommend;
    private ResFindMore.RetObjBean.RowsBean rowsBean;

    View include_no_data;
    View include_load_fail;

    public FindPresenter(Context mContext, IFindView iFindView) {
        this.mContext = mContext;
        this.iFindView = iFindView;
        initView();
    }

    private void initView() {
        mFfHotRecyclerView = iFindView.getFfHotRecyclerView();
        mFfFindRecyclerView = iFindView.getFfFindRecyclerView();
        swipeLayout = iFindView.getFfSwipeLayout();
        findMoreManager = iFindView.getFindMoreManager();
        mNestRefresh = iFindView.getNestScrollView();
        mLlRecommend = iFindView.getLlRecommend();

        include_no_data = iFindView.getIncludeNoData();
        include_load_fail = iFindView.getIncludeLoadFail();

        mLlRecommend.setOnClickListener(v -> {
            if (!isRecommendEnd && !isRecommendLoad) {
                recommendPage++;
                recommendList();
            }
        });
        swipeLayout.setOnRefreshListener(() -> refreshList());
        mNestRefresh.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (v.getChildAt(0) != null && isBottomForNestedScrollView(v, scrollY)) {
                    // 加载更多
                    if (!isEnd && !isLoad) {
                        swipeLayout.setRefreshing(true);
                        page++;
                        findMoreList();
                    }
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
        recommendPage = 1;
        isLoad = true;
        isRecommendLoad = true;

        isEnd = false;
        isRecommendEnd = false;

        if (resFindMores != null) {
            resFindMores.clear();
        }
        if (resRecommends != null) {
            resRecommends.clear();
        }
        swipeLayout.setRefreshing(true);

        if (findMoreAdapter != null) {
            findMoreAdapter.init();
        }
        if (recommendAdapter != null) {
            recommendAdapter.init();
        }

        recommendList();
        findMoreList();
    }

    public void findMoreList() {
        if (iFindView != null) {
            findApi.findMoreList(getFindMoreParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resFindMore -> {
                        setFindMoreResult(resFindMore);
                    }, this::loadFindError);
        }
    }

    public void recommendList() {
        if (iFindView != null) {
            findApi.recommendList(getRecommendParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resFindMore -> {
                        setRecommendResult(resFindMore);
                    }, this::loadError);
        }
    }

    public void findMoreListById(View v, boolean isRecommend) {
        if (iFindView != null) {
            findApi.findMoreListById(getFindMoreByIdParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resSubscription -> {
                        setFindMoreByIdResult(resSubscription, v, isRecommend);
                    }, this::loadError);
        }
    }

    public void addSubscription(View v, boolean isRecommend) {
        findApi.subscribe(getSubscribeParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setAddResult(resBase, v, isRecommend);
                }, this::loadError);
    }

    public void delSubscription(View v, boolean isRecommend) {
        findApi.delSubscription(getDelParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setDelResult(resBase, v, isRecommend);
                }, this::loadError);
    }

    private void setFindMoreByIdResult(ResSubscription resSubscription, View v, boolean isRecommend) {
        if (resSubscription.getRetCode() == 0) {
            if (resSubscription.getRetObj().isDeleteFlag()) {
                addSubscription(v, isRecommend);
            } else { //如果发现没有被删除
                if (TextUtils.isEmpty(resSubscription.getRetObj().getUserId())) {//如果也没有被添加过
                    addSubscription(v, isRecommend);
                } else {//如果被添加过
                    String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
                    if (userId.equals(resSubscription.getRetObj().getUserId())) {//如果是等于当前登录ID
                        delSubscription(v, isRecommend);
                    } else {//不等于
                        addSubscription(v, isRecommend);
                    }
                }
            }
        } else if (resSubscription.getRetCode() == 10013) {
            //从来没订阅过
            addSubscription(v, isRecommend);
        } else {
            T.ShowToast(mContext, resSubscription.getRetMsg());
        }
    }

    private void loadFindError(Throwable throwable) {

        if (!(null != resFindMores && resFindMores.size() > 0)) {
            include_load_fail.setVisibility(View.VISIBLE);
            include_no_data.setVisibility(View.GONE);
        }
        swipeLayout.setRefreshing(false);
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

    private void loadError(Throwable throwable) {
        swipeLayout.setRefreshing(false);
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

    private void setFindMoreResult(ResFindMore resFindMore) {
        isLoad = false;
        swipeLayout.setRefreshing(false);
        //TODO 填充数据
        if (resFindMore.getRetCode() == 0) {
            mFfFindRecyclerView.setVisibility(View.VISIBLE);
            include_load_fail.setVisibility(View.GONE);
            include_no_data.setVisibility(View.GONE);

            if (resFindMore.getRetObj().getRows() != null && resFindMore.getRetObj().getRows().size() > 0) {
                resFindMores.addAll(resFindMore.getRetObj().getRows());
                if (findMoreAdapter == null) {
                    findMoreAdapter = new FindMoreAdapter(mContext, resFindMores);
                    findMoreAdapter.setOnItemClickedListener(rowsBean1 -> {

                        Intent intent = new Intent(mContext, SourceCardActivity.class);
                        intent.putExtra(SourceCardActivity.KEY_LINK, rowsBean1.getLink());
                        intent.putExtra(SourceCardActivity.KEY_TITLE, rowsBean1.getName());
                        intent.putExtra(SourceCardActivity.KEY_SUBSCRIBE_ID, rowsBean1.getId());
                        intent.putExtra(SourceCardActivity.KEY_IMAGE, rowsBean1.getImg());
                        intent.putExtra(SourceCardActivity.KEY_DESC, rowsBean1.getAbstractContent());
                        intent.putExtra(SourceCardActivity.KEY_IS_CHECK, rowsBean1.isCheck());
                        mContext.startActivity(intent);
                    });
                    findMoreAdapter.setOnAddClickedListener((bean, v) -> {
                        rowsBean = bean;

                        if (!TextUtils.isEmpty(SharedPreferencesUtil.getString(mContext, Constant.TOKEN, ""))) {
                            //TODO 先去查询服务器上此条数据
                            findMoreListById(v, false);
                        } else {
                            //跳转到登录
                            T.ShowToast(mContext, Constant.MSG_NO_LOGIN);
                            Intent intent = new Intent(ProjectApplication.mContext, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ProjectApplication.mContext.startActivity(intent);
                        }
                    });
                    mFfFindRecyclerView.setAdapter(findMoreAdapter);
                } else {
                    findMoreAdapter.notifyDataSetChanged();
                }
            }
            if (resFindMores.size() == resFindMore.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else if (resFindMore.getRetCode() == 10013) {//暂无数据
            include_no_data.setVisibility(View.VISIBLE);
            include_load_fail.setVisibility(View.GONE);
            mFfFindRecyclerView.setVisibility(View.GONE);
        } else {
            include_no_data.setVisibility(View.GONE);
            include_load_fail.setVisibility(View.VISIBLE);
            mFfFindRecyclerView.setVisibility(View.GONE);
            T.ShowToast(mContext, resFindMore.getRetMsg());
        }
    }

    private void setRecommendResult(ResFindMore resFindMore) {
        isRecommendLoad = false;
        //TODO 填充数据
        if (resFindMore.getRetCode() == 0) {
            if (resFindMore.getRetObj().getRows() != null && resFindMore.getRetObj().getRows().size() > 0) {
                if (resRecommends.size() > 0) {
                    resRecommends.clear();
                    recommendAdapter.notifyDataSetChanged();
                }
                resRecommends.addAll(resFindMore.getRetObj().getRows());
                if (recommendAdapter == null) {
                    recommendAdapter = new RecommendAdapter(mContext, resRecommends);
                    recommendAdapter.setOnAddClickedListener((bean, v) -> {
                        rowsBean = bean;
                        if (!TextUtils.isEmpty(SharedPreferencesUtil.getString(mContext, Constant.TOKEN, ""))) {
                            //TODO 先去查询服务器上此条数据
                            findMoreListById(v, true);
                        } else {
                            //跳转到登录
                            T.ShowToast(mContext, Constant.MSG_NO_LOGIN);
                            Intent intent = new Intent(ProjectApplication.mContext, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ProjectApplication.mContext.startActivity(intent);
                        }
                    });
                    mFfHotRecyclerView.setAdapter(recommendAdapter);
                } else {
                    recommendAdapter.notifyDataSetChanged();
                }
                if (recommendPage * Constant.RECOMMEND_PAGE_SIZE + resRecommends.size() >= resFindMore.getRetObj().getTotal()) {
                    isRecommendEnd = true;
                }
            }
        } else {
            T.ShowToast(mContext, resFindMore.getRetMsg());
        }
    }

    private void setDelResult(ResBase resBase, View v, boolean isRecommend) {
        T.ShowToast(mContext, resBase.getRetMsg());
        if (resBase.getRetCode() == 0) {
            EventBus.getDefault().post(new RssSourceEvent(0));
        }
        if (isRecommend) {
            if (resBase.getRetCode() == 0) {
                ((ImageView) v).setImageResource(R.mipmap.ic_recommend_add);
            } else {
                ((ImageView) v).setImageResource(R.color.trans);
            }
        } else {
            if (resBase.getRetCode() == 0) {
                ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_add);
            } else {
                ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_cancel);
            }
        }
    }

    private void setAddResult(ResBase resBase, View v, boolean isRecommend) {
        T.ShowToast(mContext, resBase.getRetMsg());
        if (resBase.getRetCode() == 0) {
            EventBus.getDefault().post(new RssSourceEvent(0));
        }
        if (isRecommend) {
            if (resBase.getRetCode() == 0) {
                ((ImageView) v).setImageResource(R.color.trans);
            } else {
                ((ImageView) v).setImageResource(R.mipmap.ic_recommend_add);
            }
        } else {
            if (resBase.getRetCode() == 0) {
                ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_cancel);
            } else {
                ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_add);
            }
        }
    }

    private Map<String, String> getDelParams() {
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String subscribeId = rowsBean.getId();
        String jsonParams = "{\"subscribeId\":\"" + subscribeId + "\",\"usId\":\"" + userId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private Map<String, String> getFindMoreParams() {
        Map<String, String> map = new HashMap<>();
        String jsonParams = "{\"page\":\"" + page + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private Map<String, String> getFindMoreByIdParams() {
        Map<String, String> map = new HashMap<>();
        String subscribeId = rowsBean.getId();
        String jsonParams = "{\"subscribeId\":\"" + subscribeId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private Map<String, String> getRecommendParams() {
        Map<String, String> map = new HashMap<>();
        String jsonParams = "{\"page\":\"" + recommendPage + "\",\"size\":\"" + Constant.RECOMMEND_PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private Map<String, String> getSubscribeParams() {
        Map<String, String> map = new HashMap<>();
        String subscribeId = rowsBean.getId();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String jsonParams = "{\"userId\":\"" + userId + "\",\"subscribeId\":\"" + subscribeId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }
}
