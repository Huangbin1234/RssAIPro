package com.hb.rssai.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseFragment;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.FindPresenter;
import com.hb.rssai.util.DisplayUtil;
import com.hb.rssai.view.iView.IFindView;
import com.hb.rssai.view.subscription.tab.TabResourceActivity;
import com.hb.rssai.view.widget.FullyGridLayoutManager;
import com.hb.rssai.view.widget.GridSpacingItemDecoration;
import com.hb.rssai.view.widget.MyDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FindFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindFragment extends BaseFragment implements IFindView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
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
//    @BindView(R.id.ff_find_tv_topic)
//    TextView mFfFindTvTopic;
    @BindView(R.id.ff_tv_right_all)
    TextView mFfTvRightAll;
//    @BindView(R.id.ff_topic_iv_all)
//    ImageView mFfTopicIvAll;
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
    //    @BindView(R.id.ff_topic_recycler_view)
//    RecyclerView mFfTopicRecyclerView;
    @BindView(R.id.ff_hot_recycler_view)
    RecyclerView mFfHotRecyclerView;
    @BindView(R.id.ff_nest_scrollview)
    NestedScrollView mFfNestScrollview;
    Unbinder unbinder;
    @BindView(R.id.ll_recommend)
    LinearLayout mLlRecommend;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager mFindMoreLinearManager;
    private LinearLayoutManager mTopicLinearManager1;

    private FullyGridLayoutManager mRecommendGridLayoutManager;


    public FindFragment() {
        // Required empty public constructor
    }

    public static FindFragment newInstance(String param1, String param2) {
        FindFragment fragment = new FindFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

    @Override
    protected void initView(View rootView) {
        mFindMoreLinearManager = new LinearLayoutManager(getContext());
        mTopicLinearManager1 = new LinearLayoutManager(getContext());
        mRecommendGridLayoutManager = new FullyGridLayoutManager(getContext(), 3);

        mFindMoreLinearManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mTopicLinearManager1.setOrientation(LinearLayoutManager.VERTICAL);
//        mFfTopicRecyclerView.addItemDecoration(new MyDecoration(getContext(), LinearLayoutManager.VERTICAL));

        mFfFindRecyclerView.setLayoutManager(mFindMoreLinearManager);
        mFfFindRecyclerView.setNestedScrollingEnabled(false);//解决卡顿
        mFfFindRecyclerView.setHasFixedSize(true);

//        mFfTopicRecyclerView.setLayoutManager(mTopicLinearManager1);
//        mFfTopicRecyclerView.setNestedScrollingEnabled(false);
//        mFfTopicRecyclerView.setHasFixedSize(true);

        mFfHotRecyclerView.setLayoutManager(mRecommendGridLayoutManager);
        mFfHotRecyclerView.setNestedScrollingEnabled(false);
        mFfHotRecyclerView.setHasFixedSize(true);

        mFfFindRecyclerView.addItemDecoration(new MyDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mFfHotRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, DisplayUtil.dip2px(getContext(), 16), false));

        mFfSwipeLayout.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mFfSwipeLayout.setProgressViewOffset(true, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        mSubLlAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TabResourceActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((FindPresenter) mPresenter).findMoreList();
        ((FindPresenter) mPresenter).recommendList();
    }

    // TODO: Rename method, update argument and hook method into UI event
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
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public RecyclerView getFfFindRecyclerView() {
        return mFfFindRecyclerView;
    }

    @Override
    public RecyclerView getFfTopicRecyclerView() {
//        return mFfTopicRecyclerView;
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
}
