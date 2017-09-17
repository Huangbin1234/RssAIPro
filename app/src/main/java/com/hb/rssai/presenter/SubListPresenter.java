package com.hb.rssai.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.hb.rssai.R;
import com.hb.rssai.adapter.DialogAdapter;
import com.hb.rssai.adapter.SubListAdapter;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.RssSourceEvent;
import com.hb.rssai.util.Base64Util;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.QrCodeActivity;
import com.hb.rssai.view.iView.ISubListView;
import com.hb.rssai.view.widget.FullListView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/17 0017.
 */

public class SubListPresenter extends BasePresenter<ISubListView> {
    private Context mContext;
    private ISubListView mISubListView;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private List<ResFindMore.RetObjBean.RowsBean> resLists = new ArrayList<>();


    private int page = 1;
    private boolean isEnd = false, isLoad = false;
    private SwipeRefreshLayout mSwipeLayout;
    private SubListAdapter mSubListAdapter;
    private LinearLayout mSubLl;

    public SubListPresenter(Context context, ISubListView ISubListView) {
        mContext = context;
        mISubListView = ISubListView;
        initView();
    }

    private void initView() {
        mRecyclerView = mISubListView.getRecyclerView();
        mLayoutManager = mISubListView.getManager();
        mSwipeLayout = mISubListView.getSwipeLayout();
        mSubLl = mISubListView.getLlEmptyView();

        //TODO 设置下拉刷新
        mSwipeLayout.setOnRefreshListener(() -> refreshList());
        //TODO 设置上拉加载更多
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mSubListAdapter == null) {
                    isLoad = false;
                    mSwipeLayout.setRefreshing(false);
                    return;
                }
                // 在最后两条的时候就自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= mSubListAdapter.getItemCount()) {
                    // 加载更多
                    if (!isEnd && !isLoad) {
                        mSwipeLayout.setRefreshing(true);
                        page++;
                        getUserSubscribeList();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
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
        if (resLists != null) {
            resLists.clear();
        }
        mSwipeLayout.setRefreshing(true);
        getUserSubscribeList();
    }

    public void updateUsSort() {
        findApi.updateUsSort(getUpdateParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setUpdateUsSortResult(resBase);
                }, this::loadError);
    }

    private void setUpdateUsSortResult(ResBase resBase) {
        if (resBase.getRetCode() == 0) {
            EventBus.getDefault().post(new RssSourceEvent(0));
            refreshList();
        }
        T.ShowToast(mContext, resBase.getRetMsg());
    }

    public void getUserSubscribeList() {
        findApi.userSubscribeList(getUserSubscribeParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resFindMore -> {
                    setUserSubscribeResult(resFindMore);
                }, this::loadError);
    }

    private void setUserSubscribeResult(ResFindMore resFindMore) {
        isLoad = false;
        mSwipeLayout.setRefreshing(false);
        mSubLl.setVisibility(View.GONE);
        //TODO 填充数据
        if (resFindMore.getRetCode() == 0) {
            if (resFindMore.getRetObj().getRows() != null && resFindMore.getRetObj().getRows().size() > 0) {
                resLists.addAll(resFindMore.getRetObj().getRows());
                if (mSubListAdapter == null) {
                    mSubListAdapter = new SubListAdapter(mContext, resLists, (Activity) mContext);
                    mRecyclerView.setAdapter(mSubListAdapter);
                    mSubListAdapter.setOnItemLongClickedListener(rssSource -> {
                        openMenu(rssSource);
                    });
                } else {
                    mSubListAdapter.notifyDataSetChanged();
                }
            }
            if (resLists.size() == resFindMore.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else {
            T.ShowToast(mContext, resFindMore.getRetMsg());
        }
    }

    /**
     * 构造对话框数据
     *
     * @return
     */
    private List<HashMap<String, Object>> initDialogData() {
        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "置顶");
        map.put("id", 1);
        map.put("url", R.mipmap.ic_top);
        list.add(map);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("name", "分享");
        map2.put("id", 2);
        map2.put("url", R.mipmap.ic_share);
        list.add(map2);

        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("name", "删除");
        map3.put("id", 3);
        map3.put("url", R.mipmap.ic_delete);
        list.add(map3);
        return list;
    }

    /**
     * 菜单对话框
     *
     * @return
     */
    DialogAdapter dialogAdapter;
    MaterialDialog materialDialog;
    ResFindMore.RetObjBean.RowsBean mClickBean;

    private void openMenu(ResFindMore.RetObjBean.RowsBean bean) {
        mClickBean = bean;
        if (materialDialog == null) {
            materialDialog = new MaterialDialog(mContext);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.view_dialog, null);
            FullListView listView = (FullListView) view.findViewById(R.id.dialog_listView);

            List<HashMap<String, Object>> list = initDialogData();
            listView.setOnItemClickListener((parent, view1, position, id) -> {
                if (list.get(position).get("id").equals(1)) {
                    //TODO 置顶
                    materialDialog.dismiss();
//                    mClickBean.setSort(new Date().getTime());
//                    LiteOrmDBUtil.update(mClickBean);
//                    refreshList();
                    updateUsSort();
                } else if (list.get(position).get("id").equals(2)) {
                    materialDialog.dismiss();
                    Intent intent = new Intent(mContext, QrCodeActivity.class);
                    intent.putExtra(QrCodeActivity.KEY_FROM, QrCodeActivity.FROM_VALUES[0]);
                    intent.putExtra(QrCodeActivity.KEY_TITLE, mClickBean.getName());
                    intent.putExtra(QrCodeActivity.KEY_CONTENT, Base64Util.getEncodeStr(Constant.FLAG_RSS_SOURCE + mClickBean.getLink()));
                    mContext.startActivity(intent);
                } else if (list.get(position).get("id").equals(3)) {
                    materialDialog.dismiss();
//                    LiteOrmDBUtil.deleteWhere(RssSource.class, "id", new String[]{"" + mClickBean.getId()});
//                    refreshList();
//                    T.ShowToast(mContext, "删除成功！");
                    delSubscription();
                }
            });
            if (dialogAdapter == null) {
                dialogAdapter = new DialogAdapter(mContext, list);
                listView.setAdapter(dialogAdapter);
            }
            dialogAdapter.notifyDataSetChanged();
            materialDialog.setContentView(view).setTitle(Constant.TIPS_SYSTEM).setNegativeButton("关闭", v -> {
                materialDialog.dismiss();
            }).show();
        } else {
            materialDialog.show();
        }
    }

    private void loadError(Throwable throwable) {
        mSwipeLayout.setRefreshing(false);
        mSubLl.setVisibility(View.GONE);
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

    private Map<String, String> getUpdateParams() {
        Map<String, String> map = new HashMap<>();
        String usId = mClickBean.getUsId();
        String jsonParams = "{\"usId\":\"" + usId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private Map<String, String> getUserSubscribeParams() {
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        boolean isTag = mISubListView.getIsTag();
        String jsonParams = "{\"userId\":\"" + userId + "\",\"isTag\":\"" + isTag + "\",\"page\":\"" + page + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    public void delSubscription() {
        findApi.delSubscription(getDelParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setDelResult(resBase);
                }, this::loadError);
    }

    private void setDelResult(ResBase resBase) {
        if (resBase.getRetCode() == 0) {
            EventBus.getDefault().post(new RssSourceEvent(0));
            refreshList();
        }
        T.ShowToast(mContext, resBase.getRetMsg());
    }

    private Map<String, String> getDelParams() {
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
//        String usId = mISubscriptionView.getUsId();
        String subscribeId = mClickBean.getId();
        String jsonParams = "{\"subscribeId\":\"" + subscribeId + "\",\"usId\":\"" + userId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }
}
