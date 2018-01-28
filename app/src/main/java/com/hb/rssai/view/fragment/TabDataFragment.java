package com.hb.rssai.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseFragment;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.TabDataPresenter;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.view.iView.ITabDataView;
import com.hb.rssai.view.widget.MyDecoration;

import butterknife.BindView;


public class TabDataFragment extends BaseFragment implements ITabDataView {
    public static final String ARGS_PAGE = "args_page";
    @BindView(R.id.ftd_recycler_view)
    RecyclerView mFtdRecyclerView;
    @BindView(R.id.ftd_swipe_layout)
    SwipeRefreshLayout mFtdSwipeLayout;

    @BindView(R.id.hf_ll)
    LinearLayout mHfLl;
    @BindView(R.id.include_no_data)
    LinearLayout include_no_data;
    @BindView(R.id.include_load_fail)
    LinearLayout include_load_fail;
    @BindView(R.id.llf_btn_re_try)
    Button mLlfBtnReTry;

    private LinearLayoutManager manager;
    private int dataType;
    private boolean isUser;
    private int DF;
    private boolean isPrepared;

    public static TabDataFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE, page);
        TabDataFragment fragment = new TabDataFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataType = getArguments().getInt(ARGS_PAGE);
    }

    @Override
    protected void setAppTitle() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return new TabDataPresenter(getContext(), this);
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.fragment_tab_data;
    }

    @Override
    protected void initView(View rootView) {
        manager = new LinearLayoutManager(getContext());
        mFtdRecyclerView.setLayoutManager(manager);
        //mRecyclerView.setNestedScrollingEnabled(false);//解决卡顿
        mFtdRecyclerView.setHasFixedSize(true);
        mFtdRecyclerView.addItemDecoration(new MyDecoration(getContext(), LinearLayoutManager.VERTICAL));

        mFtdSwipeLayout.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mFtdSwipeLayout.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
    }

    @Override
    protected void lazyLoad() {

        if (!isVisible || !isPrepared) {
            return;
        }

        DF = SharedPreferencesUtil.getInt(getContext(), Constant.KEY_DATA_FROM, 0);
        if (DF == 0) {
            isUser = false;
            ((TabDataPresenter) mPresenter).getList();
        } else if (DF == 1) {
            isUser = true;
            dataType = 10;
            ((TabDataPresenter) mPresenter).getUserList();
        }
        //loadData(DF);
        isPrepared = false;
        System.out.println("====lazyLoad====");
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mFtdRecyclerView;
    }

    @Override
    public SwipeRefreshLayout getSwipeLayout() {
        return mFtdSwipeLayout;
    }

    @Override
    public LinearLayoutManager getManager() {
        return manager;
    }

    @Override
    public int getDataType() {
        return dataType;
    }

    @Override
    public LinearLayout getLlLoad() {
        return mHfLl;
    }

    @Override
    public View getIncludeNoData() {
        return include_no_data;
    }

    @Override
    public View getIncludeLoadFail() {
        return include_load_fail;
    }

    @Override
    public boolean getIsUser() {
        DF = SharedPreferencesUtil.getInt(getContext(), Constant.KEY_DATA_FROM, 0);
        if (DF == 0) {
            isUser = false;
        } else if (DF == 1) {
            isUser = true;
            dataType = 10;
        }
        return isUser;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("====onActivityCreated====");
        //初始化UI完成
        isPrepared = true;
        lazyLoad();
    }
}
