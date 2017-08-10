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
import com.hb.rssai.adapter.RecommendAdapter;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.util.DisplayUtil;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.view.widget.FullyGridLayoutManager;
import com.hb.rssai.view.widget.GridSpacingItemDecoration;
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
    @BindView(R.id.ff_hot_recycler_view)
    RecyclerView mFfHotRecyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager mLinearManager;
    private LinearLayoutManager mLinearManager1;
    private FindMoreAdapter mAdapter;
    private FullyGridLayoutManager mFullyGridLayoutManager;
    RecommendAdapter recommandAdapter;

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
        mFullyGridLayoutManager = new FullyGridLayoutManager(getContext(), 3);

        mLinearManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLinearManager1.setOrientation(LinearLayoutManager.VERTICAL);
        mFfTopicRecyclerView.addItemDecoration(new MyDecoration(getContext(), LinearLayoutManager.VERTICAL));

        mFfFindRecyclerView.setLayoutManager(mLinearManager);
        mFfFindRecyclerView.setNestedScrollingEnabled(false);//解决卡顿
        mFfFindRecyclerView.setHasFixedSize(true);

        mFfTopicRecyclerView.setLayoutManager(mLinearManager1);
        mFfTopicRecyclerView.setNestedScrollingEnabled(false);//解决卡顿
        mFfTopicRecyclerView.setHasFixedSize(true);

        mFfHotRecyclerView.setLayoutManager(mFullyGridLayoutManager);
        mFfHotRecyclerView.setNestedScrollingEnabled(false);//解决卡顿
        mFfHotRecyclerView.setHasFixedSize(true);

        mFfFindRecyclerView.addItemDecoration(new MyDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mFfHotRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, DisplayUtil.dip2px(getContext(), 16), false));

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
        resFindMore.setTitle("新发展理念结“硕果”  砥砺奋进  治国理政");
        resFindMore.setAddFlag(false);
        resFindMore.setContent("测试内容1");
        resFindMore.setPeople(20000);
        resFindMore.setImg("https://imgsa.baidu.com/news/q%3D100/sign=7254f5363cd12f2ec805aa607fc3d5ff/a71ea8d3fd1f41349eb2919f2f1f95cad0c85e4d.jpg");

        resFindMore.setAbstractContent("辽阔草原，骏马驰骋彩云飘；亮丽北疆，和泰吉祥谱新篇。我国首个省级少数民族自治区——内蒙古自治区迎来成立７０周年大庆。７０载砥砺奋进，民族区域自治制度在草原落地生根，结出丰硕果实，全区各族人民携手前行，共同谱写了团结奋进、发展繁荣的壮丽诗篇。");
        resFindMores.add(resFindMore);

        ResFindMore resFindMore1 = new ResFindMore();
        resFindMore1.setTitle("守望相助共奋进 祖国北疆更亮丽");
        resFindMore1.setAddFlag(false);
        resFindMore1.setContent("测试内容2");
        resFindMore1.setPeople(80000);
        resFindMore1.setImg("https://imgsa.baidu.com/news/q%3D100/sign=c57e751b71cb0a4683228f395b62f63e/30adcbef76094b3665228c0ca9cc7cd98c109d9b.jpg");

        resFindMore1.setAbstractContent("念兹在兹，心系北疆。中南海的阳光雨露，始终润泽着大草原。党的十八大以来，以习近平同志为核心的党中央高瞻远瞩，立足全局谋划内蒙古经济社会发展，为祖国北疆制定清晰的发展蓝图。２０１４年春节前夕，习近平总书记冒着严寒来到内蒙古，行程数千公里，看望慰问各族干部群众，对内蒙古长远发展提出战略指引、作出重大部署。从提出“把内蒙古建成祖国北疆安全稳定的屏障”“建设美丽草原”，到希望“内蒙古各族干部群众守望相助”，习近平总书记为内蒙古自治区改革发展指明前进方向，为各族人民团结奋斗注入强大精神动力。");
        resFindMores.add(resFindMore1);

        ResFindMore resFindMore2 = new ResFindMore();
        resFindMore2.setTitle("锤子手机还值得投资吗？");
        resFindMore2.setAddFlag(false);
        resFindMore2.setContent("测试内容3");
        resFindMore2.setPeople(60000);
        resFindMore2.setImg("https://imgsa.baidu.com/news/q%3D100/sign=229959c179f082022b92953f7bf9fb8a/4d086e061d950a7b8ddaf32600d162d9f3d3c961.jpg");
        resFindMore2.setAbstractContent("日前，锥子科技CEO罗永浩在参加“2017极客公园奇点创新者峰会”透露，锥子科技融到了10亿元左右的资金，接下来锤子科技会和全球正规的手机厂商一样，每年会推出5～6款产品，覆盖高中低三个档位，明年春天或者最晚明天夏天，锤子科技会推出一款有他自己带队，系统中深度整合了准革命性人工智能的手机产品。其实我们好奇的是，就锤子科技成立5年来的表现、手机产业格局的变化及锤子科技未来的计划（主要是根据罗永浩对外的言谈），为何还会有投资人为锤子科技投资呢？");
        resFindMores.add(resFindMore2);

        if (mAdapter == null) {
            mAdapter = new FindMoreAdapter(getContext(), resFindMores);

            List<RssSource> dbList = LiteOrmDBUtil.getQueryAllLengthSort(RssSource.class, 0, 3, "sort");
            if (dbList != null && dbList.size() > 0) {
                recommandAdapter = new RecommendAdapter(getContext(), dbList);
                mFfHotRecyclerView.setAdapter(recommandAdapter);
            }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
