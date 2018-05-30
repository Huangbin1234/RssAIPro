package com.hb.rssai.view.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.RecordAdapter;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResInfo;
import com.hb.rssai.bean.ResUserInformation;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.RecordPresenter;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.common.RichTextActivity;
import com.hb.rssai.view.iView.IRecordView;

import java.util.ArrayList;
import java.util.List;

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
    private RecordAdapter adapter;
    private int pageNum = 1;
    private boolean isEnd = false, isLoad = false;
    private List<ResUserInformation.RetObjBean.RowsBean> infoList = new ArrayList<>();
    private ResUserInformation.RetObjBean.RowsBean bean;
    private String infoId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RecordPresenter) mPresenter).getList();
    }

    @Override
    protected void onRefresh() {
        pageNum = 1;
        isLoad = true;
        isEnd = false;
        if (infoList != null) {
            infoList.clear();
        }
        mRecordSwipeLayout.setRefreshing(true);
        ((RecordPresenter) mPresenter).getList();
    }

    @Override
    protected void loadMore() {
        if (!isEnd && !isLoad) {
            mRecordSwipeLayout.setRefreshing(true);
            pageNum++;
            ((RecordPresenter) mPresenter).getList();
        }
    }

    @Override
    protected void initView() {
        mLayoutManager = new LinearLayoutManager(this);
        mRecordRecyclerView.setLayoutManager(mLayoutManager);
        mRecordSwipeLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        mRecordSwipeLayout.setProgressViewOffset(true, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        mLlfBtnReTry.setOnClickListener(v -> ((RecordPresenter) mPresenter).getList());

        mRecordSwipeLayout.setOnRefreshListener(() -> onRefresh());
        //TODO 设置上拉加载更多
        mRecordRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (adapter == null) {
                    isLoad = false;
                    mRecordSwipeLayout.setRefreshing(false);
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
        return new RecordPresenter(this);
    }

    @Override
    public void loadError(Throwable throwable) {
        includeLoadFail.setVisibility(View.VISIBLE);
        includeNoData.setVisibility(View.GONE);
        mRecordRecyclerView.setVisibility(View.GONE);

        mRecordSwipeLayout.setRefreshing(false);
        throwable.printStackTrace();
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
    }

    @Override
    public void setInfoResult(ResInfo resInfo) {
        if (resInfo.getRetCode() == 0) {
            Intent intent = new Intent(this, RichTextActivity.class);
            intent.putExtra("abstractContent", resInfo.getRetObj().getAbstractContent());
            intent.putExtra(ContentActivity.KEY_TITLE, resInfo.getRetObj().getTitle());
            intent.putExtra("whereFrom", resInfo.getRetObj().getWhereFrom());
            intent.putExtra("pubDate", resInfo.getRetObj().getPubTime());
            intent.putExtra("url", resInfo.getRetObj().getLink());
            intent.putExtra("id", resInfo.getRetObj().getId());
            intent.putExtra("clickGood", resInfo.getRetObj().getClickGood());
            intent.putExtra("clickNotGood", resInfo.getRetObj().getClickNotGood());
            startActivity(intent);
        } else {
//            T.ShowToast(mContext, "抱歉，文章链接已失效，无法打开！");
            Intent intent = new Intent(this, ContentActivity.class);
            intent.putExtra(ContentActivity.KEY_URL, bean.getInformationLink());
            intent.putExtra(ContentActivity.KEY_TITLE, bean.getInformationTitle());
            intent.putExtra(ContentActivity.KEY_INFORMATION_ID, bean.getInformationId());
            startActivity(intent);
        }
    }

    @Override
    public void setListResult(ResUserInformation resUserInformation) {
        isLoad = false;
        mRecordSwipeLayout.setRefreshing(false);
        //TODO 填充数据
        if (resUserInformation.getRetCode() == 0) {
            includeNoData.setVisibility(View.GONE);
            includeLoadFail.setVisibility(View.GONE);
            mRecordRecyclerView.setVisibility(View.VISIBLE);
            if (resUserInformation.getRetObj().getRows() != null && resUserInformation.getRetObj().getRows().size() > 0) {
                this.infoList.addAll(resUserInformation.getRetObj().getRows());
                if (adapter == null) {
                    adapter = new RecordAdapter(this, infoList);
                    mRecordRecyclerView.setAdapter(adapter);
                    adapter.setMyOnItemClickedListener(rowsBean -> {
                        bean = rowsBean;
                        if (!TextUtils.isEmpty(rowsBean.getInformationId())) {
                            infoId = rowsBean.getInformationId();
                            ((RecordPresenter) mPresenter).getInformation(); //获取消息
                        } else {
                            Intent intent = new Intent(RecordActivity.this, ContentActivity.class);
                            intent.putExtra(ContentActivity.KEY_URL, rowsBean.getInformationLink());
                            intent.putExtra(ContentActivity.KEY_TITLE, rowsBean.getInformationTitle());
                            intent.putExtra(ContentActivity.KEY_INFORMATION_ID, rowsBean.getInformationId());
                            startActivity(intent);
                        }
                    });
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
            if (this.infoList.size() == resUserInformation.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else if (resUserInformation.getRetCode() == 10013) {//暂无数据
            includeNoData.setVisibility(View.VISIBLE);
            includeLoadFail.setVisibility(View.GONE);
            mRecordRecyclerView.setVisibility(View.GONE);
        } else {
            includeNoData.setVisibility(View.GONE);
            includeLoadFail.setVisibility(View.VISIBLE);
            mRecordRecyclerView.setVisibility(View.GONE);
            T.ShowToast(this, resUserInformation.getRetMsg());
        }
    }

    @Override
    public String getUserId() {
        return SharedPreferencesUtil.getString(this, Constant.USER_ID, "");
    }

    @Override
    public String getInfoId() {
        return infoId;
    }

    @Override
    public int getPageNum() {
        return pageNum;
    }
}
