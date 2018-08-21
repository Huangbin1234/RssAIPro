package com.hb.rssai.view.me;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.api.ApiRetrofit;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.MainEvent;
import com.hb.rssai.event.TipsEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.widget.PrgDialog;
import com.hb.update.Config;
import com.hb.update.UpdateManager;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.hb.generalupdate.TestTwoActivity.SAVE_ISUPDATE;
import static com.hb.update.UpdateManager.SAVE_VER_CODE;
import static com.hb.update.UpdateManager.SAVE_VER_CONTENT;
import static com.hb.update.UpdateManager.SAVE_VER_UPDATEURL;
import static com.hb.update.UpdateManager.SAVE_VER_VERNAME;

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
    @BindView(R.id.sa_rl_advice)
    RelativeLayout mSaRlAdvice;
    @BindView(R.id.sa_tv_ver)
    TextView mSaTvVer;
    @BindView(R.id.sa_tv_ver_label)
    TextView mSaTvVerLabel;

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
        boolean isLoadImage = SharedPreferencesUtil.getBoolean(this, Constant.KEY_IS_LOAD_IMAGE, false);
        if (isLoadImage) {
            mSaSwNoImage.setChecked(true);
        } else {
            mSaSwNoImage.setChecked(false);
        }
        boolean isNight = SharedPreferencesUtil.getBoolean(this, Constant.KEY_SYS_NIGHT_MODE, false);
        if (isNight) {
            mSaSwDayNight.setChecked(true);
        } else {
            mSaSwDayNight.setChecked(false);
        }
        mSaTvVer.setText("V " + Config.getVerName(this));
        //进入对应的页面判断标记是否有更新在进行调用此方法
        if (SharedPreferencesUtil.getBoolean(SettingActivity.this, Constant.SAVE_IS_UPDATE, false)) {
            //添加红点
            mSaTvVerLabel.setVisibility(View.VISIBLE);
        } else {
            mSaTvVerLabel.setVisibility(View.GONE);
        }
        mSaSwDayNight.setOnClickListener(v -> {
            SharedPreferencesUtil.setLong(SettingActivity.this, Constant.KEY_SYS_NIGHT_MODE_TIME, new Date().getTime());
            if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                SharedPreferencesUtil.setBoolean(SettingActivity.this, Constant.KEY_SYS_NIGHT_MODE, false);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                SharedPreferencesUtil.setBoolean(SettingActivity.this, Constant.KEY_SYS_NIGHT_MODE, true);
            }
            getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
            recreate();
            //此种方式通知首页更新主题
            EventBus.getDefault().post(new MainEvent(1));

        });
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

    @OnClick({R.id.sa_rl_about, R.id.sa_rl_advice, R.id.sa_rl_update, R.id.sa_rl_share})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sa_rl_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.sa_rl_advice:
                startActivity(new Intent(this, AdviceActivity.class));
                break;
            case R.id.sa_rl_update:
                checkUpdate();
                break;
            case R.id.sa_rl_share:
                try {
                    String alipayUrl = SharedPreferencesUtil.getString(this, Constant.AlipaysUrl, "");
                    if (alipayUrl.startsWith("alipays")) {
                        //利用Intent打开支付宝
                        Uri uri = Uri.parse(alipayUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    //若无法正常跳转，在此进行错误处理
                    T.ShowToast(this, "无法跳转到支付宝领红包，请检查您是否安装了支付宝！");
                }
                break;
            default:
                break;
        }
    }

    PrgDialog dialog;

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

    /**
     * request server checkvercode.json file.
     */
    class CheckVerRunnable implements Runnable {
        @Override
        public void run() {
            // replace your .json file url.
            boolean isUpdate = UpdateManager.getUpdateInfo(SettingActivity.this, ApiRetrofit.JSON_URL);
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
                    com.hb.util.SharedPreferencesUtil.setBoolean(SettingActivity.this, SAVE_ISUPDATE, false);//置为更新标记false
                    T.ShowToast(SettingActivity.this, "当前已是最新版本");
                    Log.d("GeneralUpdateLib", "There's no new version here.");
                    break;
                case 1:
                    // Find new version.
                    com.hb.util.SharedPreferencesUtil.setString(SettingActivity.this, SAVE_VER_UPDATEURL, UpdateManager.getVerUpdateURL());
                    com.hb.util.SharedPreferencesUtil.setString(SettingActivity.this, SAVE_VER_VERNAME, UpdateManager.getVerVerName());
                    com.hb.util.SharedPreferencesUtil.setString(SettingActivity.this, SAVE_VER_CODE, UpdateManager.getVerVerCode());
                    com.hb.util.SharedPreferencesUtil.setString(SettingActivity.this, SAVE_VER_CONTENT, UpdateManager.getVerContent());

                    com.hb.util.SharedPreferencesUtil.setBoolean(SettingActivity.this, SAVE_ISUPDATE, true);//置为更新标记true
                    //进入对应的页面判断标记是否有更新在进行调用此方法
                    if (SharedPreferencesUtil.getBoolean(SettingActivity.this, Constant.SAVE_IS_UPDATE, false)) {
                        UpdateManager.update(SettingActivity.this);
                    }
                    break;
                default:
                    break;
            }
            //弹出更新对话框
        }
    };

    @OnCheckedChanged({R.id.sa_sw_change_source, R.id.sa_sw_no_image})
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sa_sw_change_source:
                //初始化
                if (TextUtils.isEmpty(SharedPreferencesUtil.getString(this, Constant.SP_LOGIN_USER_NAME, ""))) {
                    mSaSwChangeSource.setChecked(false);
                    T.ShowToast(this, "未登录，无法设置！");
                    return;
                }
                if (isChecked) {
                    //changed subs source
                    int dateFrom = SharedPreferencesUtil.getInt(this, Constant.KEY_DATA_FROM, 0);
                    if (dateFrom == 0) {
                        //通知更新
                        new Handler().postDelayed(() -> EventBus.getDefault().post(new TipsEvent(2)), 2000);
                    }
                    SharedPreferencesUtil.setInt(this, Constant.KEY_DATA_FROM, 1);
                } else {
                    int dateFrom = SharedPreferencesUtil.getInt(this, Constant.KEY_DATA_FROM, 0);
                    if (dateFrom == 1) {
                        //通知更新
                        new Handler().postDelayed(() -> EventBus.getDefault().post(new TipsEvent(2)), 2000);
                    }
                    SharedPreferencesUtil.setInt(this, Constant.KEY_DATA_FROM, 0);
                }
                break;
            case R.id.sa_sw_no_image:
                if (isChecked) {
                    SharedPreferencesUtil.setBoolean(this, Constant.KEY_IS_LOAD_IMAGE, true);
                } else {
                    SharedPreferencesUtil.setBoolean(this, Constant.KEY_IS_LOAD_IMAGE, false);
                }
                //通知更新
                new Handler().postDelayed(() -> EventBus.getDefault().post(new TipsEvent(2)), 2000);
                break;
            default:
                break;
        }
    }
}
