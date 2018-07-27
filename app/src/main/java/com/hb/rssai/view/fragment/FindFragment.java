package com.hb.rssai.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseFragment;
import com.hb.rssai.event.FindMoreEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.FindPresenter;
import com.hb.rssai.util.DisplayUtil;
import com.hb.rssai.view.iView.IFindView;
import com.hb.rssai.view.me.SearchActivity;
import com.hb.rssai.view.subscription.tab.TabResourceActivity;
import com.hb.rssai.view.widget.GridSpacingItemDecoration;
import com.hb.rssai.view.widget.MyDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FindFragment extends BaseFragment implements IFindView {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.ff_find_hot_label)
    TextView mFfFindHotLabel;
    @BindView(R.id.tv_sub_right_all)
    TextView mTvSubRightAll;

    @BindView(R.id.ff_tv_right_all)
    TextView mFfTvRightAll;

    @BindView(R.id.sub_ll_all)
    LinearLayout mSubLlAll;
    @BindView(R.id.ff_find_tv_more)
    TextView mFfFindTvMore;
    @BindView(R.id.ff_find_recycler_view)
    RecyclerView mFfFindRecyclerView;
    @BindView(R.id.rl_ll)
    LinearLayout mRlLl;
    @BindView(R.id.ff_swipe_layout)
    SwipeRefreshLayout mFfSwipeLayout;

    @BindView(R.id.ff_hot_recycler_view)
    RecyclerView mFfHotRecyclerView;
    @BindView(R.id.ff_nest_scrollview)
    NestedScrollView mFfNestScrollview;

    @BindView(R.id.ll_recommend)
    LinearLayout mLlRecommend;
    @BindView(R.id.ff_ll_root)
    LinearLayout mFfLlRoot;
    Unbinder unbinder;
    @BindView(R.id.sys_iv_search)
    ImageView mSysIvSearch;

    @BindView(R.id.include_no_data)
    LinearLayout include_no_data;
    @BindView(R.id.include_load_fail)
    LinearLayout include_load_fail;
    @BindView(R.id.llf_btn_re_try)
    Button mLlfBtnReTry;

    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager mFindMoreLinearManager;
    private LinearLayoutManager mTopicLinearManager1;

    private boolean isPrepared;

    @Override
    protected void lazyLoad() {
        if (!isVisible || !isPrepared) {
            return;
        }
        ((FindPresenter) mPresenter).recommendList();
        ((FindPresenter) mPresenter).findMoreList();
        isPrepared = false;
        System.out.println("====lazyLoad====");
    }

    public FindFragment() {
        // Required empty public constructor
    }

    public static FindFragment newInstance(String param1, String param2) {
        FindFragment fragment = new FindFragment();
        return fragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (rView != null) {
            if (hidden) {
                mFfLlRoot.setFitsSystemWindows(false);
            } else {
                mFfLlRoot.setFitsSystemWindows(true);
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                rView.requestApplyInsets();
            } else {
                rView.requestFitSystemWindows();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注册
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setAppTitle() {
        mSysToolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mSysToolbar);
        mSysTvTitle.setText(getResources().getString(R.string.str_main_find));
    }


    @Override
    protected BasePresenter createPresenter() {
        return new FindPresenter(getContext(), this);
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.fragment_find;
    }

    View rView;

    @Override
    protected void initView(View rootView) {
        rView = rootView;
        mFindMoreLinearManager = new LinearLayoutManager(getContext());
        mTopicLinearManager1 = new LinearLayoutManager(getContext());
        LinearLayoutManager mLlm = new LinearLayoutManager(getContext());
        mLlm.setOrientation(LinearLayoutManager.HORIZONTAL);
        mFindMoreLinearManager.setOrientation(LinearLayoutManager.VERTICAL);
        mFfFindRecyclerView.setLayoutManager(mFindMoreLinearManager);
        mFfFindRecyclerView.setNestedScrollingEnabled(false);//解决卡顿
        mFfFindRecyclerView.setHasFixedSize(true);
        mFfHotRecyclerView.setLayoutManager(mLlm);
        mFfHotRecyclerView.setNestedScrollingEnabled(false);
        mFfHotRecyclerView.setHasFixedSize(true);

        mFfFindRecyclerView.addItemDecoration(new MyDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mFfHotRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, DisplayUtil.dip2px(getContext(), 0), false));

        mFfSwipeLayout.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mFfSwipeLayout.setProgressViewOffset(true, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        mSubLlAll.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TabResourceActivity.class);
            startActivity(intent);
        });
        mSysIvSearch.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            startActivity(intent);
        });

        mLlfBtnReTry.setOnClickListener(v -> {
            ((FindPresenter) mPresenter).findMoreList();
            ((FindPresenter) mPresenter).recommendList();
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("====onActivityCreated====");
        //初始化UI完成
        isPrepared = true;
        lazyLoad();
    }

    @Subscribe
    public void onEventMainThread(FindMoreEvent event) {
        if (event.getMessage() == 0) {
            ((FindPresenter) mPresenter).refreshList();
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public RecyclerView getFfFindRecyclerView() {
        return mFfFindRecyclerView;
    }

    @Override
    public RecyclerView getFfTopicRecyclerView() {
        return null;
    }

    @Override
    public RecyclerView getFfHotRecyclerView() {
        return mFfHotRecyclerView;
    }

    @Override
    public SwipeRefreshLayout getFfSwipeLayout() {
        return mFfSwipeLayout;
    }

    @Override
    public LinearLayoutManager getFindMoreManager() {
        return mFindMoreLinearManager;
    }

    @Override
    public NestedScrollView getNestScrollView() {
        return mFfNestScrollview;
    }

    @Override
    public LinearLayout getLlRecommend() {
        return mLlRecommend;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public View getIncludeNoData() {
        return include_no_data;
    }

    @Override
    public View getIncludeLoadFail() {
        return include_load_fail;
    }
}
