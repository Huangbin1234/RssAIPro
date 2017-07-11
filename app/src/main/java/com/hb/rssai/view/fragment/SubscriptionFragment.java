package com.hb.rssai.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.RssSourceAdapter;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.view.subscription.AddSourceActivity;
import com.hb.rssai.view.subscription.RssSourceEvent;
import com.rss.bean.RSSItemBean;
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

public class SubscriptionFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    Unbinder unbinder;
    @BindView(R.id.sf_rss_add)
    ImageView mSfRssAdd;
    @BindView(R.id.sf_recycler_view)
    RecyclerView mSfRecyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager mLayoutManager;
    private RssSourceAdapter mRssSourceAdapter;
    private List<RssSource> list = new ArrayList<>();

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
        mLayoutManager = new LinearLayoutManager(getContext());
        mSfRecyclerView.setLayoutManager(mLayoutManager);

        initData();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSysToolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mSysToolbar);
        mSysTvTitle.setText(getResources().getString(R.string.str_main_subscription));

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

    @OnClick({R.id.sf_rss_add})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sf_rss_add:
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
            if (mRssSourceAdapter == null) {
                mRssSourceAdapter = new RssSourceAdapter(getContext(), list);
                mSfRecyclerView.setAdapter(mRssSourceAdapter);
            } else {
                mRssSourceAdapter.notifyDataSetChanged();
            }
        }
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
    public void rssInsert(Website website) {
        try {
            List<RSSItemBean> rssTempList = new FeedReader().getContent(website);                   //获取有内容的 rssItemBean
            if (rssTempList != null) {
                for (RssSource rssSource : list) {
                    if (website.getFid().equals("" + rssSource.getId())) {
                        rssSource.setCount(rssTempList.size());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
