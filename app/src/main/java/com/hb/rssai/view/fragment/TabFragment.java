package com.hb.rssai.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.hb.rssai.R;
import com.hb.rssai.adapter.MyPagerAdapter;
import com.hb.rssai.base.BaseFragment;
import com.hb.rssai.base.LazyFragment;
import com.hb.rssai.bean.ResDataGroup;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.TabPresenter;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.view.iView.ITabView;

import java.util.ArrayList;
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

    private List<String> datas = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private MyPagerAdapter myPagerAdapter;

    private String mParam1;
    private String mParam2;

    private int DF = 0; //0默认系统数据源1订阅
    private boolean isUser = false;

    private boolean isPrepared;

    private OnFragmentInteractionListener mListener;

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
        ((TabPresenter) mPresenter).getDataGroupList();
        isPrepared = false;
        System.out.println("====lazyLoad====");
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
            fragments.add(TabDataFragment.newInstance(i));
        }
        mSysTabLayout.addOnTabSelectedListener(this);
        myPagerAdapter = new MyPagerAdapter(getFragmentManager(), datas, fragments);
        mFtViewPager.setAdapter(myPagerAdapter);
        mSysTabLayout.setupWithViewPager(mFtViewPager);
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
        System.out.println("====onActivityCreated====");
        //初始化UI完成
        isPrepared = true;
        lazyLoad();
    }
}