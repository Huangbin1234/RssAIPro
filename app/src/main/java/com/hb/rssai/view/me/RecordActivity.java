package com.hb.rssai.view.me;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.RecordPresenter;
import com.hb.rssai.view.iView.IRecordView;

import butterknife.BindView;

public class RecordActivity extends BaseActivity implements IRecordView {

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
    @BindView(R.id.record_recycler_view)
    RecyclerView mRecordRecyclerView;
    @BindView(R.id.record_swipe_layout)
    SwipeRefreshLayout mRecordSwipeLayout;
    @BindView(R.id.include_no_data)
    View includeNoData;
    @BindView(R.id.include_load_fail)
    View includeLoadFail;
    @BindView(R.id.llf_btn_re_try)
    Button mLlfBtnReTry;

    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RecordPresenter) mPresenter).getList();
    }

    @Override
    protected void initView() {
        mLayoutManager = new LinearLayoutManager(this);
        mRecordRecyclerView.setLayoutManager(mLayoutManager);
        mRecordSwipeLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        mRecordSwipeLayout.setProgressViewOffset(true, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        mLlfBtnReTry.setOnClickListener(v -> ((RecordPresenter) mPresenter).getList());
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_record;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_record_title));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected BasePresenter createPresenter() {
        return new RecordPresenter(this, this);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecordRecyclerView;
    }

    @Override
    public SwipeRefreshLayout getSwipeLayout() {
        return mRecordSwipeLayout;
    }

    @Override
    public LinearLayoutManager getManager() {
        return mLayoutManager;
    }

    @Override
    public View getIncludeNoData() {
        return includeNoData;
    }

    @Override
    public View getIncludeLoadFail() {
        return includeLoadFail;
    }
}
