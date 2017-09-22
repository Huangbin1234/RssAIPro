package com.hb.rssai.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.hb.rssai.presenter.SearchInfoPresenter;
import com.hb.rssai.view.me.SearchActivity;
import com.hb.rssai.view.me.SearchInfoFragment;
import com.hb.rssai.view.me.SearchSubscribeFragment;

/**
 * Created by Administrator on 2016/12/21.
 */

public class SearchFragmentAdapter extends FragmentPagerAdapter {

    private String[] mTitles = {"资讯", "主题"};
    private SearchInfoFragment oneFragment;
    private SearchSubscribeFragment twoFragment;


private SearchActivity searchActivity;

    public SearchFragmentAdapter(FragmentManager fm, Activity act) {
        super(fm);
        searchActivity=(SearchActivity) act;
        oneFragment = new SearchInfoFragment();
        twoFragment = new SearchSubscribeFragment();

        searchActivity.setSearchListener(new SearchActivity.SearchListener() {
            @Override
            public void search(String val) {
                ((SearchInfoPresenter)oneFragment.mPresenter).refreshInfoList();
            }
        });
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return oneFragment;
        } else if (position == 1) {
            return twoFragment;
        } else {
            return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //防止来回切换时候销毁碎片
    }
}
