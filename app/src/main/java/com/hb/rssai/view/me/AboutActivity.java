package com.hb.rssai.view.me;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.api.ApiRetrofit;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.widget.PrgDialog;
import com.hb.update.Config;
import com.hb.update.UpdateManager;
import com.hb.util.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hb.generalupdate.TestTwoActivity.SAVE_ISUPDATE;
import static com.hb.update.UpdateManager.SAVE_VER_CODE;
import static com.hb.update.UpdateManager.SAVE_VER_CONTENT;
import static com.hb.update.UpdateManager.SAVE_VER_UPDATEURL;
import static com.hb.update.UpdateManager.SAVE_VER_VERNAME;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.aa_tv_ver_right)
    TextView mAaTvVerRight;
    @BindView(R.id.aa_tv_ver_label)
    TextView mAaTvVerLabel;
    @BindView(R.id.aa_rl_update)
    RelativeLayout mAaRlUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mAaTvQq.setOnClickListener(v -> StringUtils.joinQQGroup(AboutActivity.this, "VyA7mXrlsAOQGFbuqX_0CL35MbSEPX3u"));
        mAaTvVer.setText("当前版本：V " + Config.getVerName(this));

        mAaTvVerRight.setText("V " + Config.getVerName(this));
        //进入对应的页面判断标记是否有更新在进行调用此方法
        if (SharedPreferencesUtil.getBoolean(this, Constant.SAVE_IS_UPDATE, false)) {
            //添加红点
            mAaTvVerLabel.setVisibility(View.VISIBLE);
        } else {
            mAaTvVerLabel.setVisibility(View.GONE);
        }
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

    @OnClick({R.id.aa_rl_update})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aa_rl_update:
                checkUpdate();
                break;
            default:
                break;
        }
    }
    public void checkUpdate() {
        ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cwjManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            dialog = new PrgDialog(this, true);
            CheckVerRunnable checkRunnable = new CheckVerRunnable();
            Thread checkThread = new Thread(checkRunnable);
            checkThread.start();
        } else {
            T.ShowToast(this, Constant.FAILED_NETWORK);
        }
    }

    PrgDialog dialog;
    /**
     * request server checkvercode.json file.
     */
    class CheckVerRunnable implements Runnable {
        @Override
        public void run() {
            // replace your .json file url.
            boolean isUpdate = UpdateManager.getUpdateInfo(AboutActivity.this, ApiRetrofit.JSON_URL);
            if (isUpdate) {
                updateHandler.sendEmptyMessage(1);
            } else {
                updateHandler.sendEmptyMessage(0);
            }
        }
    }

    /**
     * handler rec.
     */
    @SuppressLint("HandlerLeak")
    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dialog.closeDialog();
            switch (msg.what) {
                case 0:
                    // To do something.
                    com.hb.util.SharedPreferencesUtil.setBoolean(AboutActivity.this, SAVE_ISUPDATE, false);//置为更新标记false
                    T.ShowToast(AboutActivity.this, "当前已是最新版本");
                    Log.d("GeneralUpdateLib", "There's no new version here.");
                    break;
                case 1:
                    // Find new version.
                    com.hb.util.SharedPreferencesUtil.setString(AboutActivity.this, SAVE_VER_UPDATEURL, UpdateManager.getVerUpdateURL());
                    com.hb.util.SharedPreferencesUtil.setString(AboutActivity.this, SAVE_VER_VERNAME, UpdateManager.getVerVerName());
                    com.hb.util.SharedPreferencesUtil.setString(AboutActivity.this, SAVE_VER_CODE, UpdateManager.getVerVerCode());
                    com.hb.util.SharedPreferencesUtil.setString(AboutActivity.this, SAVE_VER_CONTENT, UpdateManager.getVerContent());

                    com.hb.util.SharedPreferencesUtil.setBoolean(AboutActivity.this, SAVE_ISUPDATE, true);//置为更新标记true
                    //进入对应的页面判断标记是否有更新在进行调用此方法
                    if (SharedPreferencesUtil.getBoolean(AboutActivity.this, Constant.SAVE_IS_UPDATE, false)) {
                        UpdateManager.update(AboutActivity.this);
                    }
                    break;
                default:
                    break;
            }
            //弹出更新对话框
        }
    };
}
