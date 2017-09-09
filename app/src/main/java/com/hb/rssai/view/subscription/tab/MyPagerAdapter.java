package com.hb.rssai.view.subscription.tab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hb.rssai.bean.ResDataGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugeterry(http://hugeterry.cn)
 * Date: 16/1/28 17:24
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private List<ResDataGroup.RetObjBean.RowsBean> mTitles;

    public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> mFragments, List<ResDataGroup.RetObjBean.RowsBean> mTitles) {
        super(fm);
        this.mFragments = mFragments;
        this.mTitles = mTitles;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position).getName();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
