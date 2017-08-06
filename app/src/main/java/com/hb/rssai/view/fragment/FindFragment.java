package com.hb.rssai.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.FindMoreAdapter;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.view.widget.MyDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FindFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindFragment extends Fragment {
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
    @BindView(R.id.ff_find_tv_topic)
    TextView mFfFindTvTopic;
    @BindView(R.id.ff_tv_right_all)
    TextView mFfTvRightAll;
    @BindView(R.id.ff_topic_iv_all)
    ImageView mFfTopicIvAll;
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
    @BindView(R.id.ff_topic_recycler_view)
    RecyclerView mFfTopicRecyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager mLinearManager;
    private LinearLayoutManager mLinearManager1;
    private FindMoreAdapter mAdapter;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mSysToolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mSysToolbar);
        mSysTvTitle.setText(getResources().getString(R.string.str_main_find));

        mLinearManager = new LinearLayoutManager(getContext());
        mLinearManager1 = new LinearLayoutManager(getContext());
        mLinearManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLinearManager1.setOrientation(LinearLayoutManager.VERTICAL);
        mFfTopicRecyclerView.addItemDecoration(new MyDecoration(getContext(),LinearLayoutManager.VERTICAL));

        mFfTopicRecyclerView.setLayoutManager(mLinearManager1);
        mFfTopicRecyclerView.setNestedScrollingEnabled(false);//解决卡顿
        mFfTopicRecyclerView.setHasFixedSize(true);



        mFfFindRecyclerView.setLayoutManager(mLinearManager);
        mFfFindRecyclerView.setNestedScrollingEnabled(false);//解决卡顿
        mFfFindRecyclerView.setHasFixedSize(true);

        mFfFindRecyclerView.addItemDecoration(new MyDecoration(getContext(),LinearLayoutManager.VERTICAL));

        mFfSwipeLayout.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mFfSwipeLayout.setProgressViewOffset(true, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    private void initData() {
        List<ResFindMore> resFindMores = new ArrayList<>();
        ResFindMore resFindMore = new ResFindMore();
        resFindMore.setTitle("全展开的列表式");
        resFindMore.setAddFlag(false);
        resFindMore.setContent("测试内容1");
        resFindMore.setPeople(20000);
        resFindMore.setImg("http://p1.ifengimg.com/a/2017_31/701333c5881b75e_size93_w440_h330.jpg");

        resFindMore.setAbstractContent("以上就是比较通用的使用场景及所做的兼容 ，最后附上Github链接RecyclerItemDecoration，欢迎star，fork。");
        resFindMores.add(resFindMore);

        ResFindMore resFindMore1 = new ResFindMore();
        resFindMore1.setTitle("全展开的列表式");
        resFindMore1.setAddFlag(false);
        resFindMore1.setContent("测试内容2");
        resFindMore1.setPeople(80000);
        resFindMore1.setImg("http://p2.ifengimg.com/a/2017_31/e5725ce2f7926fc_size145_w690_h518.jpg");

        resFindMore1.setAbstractContent("几乎所有的app都会遇到列表的展开与收起功能，最近公司的产品也用了，相信大家都知道已经逐步替代了listview成为主流，原因不仅仅是在于它的缓存机制，还在于它对于条目的数据更新设置了更多动画。接下来我们就来实现一个万能的条目展开与收起。");
        resFindMores.add(resFindMore1);

        ResFindMore resFindMore2 = new ResFindMore();
        resFindMore2.setTitle("全展开的列表式");
        resFindMore2.setAddFlag(false);
        resFindMore2.setContent("测试内容3");
        resFindMore2.setPeople(60000);
        resFindMore2.setImg("http://p2.ifengimg.com/a/2017_31/2757a57c4bbf7a6_size152_w690_h414.jpg");
        resFindMore2.setAbstractContent("其实上面的代码就是根据滑动方向（横向或者纵向）以及child的位置（是不是最后一行或者最后一列），对附属区域进行限制，同样，如果不是特殊的分割线样式，通过背景就基本可以实现需求，不用特殊draw。");
        resFindMores.add(resFindMore2);

        if (mAdapter == null) {
            mAdapter = new FindMoreAdapter(getContext(), resFindMores);
            mFfFindRecyclerView.setAdapter(mAdapter);
            mFfTopicRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
