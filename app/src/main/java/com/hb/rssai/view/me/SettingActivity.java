package com.hb.rssai.view.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.presenter.BasePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.sa_rl_day_night)
    RelativeLayout mSaRlDayNight;
    @BindView(R.id.sa_rl_opr_image)
    RelativeLayout mSaRlOprImage;
    @BindView(R.id.sa_rl_share)
    RelativeLayout mSaRlShare;
    @BindView(R.id.sa_rl_update)
    RelativeLayout mSaRlUpdate;
    @BindView(R.id.sa_rl_about)
    RelativeLayout mSaRlAbout;
    @BindView(R.id.sa_ll)
    LinearLayout mSaLl;
    @BindView(R.id.sa_sw_day_night)
    Switch mSaSwDayNight;
    @BindView(R.id.sa_sw_no_image)
    Switch mSaSwNoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_setting;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_sa_title));
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @OnClick({R.id.sa_rl_about})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sa_rl_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
