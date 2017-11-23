package com.hb.rssai.view.subscription;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.DialogAdapter;
import com.hb.rssai.adapter.SubListAdapter;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.bean.RssChannel;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.SubListPresenter;
import com.hb.rssai.util.Base64Util;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.QrCodeActivity;
import com.hb.rssai.view.iView.ISubListView;
import com.hb.rssai.view.widget.FullListView;
import com.hb.rssai.view.widget.MyDecoration;
import com.hb.rssai.view.widget.PrgDialog;
import com.rss.bean.Website;
import com.rss.util.FeedReader;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import me.drakeet.materialdialog.MaterialDialog;

public class SubListActivity extends BaseActivity implements ISubListView {
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
    @BindView(R.id.include_no_data)
    View includeNoData;
    @BindView(R.id.include_load_fail)
    View includeLoadFail;
    @BindView(R.id.llf_btn_re_try)
    Button mLlfBtnReTry;

    private SubListAdapter mSubListAdapter;
    private List<RssSource> list = new ArrayList<>();
    private PrgDialog dialog;
    private Object isTag = null;
    public static final String KEY_IS_TAG = "key_is_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    protected void initIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isTag = bundle.getBoolean(KEY_IS_TAG);
        }
    }

    private void initData() {
        ((SubListPresenter) mPresenter).refreshList();
//        if (list != null && list.size() > 0) {
//            list.clear();
//        }
//        if (mSubListAdapter != null) {
//            mSubListAdapter.notifyDataSetChanged();
//        }
//        List<RssSource> dbList = LiteOrmDBUtil.getQueryAllSort(RssSource.class, "sort");
//        if (dbList == null || dbList.size() <= 0) {
//            return;
//        }
//        list.addAll(dbList);
//        new ReadRssTask().execute();
    }

//    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//            switch (menuItem.getItemId()) {
//                case R.id.sm_sub_search:
//                    //TODO
//                    Intent intent = new Intent(SubListActivity.this, SearchActivity.class);
//                    startActivity(intent);
//                    break;
//            }
//            return true;
//        }
//    };

    @Override
    protected void initView() {
        mLayoutManager = new LinearLayoutManager(this);
        mSubRecyclerView.setLayoutManager(mLayoutManager);
        mSubRecyclerView.setHasFixedSize(true);
        mSubRecyclerView.addItemDecoration(new MyDecoration(this, LinearLayoutManager.VERTICAL));

        mSubSwipeLayout.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mSubSwipeLayout.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        mLlfBtnReTry.setOnClickListener(v -> ((SubListPresenter) mPresenter).refreshList());
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
//        mSysToolbar.setOnMenuItemClickListener(onMenuItemClick);
    }

    @Override
    protected BasePresenter createPresenter() {
        return new SubListPresenter(this, this);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.sub_menu, menu);
//        return true;
//    }

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

    ResFindMore.RetObjBean.RowsBean rssSourceNew;

//    @Override
//    public void onItemLongClicked(ResFindMore.RetObjBean.RowsBean rssSource) {
//        //TODO
//        this.rssSourceNew = rssSource;
//        openMenu();
//    }

    /**
     * 构造对话框数据
     *
     * @return
     */
    private List<HashMap<String, Object>> initDialogData() {
        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("rssTitle", "置顶");
        map.put("id", 1);
        map.put("url", R.mipmap.ic_top);
        list.add(map);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("rssTitle", "分享");
        map2.put("id", 2);
        map2.put("url", R.mipmap.ic_share);
        list.add(map2);

        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("rssTitle", "删除");
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

    private void openMenu() {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.view_dialog, null);
            FullListView listView = (FullListView) view.findViewById(R.id.dialog_listView);

            List<HashMap<String, Object>> list = initDialogData();
            listView.setOnItemClickListener((parent, view1, position, id) -> {
                if (list.get(position).get("id").equals(1)) {
                    //TODO 置顶
                    materialDialog.dismiss();
                    rssSourceNew.setSort(new Date().getTime());
                    LiteOrmDBUtil.update(rssSourceNew);
                    initData();
                } else if (list.get(position).get("id").equals(2)) {
                    materialDialog.dismiss();
                    Intent intent = new Intent(this, QrCodeActivity.class);
                    intent.putExtra(QrCodeActivity.KEY_FROM, QrCodeActivity.FROM_VALUES[0]);
                    intent.putExtra(QrCodeActivity.KEY_TITLE, rssSourceNew.getName());
                    intent.putExtra(QrCodeActivity.KEY_CONTENT, Base64Util.getEncodeStr(Constant.FLAG_RSS_SOURCE + rssSourceNew.getLink()));
                    startActivity(intent);
                } else if (list.get(position).get("id").equals(3)) {
                    materialDialog.dismiss();
                    LiteOrmDBUtil.deleteWhere(RssSource.class, "id", new String[]{"" + rssSourceNew.getId()});
                    initData();
                    T.ShowToast(this, "删除成功！");
                }
            });
            if (dialogAdapter == null) {
                dialogAdapter = new DialogAdapter(this, list);
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

    @Override
    public RecyclerView getRecyclerView() {
        return mSubRecyclerView;
    }

    @Override
    public LinearLayoutManager getManager() {
        return mLayoutManager;
    }

    @Override
    public SwipeRefreshLayout getSwipeLayout() {
        return mSubSwipeLayout;
    }

    @Override
    public Object getIsTag() {
        return isTag;
    }

    @Override
    public LinearLayout getLlEmptyView() {
        return mSubLl;
    }


//    class ReadRssTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            dialog = new PrgDialog(SubListActivity.this, true);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            readRssXml();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            dialog.closeDialog();
//            mSubSwipeLayout.setRefreshing(false);
//            mSubLl.setVisibility(View.GONE);
//            if (mSubListAdapter == null) {
//                mSubListAdapter = new SubListAdapter(SubListActivity.this, list, SubListActivity.this);
//                mSubRecyclerView.setAdapter(mSubListAdapter);
//            } else {
//                mSubListAdapter.notifyDataSetChanged();
//            }
//        }
//    }

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
    public View getIncludeNoData() {
        return includeNoData;
    }

    @Override
    public View getIncludeLoadFail() {
        return includeLoadFail;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.closeDialog();
        }
    }
}
