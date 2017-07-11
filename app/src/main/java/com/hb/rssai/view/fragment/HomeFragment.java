package com.hb.rssai.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.RssListAdapter;
import com.hb.rssai.util.T;
import com.hb.rssai.view.widget.PrgDialog;
import com.rss.bean.RSSItemBean;
import com.rss.bean.Website;
import com.rss.util.Dom4jUtil;
import com.rss.util.FeedReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.hf_recycler_view)
    RecyclerView mHfRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.hf_swipe_layout)
    SwipeRefreshLayout mHfSwipeLayout;
    @BindView(R.id.hf_tv_empty)
    TextView mHfTvEmpty;
    @BindView(R.id.hf_ll)
    LinearLayout mHfLl;
    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager mLayoutManager;
    List<RSSItemBean> rssList = new ArrayList<>();
    private PrgDialog prgDialog;
    private RssListAdapter rssListAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSysToolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mSysToolbar);
        mSysTvTitle.setText(getResources().getString(R.string.str_main_home));
    }

    private void initView() {
        mLayoutManager = new LinearLayoutManager(getContext());
        mHfRecyclerView.setLayoutManager(mLayoutManager);
        mHfSwipeLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        mHfSwipeLayout.setProgressViewOffset(true, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        new ReadRssTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class ReadRssTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new PrgDialog(getContext(), true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            readRssXml();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mHfLl.setVisibility(View.GONE);
            prgDialog.closeDialog();
            if (rssList != null && rssList.size() > 0) {
                if (rssListAdapter == null) {
                    rssListAdapter = new RssListAdapter(getContext(), rssList);
                }
                mHfRecyclerView.setAdapter(rssListAdapter);
            }
        }
    }


    private void readRssXml() {
        InputStream in = null;
        try {
            in = getResources().getAssets().open("website.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (in == null) {
            T.ShowToast(getContext(), "xml文件不存在");
            return;
        }
        List<Website> websiteList = new Dom4jUtil().parserXml(in);
        for (Website we : websiteList) {
            if (we.getOpen().equals("true")) {         //只对开启的website  spider
                System.out.println("==========begin spide " + we.getName() + ".==============");
                rssInsert(we);
                System.out.println("==========end spide " + we.getName() + ".==============");
            }
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
                rssList.addAll(rssTempList);
//                int size = rssList.size();
//                for (int i = 0; i < size; i++) {
//                    RSSItemBean rs = rssList.get(i);
//                    System.out.println(rs.getPubDate() + " " + rs.getTitle()+" "+rs.getDescription()+" "+rs.getLink());
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
