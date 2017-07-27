package com.hb.rssai.view.subscription;

import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.SubListAdapter;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.RssChannel;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.widget.PrgDialog;
import com.rss.bean.Website;
import com.rss.util.FeedReader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SubListActivity extends BaseActivity implements SubListAdapter.onItemLongClickedListener {
    LinearLayoutManager mLayoutManager;
    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.sub_tv_empty)
    TextView mSubTvEmpty;
    @BindView(R.id.sub_ll)
    LinearLayout mSubLl;
    @BindView(R.id.sub_recycler_view)
    RecyclerView mSubRecyclerView;
    @BindView(R.id.sub_swipe_layout)
    SwipeRefreshLayout mSubSwipeLayout;

    private SubListAdapter mSubListAdapter;
    private List<RssSource> list = new ArrayList<>();
    private PrgDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        if (list != null && list.size() > 0) {
            list.clear();
        }
        if (mSubListAdapter != null) {
            mSubListAdapter.notifyDataSetChanged();
        }
        List<RssSource> dbList = LiteOrmDBUtil.getQueryAll(RssSource.class);
        if (dbList == null || dbList.size() <= 0) {
            return;
        }
        list.addAll(dbList);
        new ReadRssTask().execute();
    }

    @Override
    protected void initView() {
        mLayoutManager = new LinearLayoutManager(this);
        mSubRecyclerView.setLayoutManager(mLayoutManager);
        mSubSwipeLayout.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mSubSwipeLayout.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_sub_list;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_sub_title));
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
    @Override
    public void onItemLongClicked(RssSource rssSource) {
        //TODO
        T.ShowToast(this, "置顶，删除");
    }

    class ReadRssTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new PrgDialog(SubListActivity.this, true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            readRssXml();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.closeDialog();
            mSubSwipeLayout.setRefreshing(false);
            mSubLl.setVisibility(View.GONE);
            if (mSubListAdapter == null) {
                mSubListAdapter = new SubListAdapter(SubListActivity.this, list, SubListActivity.this);
                mSubRecyclerView.setAdapter(mSubListAdapter);
            } else {
                mSubListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void readRssXml() {
        List<Website> websiteList = new ArrayList<>();
        for (RssSource rssSource : list) {
            Website website = new Website();
            website.setUrl(rssSource.getLink());
            website.setName(rssSource.getName());
            website.setOpen("true");
            website.setEncoding("UTF-8");
            website.setStartTag("");
            website.setEndTag("");
            website.setFid("" + rssSource.getId());
            websiteList.add(website);
        }
        for (Website we : websiteList) {
            rssInsert(we);
        }
    }


    /**
     * 可以选择插入到数据库
     *
     * @param website
     */
    String[] urls = {"http://icon.nipic.com/BannerPic/20170531/home/20170531103230.jpg", "http://icon.nipic.com/BannerPic/20170509/home/20170509164717.jpg", "http://icon.nipic.com/BannerPic/20170619/home/20170619151644.jpg", "http://icon.nipic.com/BannerPic/20170531/home/20170531103230.jpg", "http://icon.nipic.com/BannerPic/20170509/home/20170509164717.jpg", "http://icon.nipic.com/BannerPic/20170619/home/20170619151644.jpg", "http://icon.nipic.com/BannerPic/20170531/home/20170531103230.jpg", "http://icon.nipic.com/BannerPic/20170509/home/20170509164717.jpg", "http://icon.nipic.com/BannerPic/20170619/home/20170619151644.jpg", "http://icon.nipic.com/BannerPic/20170509/home/20170509164717.jpg", "http://icon.nipic.com/BannerPic/20170619/home/20170619151644.jpg", "http://icon.nipic.com/BannerPic/20170509/home/20170509164717.jpg", "http://icon.nipic.com/BannerPic/20170619/home/20170619151644.jpg", "http://icon.nipic.com/BannerPic/20170509/home/20170509164717.jpg", "http://icon.nipic.com/BannerPic/20170619/home/20170619151644.jpg"};

    public void rssInsert(Website website) {
        try {
            RssChannel rssTempList = new FeedReader().getContent(website);
            if (rssTempList != null && rssTempList.getRSSItemBeen().size() > 0) {
                int len = list.size();
                for (int i = 0; i < len; i++) {
                    if (website.getFid().equals("" + list.get(i).getId())) {
                        list.get(i).setCount(rssTempList.getRSSItemBeen().size());
//                        list.get(i).setImgUrl(urls[i]);
                        if (rssTempList.getImage() != null && rssTempList.getImage().getUrl() != null) {
                            list.get(i).setImgUrl(rssTempList.getImage().getUrl());
                            if (rssTempList.getImage().getTitle() != null) {
                                list.get(i).setName(rssTempList.getTitle());
                            }
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.closeDialog();
        }
    }
}
