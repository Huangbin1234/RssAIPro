package com.hb.rssai.view.me;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.MainEvent;
import com.hb.rssai.event.MineEvent;
import com.hb.rssai.event.TipsEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.util.DisplayUtil;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.util.ThemeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

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
    @BindView(R.id.sa_tv_change_source)
    TextView mSaTvChangeSource;
    @BindView(R.id.sa_sw_change_source)
    Switch mSaSwChangeSource;
    @BindView(R.id.sa_rl_change_source)
    RelativeLayout mSaRlChangeSource;
    @BindView(R.id.sa_tv_opr_image)
    TextView mSaTvOprImage;
    @BindView(R.id.sa_sw_no_image)
    Switch mSaSwNoImage;
    @BindView(R.id.sa_rl_opr_image)
    RelativeLayout mSaRlOprImage;
    @BindView(R.id.sa_tv_cold_reboot)
    TextView mSaTvColdReboot;
    @BindView(R.id.sa_sw_cold_reboot)
    Switch mSaSwColdReboot;
    @BindView(R.id.sa_rl_cold_reboot)
    RelativeLayout mSaRlColdReboot;
    @BindView(R.id.sa_tv_offline)
    TextView mSaTvOffline;
    @BindView(R.id.sa_sw_offline)
    Switch mSaSwOffline;
    @BindView(R.id.sa_rl_offline)
    RelativeLayout mSaRlOffline;
    @BindView(R.id.sa_tv_day_night)
    TextView mSaTvDayNight;
    @BindView(R.id.sa_sw_day_night)
    Switch mSaSwDayNight;
    @BindView(R.id.sa_rl_day_night)
    RelativeLayout mSaRlDayNight;
    @BindView(R.id.sa_tv_theme)
    TextView mSaTvTheme;
    @BindView(R.id.sa_rl_theme)
    RelativeLayout mSaRlTheme;
    @BindView(R.id.sa_rl_share)
    RelativeLayout mSaRlShare;
    @BindView(R.id.sa_rl_advice)
    RelativeLayout mSaRlAdvice;
    @BindView(R.id.sa_rl_about)
    RelativeLayout mSaRlAbout;
    @BindView(R.id.sa_ll)
    LinearLayout mSaLl;
    @BindView(R.id.sa_tv_old_rec)
    TextView mSaTvOldRec;
    @BindView(R.id.sa_sw_old_rec)
    Switch mSaSwOldRec;
    @BindView(R.id.sa_rl_old_rec)
    RelativeLayout mSaRlOldRec;

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
        boolean isLoadImage = SharedPreferencesUtil.getBoolean(this, Constant.KEY_IS_NO_IMAGE_MODE, false);
        if (isLoadImage) {
            mSaSwNoImage.setChecked(true);
        } else {
            mSaSwNoImage.setChecked(false);
        }
        boolean isOffline = SharedPreferencesUtil.getBoolean(this, Constant.KEY_IS_OFFLINE_MODE, false);
        if (isOffline) {
            mSaSwOffline.setChecked(true);
        } else {
            mSaSwOffline.setChecked(false);
        }

        boolean isNight = SharedPreferencesUtil.getBoolean(this, Constant.KEY_SYS_NIGHT_MODE, false);
        if (isNight) {
            mSaSwDayNight.setChecked(true);
        } else {
            mSaSwDayNight.setChecked(false);
        }
        boolean isColdReboot = SharedPreferencesUtil.getBoolean(this, Constant.KEY_IS_COLD_REBOOT_MODE, false);
        if (isColdReboot) {
            mSaSwColdReboot.setChecked(true);
        } else {
            mSaSwColdReboot.setChecked(false);
        }

        boolean isOldRec = SharedPreferencesUtil.getBoolean(this, Constant.KEY_IS_OLD_REC_MODE, false);
        if (isOldRec) {
            mSaSwOldRec.setChecked(true);
        } else {
            mSaSwOldRec.setChecked(false);
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
//            recreate();
            //此种方式通知首页更新主题
            EventBus.getDefault().post(new MainEvent(1));

            //TODO 设置主题
            setTheme(R.style.Theme_default);
            setColor();

            SharedPreferencesUtil.setInt(this, Constant.KEY_THEME, R.style.Theme_default);
        });
        initThemePop();
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

    @OnClick({R.id.sa_rl_about, R.id.sa_rl_advice, R.id.sa_rl_share, R.id.sa_rl_theme})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sa_rl_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.sa_rl_advice:
                startActivity(new Intent(this, AdviceActivity.class));
                break;
            case R.id.sa_rl_share:
                try {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            case R.id.sa_rl_theme:
                showThemePop();
                break;
            default:
                break;
        }
    }

    View popupView;
    //    PopupWindow mPop;
    Dialog mPop;

    private void showThemePop() {
        if (mPop.isShowing()) {
            mPop.dismiss();
        } else {
            mPop.show();
            Window window = mPop.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.setContentView(popupView);//自定义布局应该在这里添加，要在dialog.show()的后面
            window.setWindowAnimations(R.style.PopupAnimation);//
            window.setLayout(DisplayUtil.getMobileWidth(this) * 8 / 10, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPop.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        }
        backgroundAlpha(0.5f);
        mPop.setOnDismissListener(dialogInterface -> {
            //Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        });
    }

    /**
     * 弹出主题设置框
     */
    private void initThemePop() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        popupView = inflater.inflate(R.layout.pop_theme, null);
        mPop = builder.create();

        View pt_v_red = popupView.findViewById(R.id.pt_v_red);
        View pt_v_blue = popupView.findViewById(R.id.pt_v_blue);
        View pt_v_green = popupView.findViewById(R.id.pt_v_green);

        View pt_v_brown = popupView.findViewById(R.id.pt_v_brown);
        View pt_v_orange = popupView.findViewById(R.id.pt_v_orange);
        View pt_v_light_green = popupView.findViewById(R.id.pt_v_light_green);


        ImageView pas_iv_close = popupView.findViewById(R.id.pas_iv_close);

        pas_iv_close.setOnClickListener(arg0 -> {
            if (mPop.isShowing()) {
                mPop.dismiss();
            }
        });
        pt_v_red.setOnClickListener(v -> {
            if (mPop.isShowing()) {
                mPop.dismiss();
            }
            //TODO 设置主题
            setTheme(R.style.Theme_default);
            setColor();

            SharedPreferencesUtil.setInt(this, Constant.KEY_THEME, R.style.Theme_default);
        });
        pt_v_blue.setOnClickListener(v -> {
            if (mPop.isShowing()) {
                mPop.dismiss();
            }
            //TODO 设置主题
            setTheme(R.style.Theme_blue);
            setColor();

            SharedPreferencesUtil.setInt(this, Constant.KEY_THEME, R.style.Theme_blue);
        });
        pt_v_green.setOnClickListener(v -> {
            if (mPop.isShowing()) {
                mPop.dismiss();
            }
            //TODO 设置主题
            setTheme(R.style.Theme_green);
            setColor();

            SharedPreferencesUtil.setInt(this, Constant.KEY_THEME, R.style.Theme_green);
        });

        pt_v_brown.setOnClickListener(v -> {
            if (mPop.isShowing()) {
                mPop.dismiss();
            }
            //TODO 设置主题
            setTheme(R.style.Theme_Theme17);
            setColor();

            SharedPreferencesUtil.setInt(this, Constant.KEY_THEME, R.style.Theme_Theme17);
        });
        pt_v_orange.setOnClickListener(v -> {
            if (mPop.isShowing()) {
                mPop.dismiss();
            }
            //TODO 设置主题
            setTheme(R.style.Theme_Theme15);
            setColor();

            SharedPreferencesUtil.setInt(this, Constant.KEY_THEME, R.style.Theme_Theme15);
        });
        pt_v_light_green.setOnClickListener(v -> {
            if (mPop.isShowing()) {
                mPop.dismiss();
            }
            //TODO 设置主题
            setTheme(R.style.Theme_Theme10);
            setColor();

            SharedPreferencesUtil.setInt(this, Constant.KEY_THEME, R.style.Theme_Theme10);
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    //    当前页面，主题切换后，需要手动进行颜色修改。下面只修改了状态栏和ToolBar的颜色。
    public void setColor() {
        ThemeUtils.setToolbarColor(SettingActivity.this, ThemeUtils.getPrimaryColor(SettingActivity.this));
        ThemeUtils.setWindowStatusBarColor(SettingActivity.this, ThemeUtils.getPrimaryDarkColor(SettingActivity.this));

        getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
        recreate();

        EventBus.getDefault().post(new MineEvent(3));
    }

    @OnCheckedChanged({R.id.sa_sw_change_source, R.id.sa_sw_no_image, R.id.sa_sw_cold_reboot, R.id.sa_sw_offline,R.id.sa_sw_old_rec})
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
                    SharedPreferencesUtil.setBoolean(this, Constant.KEY_IS_NO_IMAGE_MODE, true);
                } else {
                    SharedPreferencesUtil.setBoolean(this, Constant.KEY_IS_NO_IMAGE_MODE, false);
                }
                //通知更新
                new Handler().postDelayed(() -> EventBus.getDefault().post(new TipsEvent(2)), 2000);
                break;
            case R.id.sa_sw_cold_reboot:
                if (isChecked) {
                    SharedPreferencesUtil.setBoolean(this, Constant.KEY_IS_COLD_REBOOT_MODE, true);
                } else {
                    SharedPreferencesUtil.setBoolean(this, Constant.KEY_IS_COLD_REBOOT_MODE, false);
                }
                break;
            case R.id.sa_sw_offline:
                if (isChecked) {
                    SharedPreferencesUtil.setBoolean(this, Constant.KEY_IS_OFFLINE_MODE, true);
                } else {
                    SharedPreferencesUtil.setBoolean(this, Constant.KEY_IS_OFFLINE_MODE, false);
                }
                break;
            case R.id.sa_sw_old_rec:
                if (isChecked) {
                    SharedPreferencesUtil.setBoolean(this, Constant.KEY_IS_OLD_REC_MODE, true);
                } else {
                    SharedPreferencesUtil.setBoolean(this, Constant.KEY_IS_OLD_REC_MODE, false);
                }
                break;
            default:
                break;
        }
    }
}
