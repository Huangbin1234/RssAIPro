package com.hb.rssai.view.subscription;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.SearchSubscribeAdapter;
import com.hb.rssai.app.ProjectApplication;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.contract.AddSourceLikeContract;
import com.hb.rssai.presenter.AddSourceLikePresenterImpl;
import com.hb.rssai.util.CommonHandler;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddSourceLikeActivity extends BaseActivity<AddSourceLikeContract.View, AddSourceLikePresenterImpl> implements AddSourceLikeContract.View {

    AddSourceLikeContract.Presenter mPresenter;
    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.search_subscribe_recycler_view)
    RecyclerView mSearchSubscribeRecyclerView;
    @BindView(R.id.activity_add_source)
    LinearLayout mActivityAddSource;
    private LinearLayoutManager mLinearLayoutManager;
    private int page = 1;
    private SearchSubscribeAdapter mAdapter;
    private List<ResFindMore.RetObjBean.RowsBean> resSubscribes=new ArrayList<>();
    private boolean isEnd = false, isLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshInfoList(keyWord);
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_add_source_like;
    }

    @Override
    protected void setAppTitle() {

    }

    @Override
    protected AddSourceLikePresenterImpl createPresenter() {
        return new AddSourceLikePresenterImpl(this);
    }

    @Override
    protected void initView() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        //TODO 设置上拉加载更多
        mSearchSubscribeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mAdapter == null) {
                    isLoad = false;
                    return;
                }
                // 在最后两条的时候就自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= mAdapter.getItemCount()) {
                    // 加载更多
                    if (!isEnd && !isLoad) {
                        page++;
                       refreshInfoList(keyWord);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }
    /**
     * 刷新数据
     */
    private String keyWord = "";

    public void refreshInfoList(String val) {
        keyWord = val;
        page = 1;
        isLoad = true;
        isEnd = false;
        if (resSubscribes != null) {
            resSubscribes.clear();
        }
       mPresenter.getSubscribeLike(keyWord,page);
    }

    @Override
    public void showSubscribeLike(ResFindMore resFindMore) {
        isLoad = false;
        if (resFindMore.getRetCode() == 0) {
            if (resFindMore.getRetObj().getRows() != null && resFindMore.getRetObj().getRows().size() > 0) {
                resSubscribes.addAll(resFindMore.getRetObj().getRows());
                if (mAdapter == null) {
                    mAdapter = new SearchSubscribeAdapter(this, resSubscribes);
                    mAdapter.setOnItemClickedListener(rowsBean1 -> {

                        Intent intent = new Intent(this, SourceCardActivity.class);
                        intent.putExtra(SourceCardActivity.KEY_LINK, rowsBean1.getLink());
                        intent.putExtra(SourceCardActivity.KEY_TITLE, rowsBean1.getName());
                        intent.putExtra(SourceCardActivity.KEY_SUBSCRIBE_ID, rowsBean1.getId());
                        intent.putExtra(SourceCardActivity.KEY_IMAGE, rowsBean1.getImg());
                        intent.putExtra(SourceCardActivity.KEY_DESC, rowsBean1.getAbstractContent());
                        intent.putExtra(SourceCardActivity.KEY_IS_CHECK,  rowsBean1.isCheck());
                        this.startActivity(intent);
                    });
                    mSearchSubscribeRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
                if (resSubscribes.size() == resFindMore.getRetObj().getTotal()) {
                    isEnd = true;
                }
            }
        } else {
            T.ShowToast(this, resFindMore.getRetMsg());
        }
    }

    @Override
    public void setPresenter(AddSourceLikeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }



    @Override
    public void showFail(Throwable throwable) {
        CommonHandler.actionThrowable(throwable);
    }
}
