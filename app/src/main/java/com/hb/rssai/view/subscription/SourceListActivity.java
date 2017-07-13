package com.hb.rssai.view.subscription;

import android.os.AsyncTask;
import android.os.Bundle;
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
import com.hb.rssai.adapter.RssListAdapter;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.view.widget.PrgDialog;
import com.rss.bean.RSSItemBean;
import com.rss.bean.Website;
import com.rss.util.FeedReader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SourceListActivity extends BaseActivity {

    public static final String KEY_LINK = "link";
    public static final String KEY_TITLE ="name" ;
    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.sla_tv_empty)
    TextView mSlaTvEmpty;
    @BindView(R.id.sla_ll)
    LinearLayout mSlaLl;
    @BindView(R.id.sla_recycler_view)
    RecyclerView mSlaRecyclerView;
    @BindView(R.id.sla_swipe_layout)
    SwipeRefreshLayout mSlaSwipeLayout;
    private LinearLayoutManager mLayoutManager;
    private String linkValue;
    private ArrayList<RSSItemBean> rssList = new ArrayList<>();
    private PrgDialog slaDialog;
    RssListAdapter rssListAdapter;
    private String titleValue="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        new ReadRssTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    protected void initView() {
        mSysTvTitle.setText(titleValue);

        mLayoutManager = new LinearLayoutManager(this);
        mSlaRecyclerView.setLayoutManager(mLayoutManager);
        mSlaSwipeLayout.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mSlaSwipeLayout.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_source_list;
    }

    @Override
    protected void initIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            linkValue = bundle.getString(KEY_LINK, "");
            titleValue = bundle.getString(KEY_TITLE, "");
        }
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
        return null;
    }

    class ReadRssTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            slaDialog = new PrgDialog(SourceListActivity.this, true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            readRssXml();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mSlaLl.setVisibility(View.GONE);
            slaDialog.closeDialog();
            if (rssList != null && rssList.size() > 0) {
                if (rssListAdapter == null) {
                    rssListAdapter = new RssListAdapter(SourceListActivity.this, rssList);
                }
                mSlaRecyclerView.setAdapter(rssListAdapter);
            }
        }
    }

    private void readRssXml() {
        Website website = new Website();
        website.setUrl(linkValue);
        website.setName("12");
        website.setOpen("true");
        website.setEncoding("UTF-8");
        website.setStartTag("");
        website.setEndTag("");
        website.setFid("67");
        rssInsert(website);
    }

    /**
     * 可以选择插入到数据库
     *
     * @param website
     */
    public void rssInsert(Website website) {
        try {
            List<RSSItemBean> rssTempList = new FeedReader().getContent(website);                   //获取有内容的 rssItemBean
            if (rssTempList != null) {
                rssList.addAll(rssTempList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (slaDialog != null) {
            slaDialog.closeDialog();
        }
    }
}
