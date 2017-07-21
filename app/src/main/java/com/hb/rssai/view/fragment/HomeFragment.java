package com.hb.rssai.view.fragment;

import android.content.Context;
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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.FilterDialogAdapter;
import com.hb.rssai.adapter.RssListAdapter;
import com.hb.rssai.bean.RssSort;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.HomeSourceEvent;
import com.hb.rssai.util.RssDataSourceUtil;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.view.widget.FullGridView;
import com.rss.bean.RSSItemBean;
import com.rss.bean.Website;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.drakeet.materialdialog.MaterialDialog;

public class HomeFragment extends Fragment implements View.OnClickListener {
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
    @BindView(R.id.sys_iv_filter)
    ImageView mSysIvFilter;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager mLayoutManager;
    List<RSSItemBean> rssList = new ArrayList<>();
    private RssListAdapter rssListAdapter;

    List<Website> sites = null;
    private int DF = 0; //0默认系统数据源1订阅

    public HomeFragment() {
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
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEventMainThread(HomeSourceEvent event) {
        if (event.getMessage() == 1) {
            DF = 1;
        } else if (event.getMessage() == 0) {
            DF = 0;
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

        //TODO 设置下拉刷新
        mHfSwipeLayout.setOnRefreshListener(() -> {
            loadData(DF);
        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        DF = SharedPreferencesUtil.getInt(getContext(), Constant.KEY_DATA_FROM, 0);
        loadData(DF);
    }

    private synchronized void loadData(int dataFrom) {
        if (rssList != null && rssList.size() > 0) {
            rssList.clear();
            if (rssListAdapter != null) {
                rssListAdapter.notifyDataSetChanged();
            }
        }
        if (sites != null && sites.size() > 0) {
            sites.clear();
        }

        if (dataFrom == 0) {
            sites = RssDataSourceUtil.readFromAsset(getActivity());
        } else if (dataFrom == 1) {
            sites = RssDataSourceUtil.readFromDb();
        }
        if (sites != null && sites.size() > 0) {
            new ReadRssTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.sys_iv_filter})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sys_iv_filter:
                openFilter();
                break;
        }
    }
    /**
     * 构造对话框数据
     *
     * @return
     */
    private List<HashMap<String, Object>> initDialogData() {
        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "要闻");
        map.put("id", 1);
        map.put("url", R.mipmap.ic_place);
        list.add(map);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("name", "科技");
        map2.put("id", 2);
        map2.put("url", R.mipmap.ic_place);
        list.add(map2);
        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("name", "探索");
        map3.put("id", 3);
        map3.put("url", R.mipmap.ic_place);
        list.add(map3);
        HashMap<String, Object> map4 = new HashMap<>();
        map4.put("name", "军事");
        map4.put("id", 4);
        map4.put("url", R.mipmap.ic_place);
        list.add(map4);
        HashMap<String, Object> map5 = new HashMap<>();
        map5.put("name", "娱乐");
        map5.put("id", 5);
        map5.put("url", R.mipmap.ic_place);
        list.add(map5);
        HashMap<String, Object> map6 = new HashMap<>();
        map6.put("name", "数码");
        map6.put("id", 6);
        map6.put("url", R.mipmap.ic_place);
        list.add(map6);
        HashMap<String, Object> map7 = new HashMap<>();
        map7.put("name", "游戏");
        map7.put("id", 7);
        map7.put("url", R.mipmap.ic_place);
        list.add(map7);
        return list;
    }
    /**
     * 菜单对话框
     *
     * @return
     */
    FilterDialogAdapter dialogAdapter;
    MaterialDialog materialDialog;

    private void openFilter() {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog(getContext());
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.view_filter, null);
            FullGridView listView = (FullGridView) view.findViewById(R.id.dialog_gridView);

            List<HashMap<String, Object>> list = initDialogData();
            listView.setOnItemClickListener((parent, view1, position, id) -> {

            });
            if (dialogAdapter == null) {
                dialogAdapter = new FilterDialogAdapter(getContext(), list);
                listView.setAdapter(dialogAdapter);
            }
            dialogAdapter.notifyDataSetChanged();
            materialDialog.setContentView(view).setTitle(Constant.TIPS_FILTER).setNegativeButton("关闭", v -> {
                materialDialog.dismiss();
            }).show();
        } else {
            materialDialog.show();
        }
    }

    class ReadRssTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<RSSItemBean> tempList = null;
            for (Website website : sites) {
                tempList = RssDataSourceUtil.getRssData(website, 5);
                if (tempList != null && tempList.size() > 0) {
                    rssList.addAll(tempList);
                }
                System.out.println(website.getName());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mHfSwipeLayout.setRefreshing(false);
            mHfLl.setVisibility(View.GONE);
            if (rssList != null && rssList.size() > 0) {
                RssSort cm = new RssSort();
                Collections.sort(rssList, cm);
                if (rssListAdapter == null) {
                    rssListAdapter = new RssListAdapter(getContext(), rssList);
                }
                mHfRecyclerView.setAdapter(rssListAdapter);
            }
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
