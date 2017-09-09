package com.hb.rssai.view.subscription.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResDataGroup;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.TabContentPresenter;
import com.hb.rssai.presenter.TabResourcePresenter;
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.view.iView.ITabResourceView;
import com.hb.rssai.view.widget.tab.CoordinatorTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class TabResourceActivity extends BaseActivity implements ITabResourceView {

    @BindView(R.id.vp)
    ViewPager mViewPager;
    @BindView(R.id.coordinatortablayout)
    CoordinatorTabLayout mCoordinatorTabLayout;
    private ArrayList<Fragment> mFragments;
    List<ResDataGroup.RetObjBean.RowsBean> mRowsBeanList;
    private int[] mColorArray;
    private List<TabResourceFragment> fList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        ((TabResourcePresenter) mPresenter).getGroupType();
    }

    @Override
    protected void initView() {
        mColorArray = new int[]{
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light};
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_tab_resource;
    }

    @Override
    protected void setAppTitle() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return new TabResourcePresenter(this, this);
    }



    private void initFragments(List<ResDataGroup.RetObjBean.RowsBean> rows) {
        mRowsBeanList = rows;
        mFragments = new ArrayList<>();
        for (ResDataGroup.RetObjBean.RowsBean row : rows) {
            TabResourceFragment trFragment = TabResourceFragment.getInstance(row.getName());
            mFragments.add(trFragment);
            fList.add(trFragment);
        }
    }

    private void initViewPager() {
        mViewPager.setOffscreenPageLimit(mRowsBeanList.size());
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragments, mRowsBeanList));
    }

    @Override
    public void setUi(List<ResDataGroup.RetObjBean.RowsBean> rowsBeen) {
        initFragments(rowsBeen);
        initViewPager();
        mCoordinatorTabLayout.setTransulcentStatusBar(this)
                .setTitle("")
                .setBackEnable(true)
                // .setContentScrimColorArray(mColorArray)
                .setLoadHeaderImagesListener((imageView, tab) -> {

                    if (null == loadMap.get(tab.getPosition())) {
                        HttpLoadImg.loadCircleImg(TabResourceActivity.this, mRowsBeanList.get(tab.getPosition()).getUrl(), imageView);
                        //加载数据
                        ((TabContentPresenter) fList.get(tab.getPosition()).mPresenter).getListData(mRowsBeanList.get(tab.getPosition()).getVal());
                        loadMap.put(tab.getPosition(), true);
                    }
                })
                .setupWithViewPager(mViewPager);
    }

    private Map<Integer, Boolean> loadMap = new HashMap<>();
}
