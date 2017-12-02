package com.hb.rssai.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.FindMoreAdapter;
import com.hb.rssai.app.ProjectApplication;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.bean.ResSubscription;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.RssSourceEvent;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.LoginActivity;
import com.hb.rssai.view.iView.ITabContentView;
import com.hb.rssai.view.subscription.SourceListActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/9 0009.
 */

public class TabContentPresenter extends BasePresenter<ITabContentView> {
    private Context mContext;
    private ITabContentView mITabContentView;
    private int page = 1;
    private FindMoreAdapter findMoreAdapter;
    private boolean isEnd = false;
    private RecyclerView recyclerView;
    private List<ResFindMore.RetObjBean.RowsBean> resFindMores = new ArrayList<>();
    private ResFindMore.RetObjBean.RowsBean rowsBean;
    int dataType = 0;
    private boolean isLoad = false;
    private LinearLayoutManager topicManager;

    public TabContentPresenter(Context context, ITabContentView ITabContentView) {
        mContext = context;
        mITabContentView = ITabContentView;
        initView();
    }

    private void initView() {
        recyclerView = mITabContentView.getRecyclerView();
        topicManager = mITabContentView.getManager();

        //TODO 设置上拉加载更多
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (findMoreAdapter == null) {
                    isLoad = false;
                    return;
                }
                // 在最后两条的时候就自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= findMoreAdapter.getItemCount()) {
                    // 加载更多
                    if (!isEnd && !isLoad) {
                        page++;
                        getListData(dataType);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = topicManager.findLastVisibleItemPosition();
            }
        });
    }

    public void getListData(int dType) {
        isLoad = true;
        dataType = dType;
        findApi.getListByType(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resFindMore -> {
                    setResult(resFindMore);
                }, this::loadError);
    }

    private void setResult(ResFindMore resFindMore) {
        isLoad = false;
        //TODO 填充数据
        if (resFindMore.getRetCode() == 0) {
            if (resFindMore.getRetObj().getRows() != null && resFindMore.getRetObj().getRows().size() > 0) {
                resFindMores.addAll(resFindMore.getRetObj().getRows());
                if (findMoreAdapter == null) {
                    findMoreAdapter = new FindMoreAdapter(mContext, resFindMores);
                    findMoreAdapter.setOnItemClickedListener(rowsBean1 -> {
                        Intent intent = new Intent(mContext, SourceListActivity.class);
                        intent.putExtra(SourceListActivity.KEY_LINK, rowsBean1.getLink());
                        intent.putExtra(SourceListActivity.KEY_TITLE, rowsBean1.getName());
                        intent.putExtra(SourceListActivity.KEY_SUBSCRIBE_ID, rowsBean1.getId());
                        intent.putExtra(SourceListActivity.KEY_IMAGE, rowsBean1.getImg());
                        intent.putExtra(SourceListActivity.KEY_DESC, rowsBean1.getAbstractContent());
                        mContext.startActivity(intent);
                    });
                    findMoreAdapter.setOnAddClickedListener((bean, v) -> {
                        rowsBean = bean;
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
                    });
                    recyclerView.setAdapter(findMoreAdapter);
                } else {
                    findMoreAdapter.notifyDataSetChanged();
                }
            }
            if (resFindMores.size() == resFindMore.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else {
            T.ShowToast(mContext, resFindMore.getRetMsg());
        }
    }

    public void findMoreListById(View v) {
        if (mITabContentView != null) {
            findApi.findMoreListById(getFindMoreByIdParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resSubscription -> {
                        setFindMoreByIdResult(resSubscription, v);
                    }, this::loadError);
        }
    }

    public void addSubscription(View v) {
        findApi.subscribe(getSubscribeParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setAddResult(resBase, v);
                }, this::loadNewError);
    }

    public void delSubscription(View v) {
        findApi.delSubscription(getDelParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setDelResult(resBase, v);
                }, this::loadError);
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

    private void setAddResult(ResBase resBase, View v) {
        T.ShowToast(mContext, resBase.getRetMsg());
        if (resBase.getRetCode() == 0) {
            EventBus.getDefault().post(new RssSourceEvent(0));
            ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_cancel);
        } else {
            ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_add);
        }
    }

    private void setDelResult(ResBase resBase, View v) {
        T.ShowToast(mContext, resBase.getRetMsg());
        if (resBase.getRetCode() == 0) {
            EventBus.getDefault().post(new RssSourceEvent(0));
            ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_add);
        } else {
            ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_cancel);
        }
    }

    private void loadError(Throwable throwable) {
        isLoad = false;
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

    private void loadNewError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
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

    private Map<String, String> getFindMoreByIdParams() {
        Map<String, String> map = new HashMap<>();
        String subscribeId = rowsBean.getId();
        String jsonParams = "{\"subscribeId\":\"" + subscribeId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
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

    private Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        String jsonParams = "{\"dataType\":\"" + dataType + "\",\"page\":\"" + page + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }
}
