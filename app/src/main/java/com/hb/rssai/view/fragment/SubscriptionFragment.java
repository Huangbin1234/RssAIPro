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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hb.rssai.R;
import com.hb.rssai.adapter.RssSourceAdapter;
import com.hb.rssai.bean.RssChannel;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.event.RssSourceEvent;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.view.subscription.AddSourceActivity;
import com.rss.bean.Website;
import com.rss.util.FeedReader;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager mLayoutManager;
    private RssSourceAdapter mRssSourceAdapter;
    private List<RssSource> list = new ArrayList<>();

    MyAdapter myAdapter;


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
        mLayoutManager = new LinearLayoutManager(getContext());
        mSfRecyclerView.setLayoutManager(mLayoutManager);
        mSfSwipe.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mSfSwipe.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        //TODO 设置下拉刷新
        mSfSwipe.setOnRefreshListener(() -> initData());

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


    @OnClick({R.id.sys_iv_add})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sys_iv_add:
                startActivity(new Intent(getContext(), AddSourceActivity.class));
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
//            if (mRssSourceAdapter == null) {
//                mRssSourceAdapter = new RssSourceAdapter(getContext(), list);
//                mSfRecyclerView.setAdapter(mRssSourceAdapter);
//            } else {
//                mRssSourceAdapter.notifyDataSetChanged();
//            }
            if (myAdapter == null) {
                myAdapter = new MyAdapter(getContext(), list);
                mSfRecyclerView.setAdapter(myAdapter);
            } else {
                myAdapter.notifyDataSetChanged();
            }
            cardSettting();
        }
    }

    private void cardSettting() {
        CardItemTouchHelperCallback cardCallback = new CardItemTouchHelperCallback(mSfRecyclerView.getAdapter(), list);
        ItemTouchHelper touchHelper = new ItemTouchHelper(cardCallback);
        CardLayoutManager cardLayoutManager = new CardLayoutManager(mSfRecyclerView, touchHelper);
        mSfRecyclerView.setLayoutManager(cardLayoutManager);
        touchHelper.attachToRecyclerView(mSfRecyclerView);
        cardCallback.setOnSwipedListener(new OnSwipeListener<RssSource>() {

            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
                MyAdapter.MyViewHolder myHolder = (MyAdapter.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1 - Math.abs(ratio) * 0.2f);

            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, RssSource rssSource, int direction) {
                MyAdapter.MyViewHolder myHolder = (MyAdapter.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1f);
                Glide.with(getContext())
                        .load(rssSource.getImgUrl())
                        .crossFade(1000)
                        .bitmapTransform(new BlurTransformation(getContext(), 23, 4)) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                        .into(mSfIvBg);
            }

            @Override
            public void onSwipedClear() {
                Toast.makeText(getContext(), "data clear", Toast.LENGTH_SHORT).show();
                mSfRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        mSfRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                }, 3000L);
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

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        Context mContext;
        List<RssSource> list;

        public MyAdapter(Context mContext, List<RssSource> list) {
            this.mContext = mContext;
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Glide.with(mContext)
                    .load(list.get(position).getImgUrl())
                    .crossFade(1000)
                    .into(holder.avatarImageView);
            holder.tv_name.setText(list.get(position).getName());
            holder.tv_des.setText(list.get(position).getLink());
            holder.tv_count.setText("" + list.get(position).getCount());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView avatarImageView;
            TextView tv_count, tv_des, tv_name;

            MyViewHolder(View itemView) {
                super(itemView);
                avatarImageView = (ImageView) itemView.findViewById(R.id.iv_avatar);
                tv_count = (TextView) itemView.findViewById(R.id.tv_count);
                tv_des = (TextView) itemView.findViewById(R.id.tv_des);
                tv_name = (TextView) itemView.findViewById(R.id.tv_name);

            }
        }
    }

    /**
     * 可以选择插入到数据库
     *
     * @param website
     */
    String[] urls = {"http://icon.nipic.com/BannerPic/20170531/home/20170531103230.jpg", "http://icon.nipic.com/BannerPic/20170509/home/20170509164717.jpg", "http://icon.nipic.com/BannerPic/20170619/home/20170619151644.jpg", "http://icon.nipic.com/BannerPic/20170531/home/20170531103230.jpg", "http://icon.nipic.com/BannerPic/20170509/home/20170509164717.jpg", "http://icon.nipic.com/BannerPic/20170619/home/20170619151644.jpg", "http://icon.nipic.com/BannerPic/20170531/home/20170531103230.jpg", "http://icon.nipic.com/BannerPic/20170509/home/20170509164717.jpg", "http://icon.nipic.com/BannerPic/20170619/home/20170619151644.jpg"};

    public void rssInsert(Website website) {
        try {
//            List<RSSItemBean> rssTempList = new FeedReader().getContent(website);
            RssChannel rssTempList = new FeedReader().getContent(website);
            if (rssTempList != null && rssTempList.getRSSItemBeen().size() > 0) {
                int len = list.size();
                for (int i = 0; i < len; i++) {
                    if (website.getFid().equals("" + list.get(i).getId())) {
                        list.get(i).setCount(rssTempList.getRSSItemBeen().size());
                        if (rssTempList.getImage() != null && rssTempList.getImage().getUrl() != null) {
//                            list.get(i).setImgUrl(rssTempList.getImage().getUrl());
                            list.get(i).setImgUrl(urls[i]);
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
}
