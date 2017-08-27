package com.hb.rssai.view.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.HomeSourceEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

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
    @BindView(R.id.sa_sw_change_source)
    Switch mSaSwChangeSource;
    @BindView(R.id.sa_rl_change_source)
    RelativeLayout mSaRlChangeSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        int dateFrom = SharedPreferencesUtil.getInt(this, Constant.KEY_DATA_FROM, 0);
        if (dateFrom == 0) {
            mSaSwChangeSource.setChecked(false);
        } else if (dateFrom == 1) {
            mSaSwChangeSource.setChecked(true);
        }
        boolean isLoadImage = SharedPreferencesUtil.getBoolean(this, Constant.KEY_IS_LOAD_IMAGE, true);
        if (isLoadImage) {
            mSaSwNoImage.setChecked(true);
        } else {
            mSaSwNoImage.setChecked(false);
        }
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

    @OnClick({R.id.sa_rl_about, R.id.sa_rl_advice})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sa_rl_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.sa_rl_advice:
                startActivity(new Intent(this, AdviceActivity.class));
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

    @OnCheckedChanged({R.id.sa_sw_change_source, R.id.sa_sw_no_image})
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sa_sw_change_source:
                //初始化
                if (TextUtils.isEmpty(SharedPreferencesUtil.getString(this, Constant.SP_LOGIN_USER_NAME, ""))) {
                    mSaSwChangeSource.setChecked(false);
                    T.ShowToast(this,"未登录，无法设置！");
                    return;
                }
                if (isChecked) {
                    //changed subs source
                    SharedPreferencesUtil.setInt(this, Constant.KEY_DATA_FROM, 1);
                    EventBus.getDefault().post(new HomeSourceEvent(1));
                } else {
                    SharedPreferencesUtil.setInt(this, Constant.KEY_DATA_FROM, 0);
                    EventBus.getDefault().post(new HomeSourceEvent(0));
                }
                break;
            case R.id.sa_sw_no_image:
                if (isChecked) {
                    SharedPreferencesUtil.setBoolean(this, Constant.KEY_IS_LOAD_IMAGE, true);
                } else {
                    SharedPreferencesUtil.setBoolean(this, Constant.KEY_IS_LOAD_IMAGE, false);
                }
                EventBus.getDefault().post(new HomeSourceEvent(3));
                break;
        }
    }
}
