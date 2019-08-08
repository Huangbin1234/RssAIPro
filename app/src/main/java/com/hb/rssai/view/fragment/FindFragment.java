package com.hb.rssai.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.FindMoreAdapter;
import com.hb.rssai.adapter.RecommendAdapter;
import com.hb.rssai.app.ProjectApplication;
import com.hb.rssai.base.BaseFragment;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.FindMoreEvent;
import com.hb.rssai.event.RssSourceEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.FindPresenter;
import com.hb.rssai.util.DisplayUtil;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.util.ThemeUtils;
import com.hb.rssai.view.common.LoginActivity;
import com.hb.rssai.view.iView.IFindView;
import com.hb.rssai.view.me.SearchActivity;
import com.hb.rssai.view.subscription.AddSourceActivity;
import com.hb.rssai.view.subscription.OfflineListActivity;
import com.hb.rssai.view.subscription.SourceCardActivity;
import com.hb.rssai.view.subscription.tab.TabResourceActivity;
import com.hb.rssai.view.widget.GridSpacingItemDecoration;
import com.hb.rssai.view.widget.MyDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class FindFragment extends BaseFragment implements IFindView, View.OnClickListener {

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
    @BindView(R.id.sys_iv_search)
    ImageView mSysIvSearch;
    @BindView(R.id.include_no_data)
    LinearLayout include_no_data;
    @BindView(R.id.include_load_fail)
    LinearLayout include_load_fail;
    @BindView(R.id.llf_btn_re_try)
    Button mLlfBtnReTry;
    @BindView(R.id.sys_iv_add)
    ImageView mSysIvAdd;
//    @BindView(R.id.fmi_tv_count)
//    TextView mFmiTvCount;
    Unbinder unbinder;

    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager mFindMoreLinearManager;
    private LinearLayoutManager mTopicLinearManager1;

    private boolean isPrepared;
    private View rView;

    private RecommendAdapter recommendAdapter;
    private FindMoreAdapter findMoreAdapter;

    private int page = 1;
    private boolean isEnd = false, isLoad = false;
    private List<ResFindMore.RetObjBean.RowsBean> resFindMores = new ArrayList<>();
    private List<ResFindMore.RetObjBean.RowsBean> resRecommends = new ArrayList<>();
    private int recommendPage = 1;
    private boolean isRecommendEnd = false, isRecommendLoad = false;
    private ResFindMore.RetObjBean.RowsBean rowsBean;

    @Override
    protected void lazyLoad() {
        if (!isVisible || !isPrepared) {
            return;
        }
        //解决状态栏闪烁问题
        int theme = SharedPreferencesUtil.getInt(getContext(), Constant.KEY_THEME, 0);
        if (theme != 0) {
            mFfLlRoot.setBackgroundColor(ThemeUtils.getPrimaryDarkColor(getActivity()));
        } else {
            mFfLlRoot.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        }
        mFfSwipeLayout.setRefreshing(true);
        ((FindPresenter) mPresenter).recommendList();
        ((FindPresenter) mPresenter).findMoreList();
        isPrepared = false;
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
    protected void setAppTitle() {
        mSysToolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mSysToolbar);
        mSysTvTitle.setText(getResources().getString(R.string.str_main_find));
    }

    @Override
    protected BasePresenter createPresenter() {
        return new FindPresenter(this);
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.fragment_find;
    }

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

        mFfSwipeLayout.setOnRefreshListener(() -> onRefresh());
        mFfNestScrollview.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (v.getChildAt(0) != null && isBottomForNestedScrollView(v, scrollY)) {
                // 加载更多
                loadMore();
            }
        });
    }

    // TODO: 判断是不是在底部
    private boolean isBottomForNestedScrollView(NestedScrollView v, int scrollY) {
        return (scrollY + v.getHeight()) == (v.getChildAt(0).getHeight() + v.getPaddingTop() + v.getPaddingBottom());
    }

    @Override
    protected void onRefresh() {
        page = 1;
        recommendPage = 1;
        isLoad = true;
        isRecommendLoad = true;

        isEnd = false;
        isRecommendEnd = false;

        if (resFindMores != null) {
            resFindMores.clear();
        }
        if (resRecommends != null) {
            resRecommends.clear();
        }
        mFfSwipeLayout.setRefreshing(true);

        if (findMoreAdapter != null) {
            findMoreAdapter.init();
        }
        if (recommendAdapter != null) {
            recommendAdapter.init();
        }

        ((FindPresenter) mPresenter).recommendList();
        ((FindPresenter) mPresenter).findMoreList();
    }

    @Override
    protected void loadMore() {
        if (!isEnd && !isLoad) {
            mFfSwipeLayout.setRefreshing(true);
            page++;
            ((FindPresenter) mPresenter).findMoreList();
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化UI完成
        isPrepared = true;
        lazyLoad();
    }

    @Subscribe
    public void onEventMainThread(FindMoreEvent event) {
        if (event.getMessage() == 0) {
            onRefresh();
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
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @OnClick({R.id.sys_iv_add, R.id.sub_ll_all, R.id.sys_iv_search, R.id.llf_btn_re_try, R.id.ll_recommend})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sys_iv_add:
                startActivity(new Intent(getContext(), AddSourceActivity.class));
                break;
            case R.id.sub_ll_all:
                startActivity(new Intent(getContext(), TabResourceActivity.class));
                break;
            case R.id.sys_iv_search:
                startActivity(new Intent(getContext(), SearchActivity.class));
                break;
            case R.id.llf_btn_re_try:
                ((FindPresenter) mPresenter).findMoreList();
                ((FindPresenter) mPresenter).recommendList();
                break;
            case R.id.ll_recommend:
                if (!isRecommendEnd && !isRecommendLoad) {
                    recommendPage++;
                    ((FindPresenter) mPresenter).recommendList();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setFindMoreResult(ResFindMore resFindMore) {
        isLoad = false;
        mFfSwipeLayout.setRefreshing(false);
        //TODO 填充数据
        if (resFindMore.getRetCode() == 0) {
            mFfFindRecyclerView.setVisibility(View.VISIBLE);
            include_load_fail.setVisibility(View.GONE);
            include_no_data.setVisibility(View.GONE);

            if (resFindMore.getRetObj().getRows() != null && resFindMore.getRetObj().getRows().size() > 0) {
//                mFmiTvCount.setText(""+resFindMore.getRetObj().getTotal());
                resFindMores.addAll(resFindMore.getRetObj().getRows());
                if (findMoreAdapter == null) {
                    findMoreAdapter = new FindMoreAdapter(getContext(), resFindMores);
                    findMoreAdapter.setOnItemClickedListener((itemView, rowsBean1) -> {
                        boolean isOffline = SharedPreferencesUtil.getBoolean(getContext(), Constant.KEY_IS_OFFLINE_MODE, false);
                        if (isOffline) {
                            Intent intent = new Intent(getContext(), OfflineListActivity.class);
                            intent.putExtra(OfflineListActivity.KEY_LINK, rowsBean1.getLink());
                            intent.putExtra(OfflineListActivity.KEY_NAME, rowsBean1.getName());
                            intent.putExtra(OfflineListActivity.KEY_SUBSCRIBE_ID, rowsBean1.getId());
                            intent.putExtra(OfflineListActivity.KEY_IS_TAG, rowsBean1.isIsTag());
                            intent.putExtra(OfflineListActivity.KEY_IMG, rowsBean1.getImg());
                            getContext().startActivity(intent);
                        } else {
                            Intent intent = new Intent(getContext(), SourceCardActivity.class);
                            intent.putExtra(SourceCardActivity.KEY_LINK, rowsBean1.getLink());
                            intent.putExtra(SourceCardActivity.KEY_TITLE, rowsBean1.getName());
                            intent.putExtra(SourceCardActivity.KEY_SUBSCRIBE_ID, rowsBean1.getId());
                            intent.putExtra(SourceCardActivity.KEY_IMAGE, rowsBean1.getImg());
                            intent.putExtra(SourceCardActivity.KEY_DESC, rowsBean1.getAbstractContent());
                            intent.putExtra(SourceCardActivity.KEY_IS_CHECK, rowsBean1.isCheck());


                            Pair<View, String> pImg = Pair.create(itemView.findViewById(R.id.ifm_iv_img), "img");
                            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pImg);
                            startActivity(intent, compat.toBundle());

//                            getContext().startActivity(intent);
                        }
                    });
                    findMoreAdapter.setOnAddClickedListener((bean, v) -> {
                        rowsBean = bean;

                        if (!TextUtils.isEmpty(SharedPreferencesUtil.getString(getContext(), Constant.TOKEN, ""))) {
                            //TODO 先去查询服务器上此条数据
                            ((FindPresenter) mPresenter).findMoreListById(v, false);
                        } else {
                            //跳转到登录
                            T.ShowToast(getContext(), Constant.MSG_NO_LOGIN);
                            Intent intent = new Intent(ProjectApplication.mContext, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ProjectApplication.mContext.startActivity(intent);
                        }
                    });
                    mFfFindRecyclerView.setAdapter(findMoreAdapter);
                } else {
                    findMoreAdapter.notifyDataSetChanged();
                }
            }
            if (resFindMores.size() == resFindMore.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else if (resFindMore.getRetCode() == 10013) {//暂无数据
            include_no_data.setVisibility(View.VISIBLE);
            include_load_fail.setVisibility(View.GONE);
            mFfFindRecyclerView.setVisibility(View.GONE);
        } else {
            include_no_data.setVisibility(View.GONE);
            include_load_fail.setVisibility(View.VISIBLE);
            mFfFindRecyclerView.setVisibility(View.GONE);
            T.ShowToast(getContext(), resFindMore.getRetMsg());
        }
    }

    @Override
    public void setRecommendResult(ResFindMore resFindMore) {
        isRecommendLoad = false;
        //TODO 填充数据
        if (resFindMore.getRetCode() == 0) {
            if (resFindMore.getRetObj().getRows() != null && resFindMore.getRetObj().getRows().size() > 0) {
                if (resRecommends.size() > 0) {
                    resRecommends.clear();
                    recommendAdapter.notifyDataSetChanged();
                }
                resRecommends.addAll(resFindMore.getRetObj().getRows());
                if (recommendAdapter == null) {
                    recommendAdapter = new RecommendAdapter(getContext(), resRecommends);
                    recommendAdapter.setOnAddClickedListener((bean, v) -> {
                        rowsBean = bean;
                        if (!TextUtils.isEmpty(SharedPreferencesUtil.getString(getContext(), Constant.TOKEN, ""))) {
                            //TODO 先去查询服务器上此条数据
                            ((FindPresenter) mPresenter).findMoreListById(v, true);
                        } else {
                            //跳转到登录
                            T.ShowToast(getContext(), Constant.MSG_NO_LOGIN);
                            Intent intent = new Intent(ProjectApplication.mContext, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ProjectApplication.mContext.startActivity(intent);
                        }
                    });
                    mFfHotRecyclerView.setAdapter(recommendAdapter);
                } else {
                    recommendAdapter.notifyDataSetChanged();
                }
                if (recommendPage * Constant.RECOMMEND_PAGE_SIZE + resRecommends.size() >= resFindMore.getRetObj().getTotal()) {
                    isRecommendEnd = true;
                }
            }
        } else {
            T.ShowToast(getContext(), resFindMore.getRetMsg());
        }
    }

    @Override
    public void setDelResult(ResBase resBase, View v, boolean isRecommend) {
        T.ShowToast(getContext(), resBase.getRetMsg());
        if (resBase.getRetCode() == 0) {
            EventBus.getDefault().post(new RssSourceEvent(0));
        }
        if (isRecommend) {
            if (resBase.getRetCode() == 0) {
                ((ImageView) v).setImageResource(R.mipmap.ic_recommend_add);
            } else {
                ((ImageView) v).setImageResource(R.color.trans);
            }
        } else {
            if (resBase.getRetCode() == 0) {
                ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_add);
            } else {
                ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_cancel);
            }
        }
    }

    @Override
    public String getRowsBeanId() {
        return rowsBean.getId();
    }

    @Override
    public String getPage() {
        return "" + page;
    }

    @Override
    public String getRecommendPage() {
        return "" + recommendPage;
    }

    @Override
    public void showFindError() {
        if (!(null != resFindMores && resFindMores.size() > 0)) {
            include_load_fail.setVisibility(View.VISIBLE);
            include_no_data.setVisibility(View.GONE);
        }
        mFfSwipeLayout.setRefreshing(false);
        T.ShowToast(getContext(), Constant.MSG_NETWORK_ERROR);
    }

    @Override
    public void showLoadError() {
        mFfSwipeLayout.setRefreshing(false);
        T.ShowToast(getContext(), Constant.MSG_NETWORK_ERROR);
    }

    @Override
    public String getUserID() {
        return SharedPreferencesUtil.getString(getContext(), Constant.USER_ID, "");
    }

    @Override
    public void showToast(String retMsg) {
        T.ShowToast(getContext(), retMsg);
    }

    @Override
    public void setAddResult(ResBase resBase, View v, boolean isRecommend) {
        T.ShowToast(getContext(), resBase.getRetMsg());
        if (resBase.getRetCode() == 0) {
            EventBus.getDefault().post(new RssSourceEvent(0));
        }
        if (isRecommend) {
            if (resBase.getRetCode() == 0) {
                ((ImageView) v).setImageResource(R.color.trans);
            } else {
                ((ImageView) v).setImageResource(R.mipmap.ic_recommend_add);
            }
        } else {
            if (resBase.getRetCode() == 0) {
                ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_cancel);
            } else {
                ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_add);
            }
        }
    }
}
