package com.hb.rssai.view.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.MyPagerAdapter;
import com.hb.rssai.base.BaseFragment;
import com.hb.rssai.bean.ResDataGroup;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.TipsEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.TabPresenter;
import com.hb.rssai.util.GsonUtil;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.view.iView.ITabView;
import com.hb.rssai.view.widget.TipTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;


public class TabFragment extends BaseFragment implements TabLayout.OnTabSelectedListener, ITabView {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.sys_tabLayout)
    TabLayout mSysTabLayout;
    @BindView(R.id.ft_viewPager)
    ViewPager mFtViewPager;
    @BindView(R.id.hf_ll_root)
    LinearLayout mHfLlRoot;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.ft_tv_tips)
    TipTextView mFtTvTips;
    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_iv_filter)
    ImageView mSysIvFilter;


    private List<String> datas = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private MyPagerAdapter myPagerAdapter;

    private String mParam1;
    private String mParam2;

    private int DF = 0; //0默认系统数据源1订阅

    private boolean isPrepared;

    private OnFragmentInteractionListener mListener;
    private TabDataFragment mTabDataFragment;

    public TabFragment() {
    }

    public static TabFragment newInstance(String param1, String param2) {
        TabFragment fragment = new TabFragment();
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

    @Subscribe
    public void onEventMainThread(TipsEvent event) {
        if (event.getMessage() == 1 && event.getNewsCount() > 0) {
            getActivity().runOnUiThread(() -> {
                mFtTvTips.setVisibility(View.VISIBLE);
                mFtTvTips.setText("发现" + event.getNewsCount() + "条新消息");
                mFtTvTips.showTips();
            });
        } else if (event.getMessage() == 2) {
            ResDataGroup resDataGroup = getCacheDataGroup();
            if (null == resDataGroup) {
                ((TabPresenter) mPresenter).getDataGroupList();
            } else {
                ((TabPresenter) mPresenter).setDataGroupResult(resDataGroup);
            }
        }
    }

    @Override
    protected void setAppTitle() {

    }

    @Override
    protected void lazyLoad() {
        if (!isVisible || !isPrepared) {
            return;
        }
        //loadData(DF);
        ResDataGroup resDataGroup = getCacheDataGroup();
        if (null == resDataGroup) {
            ((TabPresenter) mPresenter).getDataGroupList();
        } else {
            ((TabPresenter) mPresenter).setDataGroupResult(resDataGroup);
        }
        isPrepared = false;
    }

    @Override
    protected BasePresenter createPresenter() {
        return new TabPresenter(getContext(), this);
    }

    View rView;

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (rView != null) {
            if (hidden) {
                mHfLlRoot.setFitsSystemWindows(false);
            } else {
                mHfLlRoot.setFitsSystemWindows(true);
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                rView.requestApplyInsets();
            } else {
                rView.requestFitSystemWindows();
            }
        }
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.fragment_tab;
    }


    @Override
    protected void initView(View rootView) {
        rView = rootView;
        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getContext().getResources().getDisplayMetrics());
            mFtTvTips.setTitleHeight(actionBarHeight);//把标题栏的高度赋值给自定义的TextView
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void loadGroupDown() {
        if (datas != null && datas.size() > 0) {
            datas.clear();
        }
        if (fragments != null && fragments.size() > 0) {
            fragments.clear();
        }

        DF = SharedPreferencesUtil.getInt(getContext(), Constant.KEY_DATA_FROM, 0);
        List<ResDataGroup.RetObjBean.RowsBean> groupList;
        if (DF == 0) {
            groupList = ((TabPresenter) mPresenter).getGroupList();
        } else if (DF == 1) {
            groupList = ((TabPresenter) mPresenter).getMeGroupList();
        } else {
            groupList = new ArrayList<>();
        }
        //循环注入标签
        for (ResDataGroup.RetObjBean.RowsBean tab : groupList) {
            mSysTabLayout.addTab(mSysTabLayout.newTab().setText(tab.getName()));
            datas.add(tab.getName());
        }
        //设置TabLayout点击事件
        for (int i = 0; i < datas.size(); i++) {
            mTabDataFragment = TabDataFragment.newInstance(i);
            fragments.add(mTabDataFragment);
            //设置监听回到顶部事件
//            mTabDataFragment.setOnBackTopListener(mFtdRecyclerView -> mFtdRecyclerView.scrollToPosition(0));
        }
        mSysTabLayout.addOnTabSelectedListener(this);
        mFtViewPager.setOffscreenPageLimit(0);
        myPagerAdapter = new MyPagerAdapter(getFragmentManager(), datas, fragments);
        mFtViewPager.setAdapter(myPagerAdapter);

        mSysTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.custom_tab_layout_text);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextColor(mSysTabLayout.getTabTextColors());
                textView.setTypeface(Typeface.DEFAULT_BOLD);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.custom_tab_layout_text);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTypeface(Typeface.DEFAULT);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mSysTabLayout.setupWithViewPager(mFtViewPager);



    }

    @Override
    public void cacheDataGroup(ResDataGroup resDataGroup) {
        if (null == resDataGroup || resDataGroup.getRetCode() != 0) {
            return;
        }
        String str_data_group = GsonUtil.toJson(resDataGroup);
        SharedPreferencesUtil.setString(getContext(), Constant.KEY_DATA_GROUP, str_data_group);
        SharedPreferencesUtil.setLong(getContext(), Constant.KEY_DATA_GROUP_TIME, new Date().getTime());
    }

    /**
     * 获取缓存数据源
     *
     * @return
     */
    private ResDataGroup getCacheDataGroup() {
        String str_data_group = SharedPreferencesUtil.getString(getContext(), Constant.KEY_DATA_GROUP, "");
        if (TextUtils.isEmpty(str_data_group)) {
            return null;
        }
        long cacheTime = SharedPreferencesUtil.getLong(getContext(), Constant.KEY_DATA_GROUP_TIME, 0);
        long nowTime = new Date().getTime();
        if (nowTime - cacheTime > 7 * 24 * 60 * 60 * 1000) {
            return null;
        } else {
            return GsonUtil.getGsonUtil().getBean(str_data_group, ResDataGroup.class);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mFtViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化UI完成
        isPrepared = true;
        lazyLoad();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消注册
        EventBus.getDefault().unregister(this);
    }
}
