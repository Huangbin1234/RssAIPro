package com.hb.rssai.view.me;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.update.Config;
import com.hb.util.StringUtils;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.aa_tv_qq)
    TextView mAaTvQq;
    @BindView(R.id.la_iv)
    ImageView mLaIv;
    @BindView(R.id.aa_tv_ver)
    TextView mAaTvVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        mAaTvQq.setOnClickListener(v -> StringUtils.joinQQGroup(AboutActivity.this, "VyA7mXrlsAOQGFbuqX_0CL35MbSEPX3u"));
        mAaTvVer.setText("当前版本：V " + Config.getVerName(this));
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_about;
    }

    @Override
    protected void setAppTitle() {
        mSysToolbar.setTitle("");
        setSupportActionBar(mSysToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//设置ActionBar一个返回箭头，主界面没有，次级界面有
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mSysTvTitle.setText(getResources().getString(R.string.str_aa_title));
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

}
