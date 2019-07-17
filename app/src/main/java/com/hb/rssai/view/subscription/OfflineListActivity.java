package com.hb.rssai.view.subscription;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.OfflineListAdapter;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.contract.OfflineListContract;
import com.hb.rssai.presenter.OfflineListPresenter;
import com.hb.rssai.util.T;
import com.hb.rssai.view.widget.MyDecoration;
import com.rss.bean.Information;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.google.common.base.Preconditions.checkNotNull;

public class OfflineListActivity extends BaseActivity<OfflineListContract.View, OfflineListPresenter> implements OfflineListContract.View {
    public static final String KEY_LINK = "key_link";
    public static final String KEY_NAME = "key_name";
    public static final String KEY_SUBSCRIBE_ID = "key_subscribe_id";
    public static final String KEY_IS_TAG = "key_is_tag";
    public static final String KEY_IMG="key_img";
    OfflineListContract.Presenter mPresenter;
    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.oal_ll)
    LinearLayout mOalLl;
    @BindView(R.id.llf_btn_re_try)
    Button mLlfBtnReTry;
    @BindView(R.id.oal_recycler_view)
    RecyclerView mOalRecyclerView;
    @BindView(R.id.oal_swipe_layout)
    SwipeRefreshLayout mOalSwipeLayout;
    @BindView(R.id.llRootView)
    LinearLayout mLlRootView;
    @BindView(R.id.include_no_data)
    LinearLayout include_no_data;
    @BindView(R.id.include_load_fail)
    LinearLayout include_load_fail;

    private LinearLayoutManager manager;
    private String link;
    private String name;
    private OfflineListAdapter adapter;
    private List<Information> infoList = new ArrayList<>();
    private String subscribeId;
    private boolean isTag=false;
    private String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        if (!TextUtils.isEmpty(link)) {
            mOalSwipeLayout.setRefreshing(true);
            mPresenter.getList(link,subscribeId,isTag,img);
        }
    }

    @Override
    protected void initIntent() {
        super.initIntent();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            link = bundle.getString(KEY_LINK, "");
            subscribeId = bundle.getString(KEY_SUBSCRIBE_ID, "");
            name = bundle.getString(KEY_NAME, "");
            isTag = bundle.getBoolean(KEY_IS_TAG, false);
            img = bundle.getString(KEY_IMG, "");
            if (!TextUtils.isEmpty(name)) {
                mSysTvTitle.setText(name);
            }
        }
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_offline_list;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_offline_list_title));
    }

    @Override
    protected OfflineListPresenter createPresenter() {
        return new OfflineListPresenter(this);
    }

    @Override
    protected void initView() {
        manager = new LinearLayoutManager(this);
        mOalRecyclerView.setLayoutManager(manager);
        mOalRecyclerView.setHasFixedSize(true);
        mOalRecyclerView.addItemDecoration(new MyDecoration(this, LinearLayoutManager.VERTICAL));

        mOalSwipeLayout.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mOalSwipeLayout.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        mLlfBtnReTry.setOnClickListener(v -> loadData());
        //设置上下拉刷新
        mOalSwipeLayout.setOnRefreshListener(() -> loadData());
    }

    @Override
    public void setPresenter(OfflineListContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }


    @Override
    public void showList(List<Information> list) {
        runOnUiThread(() -> {
            if (infoList != null && infoList.size() > 0) {
                infoList.clear();
            }
            mOalLl.setVisibility(View.GONE);
            include_no_data.setVisibility(View.GONE);
            include_load_fail.setVisibility(View.GONE);

            mOalRecyclerView.setVisibility(View.VISIBLE);
            infoList.addAll(list);
            if (adapter == null) {
                adapter = new OfflineListAdapter(OfflineListActivity.this, infoList);
                mOalRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                adapter.init();//更新一下是否显示图片首选项
                adapter.notifyDataSetChanged();
            }
            //通知更新
            mOalSwipeLayout.setRefreshing(false);
        });
    }

    @Override
    public void showError() {
        runOnUiThread(() -> {
            mOalSwipeLayout.setRefreshing(false);
            mOalLl.setVisibility(View.GONE);
            mOalRecyclerView.setVisibility(View.GONE);
            include_no_data.setVisibility(View.GONE);
            include_load_fail.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void showNoData() {
        runOnUiThread(() -> {
            mOalSwipeLayout.setRefreshing(false);
            mOalLl.setVisibility(View.GONE);
            mOalRecyclerView.setVisibility(View.GONE);
            include_no_data.setVisibility(View.VISIBLE);
            include_load_fail.setVisibility(View.GONE);
            T.ShowToast(this, "暂无更多数据");
        });
    }
}
