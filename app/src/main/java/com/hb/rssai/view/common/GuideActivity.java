package com.hb.rssai.view.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.hb.rssai.R;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.view.fragment.guide.Fragment1;
import com.hb.rssai.view.fragment.guide.Fragment2;
import com.hb.rssai.view.fragment.guide.Fragment3;
import com.hb.rssai.view.fragment.guide.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends FragmentActivity {
    private ViewPager viewPager;
    private List<View> listImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        if (SharedPreferencesUtil.getString(this, Constant.KEY_GUIDE, "2").equals("1")) {
            Intent intent = new Intent(GuideActivity.this, LoadActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_guide);
            initView();
        }
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), showView()));
        listImg = new ArrayList<>();
        listImg.add(findViewById(R.id.y1));
        listImg.add(findViewById(R.id.y2));
        listImg.add(findViewById(R.id.y3));
        viewPager.setOnPageChangeListener(showPageChange);
    }

    ViewPager.OnPageChangeListener showPageChange = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < listImg.size(); i++) {
                if (i == arg0) {
                    listImg.get(arg0).setBackgroundResource(R.drawable.y_focused);
                } else {
                    listImg.get(i).setBackgroundResource(R.drawable.y_normal);
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }
    };


    private List<Fragment> showView() {
        List<Fragment> listView = new ArrayList<>();
        listView.add(new Fragment1());
        listView.add(new Fragment2());
        listView.add(new Fragment3());
        return listView;
    }
}
