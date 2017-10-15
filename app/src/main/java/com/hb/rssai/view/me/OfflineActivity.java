package com.hb.rssai.view.me;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.event.MineEvent;
import com.hb.rssai.event.OfflineEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.OfflinePresenter;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.view.iView.IOfficeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

public class OfflineActivity extends BaseActivity implements IOfficeView {

    @BindView(R.id.offline_list_view)
    ListView mOfflineListView;
    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.oa_btn_down)
    Button mOaBtnDown;
    @BindView(R.id.oa_chk_all)
    CheckBox mOaChkAll;
    @BindView(R.id.oa_chk_personal)
    CheckBox mOaChkPersonal;
    @BindView(R.id.oa_rl_all)
    RelativeLayout mOaRlAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((OfflinePresenter) mPresenter).getChannels();
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEventMainThread(OfflineEvent event) {
        mOaBtnDown.setText("ProgressVal:" + event.getProgressVal() + " ,MaxVal:" + event.getMaxVal());
        ((OfflinePresenter) mPresenter).setProgress(event.getId(), event.getProgressVal(), event.getMaxVal());
    }

    @Override
    protected void initView() {
        SharedPreferencesUtil.setBoolean(this, "isClickOffline", false);
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_offline;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_offline_title));
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

    @Override
    protected BasePresenter createPresenter() {
        return new OfflinePresenter(this, this);
    }

    @Override
    public ListView getListView() {
        return mOfflineListView;
    }

    @Override
    public CheckBox getChkAll() {
        return mOaChkAll;
    }

    @Override
    public RelativeLayout getOaRlAll() {
        return mOaRlAll;
    }

    @Override
    public Button getBtnDown() {
        return mOaBtnDown;
    }

    @Override
    protected void onStop() {
        if (SharedPreferencesUtil.getBoolean(this, "isClickOffline", false)) {
            EventBus.getDefault().post(new MineEvent(1));
        }
        super.onStop();
    }
}
