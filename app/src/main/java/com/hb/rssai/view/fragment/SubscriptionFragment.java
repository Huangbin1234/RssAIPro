package com.hb.rssai.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hb.rssai.R;
import com.hb.rssai.adapter.CardAdapter;
import com.hb.rssai.adapter.RssSourceAdapter;
import com.hb.rssai.bean.RssChannel;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.event.RssSourceEvent;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.view.subscription.HotTagActivity;
import com.rss.bean.Website;
import com.rss.util.FeedReader;
import com.zbar.lib.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.BlurTransformation;
import me.yuqirong.cardswipelayout.CardItemTouchHelperCallback;
import me.yuqirong.cardswipelayout.CardLayoutManager;
import me.yuqirong.cardswipelayout.OnSwipeListener;

import static android.app.Activity.RESULT_OK;

public class SubscriptionFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    Unbinder unbinder;
    @BindView(R.id.sf_recycler_view)
    RecyclerView mSfRecyclerView;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.sys_iv_add)
    ImageView mSysIvAdd;
    @BindView(R.id.sf_swipe)
    SwipeRefreshLayout mSfSwipe;
    @BindView(R.id.sf_iv_bg)
    ImageView mSfIvBg;
    @BindView(R.id.sys_iv_scan)
    ImageView mSysIvScan;
//    @BindView(R.id.index_function_gridview)
//    FullGridView mIndexFunctionGridView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private GridLayoutManager mGridLayoutManager;
    private RssSourceAdapter mRssSourceAdapter;
    private List<RssSource> list = new ArrayList<>();

    CardAdapter mCardAdapter;
    public final static int REQUESTCODE = 1;

    public SubscriptionFragment() {
    }

    public static SubscriptionFragment newInstance(String param1, String param2) {
        SubscriptionFragment fragment = new SubscriptionFragment();
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
        // 注册
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscription, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }


    private void initView() {
        mSysToolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mSysToolbar);
        mSysTvTitle.setText(getResources().getString(R.string.str_main_subscription));

        // 卡片式
        mGridLayoutManager = new GridLayoutManager(getContext(), 2);
        mSfRecyclerView.setLayoutManager(mGridLayoutManager);
//        mSfRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext()));

        mSfSwipe.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mSfSwipe.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        //TODO 设置下拉刷新
        mSfSwipe.setOnRefreshListener(() -> initData());


//        mIndexFunctionGridView.setOnItemClickListener((parent, view, position, id) -> {
//            Intent intent = new Intent(getContext(), SourceListActivity.class);
//            intent.putExtra(SourceListActivity.KEY_LINK, list.get(position).getLink());
//            intent.putExtra(SourceListActivity.KEY_TITLE, list.get(position).getName());
//            getContext().startActivity(intent);
//        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();


    }

    private void initData() {
        List<RssSource> dbList = LiteOrmDBUtil.getQueryAll(RssSource.class);
        if (dbList == null || dbList.size() <= 0) {
            return;
        }
        if (list != null && list.size() > 0) {
            list.clear();
        }
        list.addAll(dbList);
        new ReadRssTask().execute();
    }

    @Subscribe
    public void onEventMainThread(RssSourceEvent event) {
        if (event.getMessage() == 0) {
            initData();
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.sys_iv_add, R.id.sys_iv_scan})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sys_iv_add:
                startActivity(new Intent(getContext(), HotTagActivity.class));
                break;
            case R.id.sys_iv_scan:
//                startActivity(new Intent(getContext(), AddSourceActivity.class));
                startActivityForResult(new Intent(getContext(), CaptureActivity.class), REQUESTCODE);
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        // 取消注册
        EventBus.getDefault().unregister(this);
        mListener = null;
    }

    class ReadRssTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            readRssXml();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mSfSwipe.setRefreshing(false);
            if (list.size() < 9) {
                if (mCardAdapter == null) {
                    mCardAdapter = new CardAdapter(getContext(), list);
                    mSfRecyclerView.setAdapter(mCardAdapter);
                } else {
                    mCardAdapter.notifyDataSetChanged();
                }
                cardSetting();
            } else {
                if (mRssSourceAdapter == null) {
                    RssSource rssSource = new RssSource();
                    rssSource.setName("添加更多");
                    rssSource.setId(0);
                    list.add(rssSource);
                    mRssSourceAdapter = new RssSourceAdapter(getContext(), list);
                    mSfRecyclerView.setAdapter(mRssSourceAdapter);
                } else {
                    mRssSourceAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void cardSetting() {
        CardItemTouchHelperCallback cardCallback = new CardItemTouchHelperCallback(mSfRecyclerView.getAdapter(), list);
        ItemTouchHelper touchHelper = new ItemTouchHelper(cardCallback);
        CardLayoutManager cardLayoutManager = new CardLayoutManager(mSfRecyclerView, touchHelper);
        mSfRecyclerView.setLayoutManager(cardLayoutManager);
        touchHelper.attachToRecyclerView(mSfRecyclerView);
        cardCallback.setOnSwipedListener(new OnSwipeListener<RssSource>() {

            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
                CardAdapter.MyViewHolder myHolder = (CardAdapter.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1 - Math.abs(ratio) * 0.2f);

            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, RssSource rssSource, int direction) {
                CardAdapter.MyViewHolder myHolder = (CardAdapter.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1f);
                Glide.with(getContext())
                        .load(rssSource.getImgUrl())
                        .crossFade(1000)
                        .bitmapTransform(new BlurTransformation(getContext(), 23, 4)) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                        .into(mSfIvBg);
            }

            @Override
            public void onSwipedClear() {
                mSfRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        mSfRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                }, 1500L);
            }
        });

    }

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
//            List<RSSItemBean> rssTempList = new FeedReader().getContent(website);
            RssChannel rssTempList = new FeedReader().getContent(website);
            if (rssTempList != null && rssTempList.getRSSItemBeen().size() > 0) {
                int len = list.size();
                for (int i = 0; i < len; i++) {
                    if (website.getFid().equals("" + list.get(i).getId())) {
                        list.get(i).setCount(rssTempList.getRSSItemBeen().size());
                        list.get(i).setImgUrl(urls[i]);
                        if (rssTempList.getImage() != null && rssTempList.getImage().getUrl() != null) {
//                            list.get(i).setImgUrl(rssTempList.getImage().getUrl());
                            if (rssTempList.getImage().getTitle() != null) {
                                list.get(i).setName(rssTempList.getImage().getTitle());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK == resultCode) {
            if (requestCode == REQUESTCODE) {
                RssSource rssSource = new RssSource();
                rssSource.setName("订阅");
                rssSource.setLink(data.getStringExtra("info"));
                LiteOrmDBUtil.insert(rssSource);
                initData();
            }
        }
    }
}
