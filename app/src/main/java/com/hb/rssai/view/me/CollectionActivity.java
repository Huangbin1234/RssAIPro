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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.CollectionAdapter;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.UserCollection;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.util.LiteOrmDBUtil;

import java.util.List;

import butterknife.BindView;

public class CollectionActivity extends BaseActivity {


    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.coll_tv_empty)
    TextView mCollTvEmpty;
    @BindView(R.id.coll_ll)
    LinearLayout mCollLl;
    @BindView(R.id.coll_recycler_view)
    RecyclerView mCollRecyclerView;
    @BindView(R.id.coll_swipe_layout)
    SwipeRefreshLayout mCollSwipeLayout;
    private LinearLayoutManager mLayoutManager;
    CollectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        List<UserCollection> dbList = LiteOrmDBUtil.getQueryAll(UserCollection.class);
        if (dbList == null || dbList.size() <= 0) {
            return;
        }
        if (adapter == null) {
            adapter = new CollectionAdapter(this, dbList);
        }
        mCollRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initView() {
        mLayoutManager = new LinearLayoutManager(this);
        mCollRecyclerView.setLayoutManager(mLayoutManager);
        mCollSwipeLayout.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mCollSwipeLayout.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_collection;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_coll_title));
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
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
}