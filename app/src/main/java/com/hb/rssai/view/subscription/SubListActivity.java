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
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.RssSourceEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.SubListPresenter;
import com.hb.rssai.util.Base64Util;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.QrCodeActivity;
import com.hb.rssai.view.iView.ISubListView;
import com.hb.rssai.view.widget.FullListView;
import com.hb.rssai.view.widget.MyDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import me.drakeet.materialdialog.MaterialDialog;
import retrofit2.adapter.rxjava.HttpException;

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

    private List<RssSource> list = new ArrayList<>();
    private Object isTag = null;
    public static final String KEY_IS_TAG = "key_is_tag";

    private List<ResFindMore.RetObjBean.RowsBean> resLists = new ArrayList<>();
    private int pageNum = 1;
    private boolean isEnd = false, isLoad = false;
    private SubListAdapter mSubListAdapter;

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
        ((SubListPresenter) mPresenter).getUserSubscribeList();
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

    @Override
    protected void onRefresh() {
        pageNum = 1;
        isLoad = true;
        isEnd = false;

        mSubSwipeLayout.setRefreshing(true);
        ((SubListPresenter) mPresenter).getUserSubscribeList();
    }

    @Override
    protected void loadMore() {
        if (!isEnd && !isLoad) {
            mSubSwipeLayout.setRefreshing(true);
            pageNum++;
            ((SubListPresenter) mPresenter).getUserSubscribeList();
        }
    }

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

        mLlfBtnReTry.setOnClickListener(v -> onRefresh());

        //TODO 设置下拉刷新
        mSubSwipeLayout.setOnRefreshListener(() -> onRefresh());
        //TODO 设置上拉加载更多
        mSubRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mSubListAdapter == null) {
                    isLoad = false;
                    mSubSwipeLayout.setRefreshing(false);
                    return;
                }
                // 在最后两条的时候就自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= mSubListAdapter.getItemCount()) {
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
        return new SubListPresenter(this);
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
            materialDialog = new MaterialDialog(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.view_dialog, null);
            FullListView listView = (FullListView) view.findViewById(R.id.dialog_listView);

            List<HashMap<String, Object>> list = initDialogData();
            listView.setOnItemClickListener((parent, view1, position, id) -> {
                if (list.get(position).get("id").equals(1)) {
                    //TODO 置顶
                    materialDialog.dismiss();
                    ((SubListPresenter) mPresenter).updateUsSort();
                } else if (list.get(position).get("id").equals(2)) {
                    materialDialog.dismiss();
                    Intent intent = new Intent(this, QrCodeActivity.class);
                    intent.putExtra(QrCodeActivity.KEY_FROM, QrCodeActivity.FROM_VALUES[0]);
                    intent.putExtra(QrCodeActivity.KEY_TITLE, mClickBean.getName());
                    intent.putExtra(QrCodeActivity.KEY_CONTENT, Base64Util.getEncodeStr(Constant.FLAG_RSS_SOURCE + mClickBean.getLink()));
                    startActivity(intent);
                } else if (list.get(position).get("id").equals(3)) {
                    materialDialog.dismiss();
                    ((SubListPresenter) mPresenter).delSubscription();
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
    public Object getIsTag() {
        return isTag;
    }

    @Override
    public void loadError(Throwable throwable) {
        includeLoadFail.setVisibility(View.VISIBLE);
        includeNoData.setVisibility(View.GONE);
        mSubRecyclerView.setVisibility(View.GONE);

        mSubSwipeLayout.setRefreshing(false);
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            if (((HttpException) throwable).response().code() == 401) {
                T.ShowToast(this, Constant.MSG_NO_LOGIN);
            } else {
                T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
            }
        } else {
            T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
        }
        mSubLl.setVisibility(View.GONE);
    }

    @Override
    public String getUserId() {
        return SharedPreferencesUtil.getString(this, Constant.USER_ID, "");
    }

    @Override
    public void setDelResult(ResBase resBase) {
        if (resBase.getRetCode() == 0) {
            EventBus.getDefault().post(new RssSourceEvent(0));
            onRefresh();
        }
        T.ShowToast(this, resBase.getRetMsg());
    }

    @Override
    public void setUpdateUsSortResult(ResBase resBase) {
        if (resBase.getRetCode() == 0) {
            EventBus.getDefault().post(new RssSourceEvent(0));
            onRefresh();
        }
        T.ShowToast(this, resBase.getRetMsg());
    }

    @Override
    public ResFindMore.RetObjBean.RowsBean getClickBean() {
        return mClickBean;
    }

    @Override
    public int getPageNum() {
        return pageNum;
    }

    @Override
    public void setUserSubscribeResult(ResFindMore resFindMore) {
        if (resLists != null && pageNum == 1) {
            resLists.clear();
        }
        isLoad = false;
        mSubSwipeLayout.setRefreshing(false);
        mSubLl.setVisibility(View.GONE);
        //TODO 填充数据
        if (resFindMore.getRetCode() == 0) {
            includeNoData.setVisibility(View.GONE);
            includeLoadFail.setVisibility(View.GONE);
            mSubRecyclerView.setVisibility(View.VISIBLE);

            if (resFindMore.getRetObj().getRows() != null && resFindMore.getRetObj().getRows().size() > 0) {
                resLists.addAll(resFindMore.getRetObj().getRows());
                if (mSubListAdapter == null) {
                    mSubListAdapter = new SubListAdapter(this, resLists, this);
                    mSubRecyclerView.setAdapter(mSubListAdapter);
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
        } else if (resFindMore.getRetCode() == 10013) {//暂无数据
            includeNoData.setVisibility(View.VISIBLE);
            includeLoadFail.setVisibility(View.GONE);
            mSubRecyclerView.setVisibility(View.GONE);
        } else {
            includeNoData.setVisibility(View.GONE);
            includeLoadFail.setVisibility(View.VISIBLE);
            mSubRecyclerView.setVisibility(View.GONE);
            T.ShowToast(this, resFindMore.getRetMsg());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
