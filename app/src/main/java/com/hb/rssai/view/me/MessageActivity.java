package com.hb.rssai.view.me;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.MessageAdapter;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResMessageList;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.MineEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.MessagePresenter;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IMessageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MessageActivity extends BaseActivity implements IMessageView {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.msg_tv_empty)
    TextView mMsgTvEmpty;
    @BindView(R.id.msg_ll)
    LinearLayout mMsgLl;
    @BindView(R.id.msg_recycler_view)
    RecyclerView mMsgRecyclerView;
    @BindView(R.id.msg_swipe_layout)
    SwipeRefreshLayout mMsgSwipeLayout;
    @BindView(R.id.include_no_data)
    View includeNoData;
    @BindView(R.id.include_load_fail)
    View includeLoadFail;
    @BindView(R.id.llf_btn_re_try)
    Button mLlfBtnReTry;

    private LinearLayoutManager mLayoutManager;
    private MessageAdapter adapter;
    private List<ResMessageList.RetObjBean.RowsBean> mMessages = new ArrayList<>();

    private int pageNum = 1;
    private boolean isEnd = false, isLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MessagePresenter) mPresenter).getList();
    }

    @Override
    protected void initView() {
        mLayoutManager = new LinearLayoutManager(this);
        mMsgRecyclerView.setLayoutManager(mLayoutManager);
        mMsgSwipeLayout.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mMsgSwipeLayout.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        mLlfBtnReTry.setOnClickListener(v -> ((MessagePresenter) mPresenter).getList());

        //设置上下拉刷新
        mMsgSwipeLayout.setOnRefreshListener(() -> onRefresh());
        mMsgRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (adapter == null) {
                    isLoad = false;
                    mMsgSwipeLayout.setRefreshing(false);
                    return;
                }
                // 在最后两条的时候就自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= adapter.getItemCount()) {
                    // 加载更多
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_message;
    }

    @Override
    protected void setAppTitle() {
        mSysToolbar.setTitle("");
        setSupportActionBar(mSysToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//设置ActionBar一个返回箭头，主界面没有，次级界面有
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mSysTvTitle.setText(getResources().getString(R.string.str_message_title));
    }

    @Override
    protected BasePresenter createPresenter() {
        return new MessagePresenter(this);
    }

    @Override
    public void loadError(Throwable throwable) {
        includeLoadFail.setVisibility(View.VISIBLE);
        includeNoData.setVisibility(View.GONE);
        mMsgRecyclerView.setVisibility(View.GONE);

        mMsgSwipeLayout.setRefreshing(false);
        throwable.printStackTrace();
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
    }

    @Override
    public void setListResult(ResMessageList resMessageList) {
        isLoad = false;
        mMsgSwipeLayout.setRefreshing(false);
        //TODO 填充数据
        if (resMessageList.getRetCode() == 0) {
            includeNoData.setVisibility(View.GONE);
            includeLoadFail.setVisibility(View.GONE);
            mMsgRecyclerView.setVisibility(View.VISIBLE);
            if (resMessageList.getRetObj().getRows() != null && resMessageList.getRetObj().getRows().size() > 0) {
                this.mMessages.addAll(resMessageList.getRetObj().getRows());
                if (adapter == null) {
                    adapter = new MessageAdapter(this, mMessages);
                    mMsgRecyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
            //存储上一次的消息总数
            SharedPreferencesUtil.setLong(this, Constant.KEY_MESSAGE_TOTAL_COUNT, resMessageList.getRetObj().getTotal());
            //触发一次更新 消息数量
            new Handler().postDelayed(() -> EventBus.getDefault().post(new MineEvent(2)), 1500);
            if (this.mMessages.size() == resMessageList.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else if (resMessageList.getRetCode() == 10013) {//暂无数据
            includeNoData.setVisibility(View.VISIBLE);
            includeLoadFail.setVisibility(View.GONE);
            mMsgRecyclerView.setVisibility(View.GONE);
        } else {
            includeNoData.setVisibility(View.GONE);
            includeLoadFail.setVisibility(View.VISIBLE);
            mMsgRecyclerView.setVisibility(View.GONE);
            T.ShowToast(this, resMessageList.getRetMsg());
        }
    }

    @Override
    public String getUserId() {
        return SharedPreferencesUtil.getString(this, Constant.USER_ID, "");
    }

    @Override
    public int getPageNum() {
        return pageNum;
    }

    /**
     * 下拉刷新
     */
    @Override
    protected void onRefresh() {
        pageNum = 1;
        isLoad = true;
        isEnd = false;
        if (mMessages != null) {
            mMessages.clear();
        }
        mMsgSwipeLayout.setRefreshing(true);
        ((MessagePresenter) mPresenter).getList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 上拉加载更多
     */
    @Override
    protected void loadMore() {
        if (!isEnd && !isLoad) {
            mMsgSwipeLayout.setRefreshing(true);
            pageNum++;
            ((MessagePresenter) mPresenter).getList();
        }
    }
}
