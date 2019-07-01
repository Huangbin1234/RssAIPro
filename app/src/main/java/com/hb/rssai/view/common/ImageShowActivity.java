package com.hb.rssai.view.common;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.ImagePagerAdapter;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 图片显示
 */
public class ImageShowActivity extends BaseActivity {


    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    private ImagePagerAdapter pagerAdapter;
    private List<String> list = new ArrayList<>();
    private int pos;
    public static final String KEY_IMAGE_POS = "image_pos";
    public static final String KEY_IMAGE_BEAN = "image_bean";

    @Override
    protected void initIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pos = bundle.getInt(KEY_IMAGE_POS, 0);
            list = bundle.getStringArrayList(KEY_IMAGE_BEAN);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        mViewPager.setOffscreenPageLimit(0);

        pagerAdapter = new ImagePagerAdapter(this, list, pos);
        mViewPager.setAdapter(pagerAdapter);
//        pagerAdapter.notifyDataSetChanged();

        mViewPager.setCurrentItem(pos);

        mIvBack.setOnClickListener(v -> finish());
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_image_show;
    }

    @Override
    protected void setAppTitle() {
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

}
