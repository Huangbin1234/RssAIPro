package com.hb.rssai.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.ThemeUtils;
import com.jaeger.library.StatusBarUtil;

import butterknife.ButterKnife;

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity implements SlidingPaneLayout.PanelSlideListener {
    public T mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        setTranslucentStatus2(this);
        setSelfTheme(this);
        setContentView(providerContentViewId());
        ButterKnife.bind(this);
        //initStatusBar();
        setAppTitle();
        initIntent();
        initView();
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    //设置布局
    protected abstract int providerContentViewId();

    //设置标题
    protected abstract void setAppTitle();

    //创建Presenter
    protected abstract T createPresenter();

    //参数接收
    protected void initIntent() {
    }

    //初始化控件信息
    protected abstract void initView();

    //下拉刷新
    protected void onRefresh() {
    }

    //上拉加载更多
    protected void loadMore() {
    }


    int theme = 0;

    public void setSelfTheme(Activity activity) {
        theme = SharedPreferencesUtil.getInt(this, Constant.KEY_THEME, 0);
        if (theme != 0) {
            setTheme(theme);
            setTranslucentStatus(activity);
        } else if (theme == R.style.Theme_default) {
            setTranslucentStatus2(activity);
        } else {
            setTranslucentStatus2(activity);
        }
    }

    //    当Activity 回调onRestart时（从上一个页面返回），检查当前主题是否已将被更改。
    @Override
    protected void onRestart() {
        super.onRestart();
        int currentTheme = SharedPreferencesUtil.getInt(this, Constant.KEY_THEME, 0);
        if (theme != currentTheme) {
            recreate();
        }
    }

    //初始化状态栏
    public void initStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
    }

    //设置沉浸式状态栏
    public void setTranslucentStatus(Activity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            theme = SharedPreferencesUtil.getInt(this, Constant.KEY_THEME, 0);
            if (theme != 0) {
                window.setStatusBarColor(ThemeUtils.getPrimaryDarkColor(activity));
            } else {
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    //设置沉浸式状态栏
    public void setTranslucentStatus2(Activity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    //初始化toolbar
    protected void initToolBar(Toolbar sysToolbar, TextView sysTvTitle, String title) {
        if (sysToolbar != null && sysTvTitle != null) {
            sysToolbar.setTitle("");
            setSupportActionBar(sysToolbar);
            sysTvTitle.setText(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            sysToolbar.setNavigationOnClickListener(view -> finish());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    /**
     * 获取用户ID
     */

    public String getUserID() {
        return SharedPreferencesUtil.getString(this, Constant.USER_ID, "");
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

    /**
     * 是否支持滑动返回
     *
     * @return
     */
    protected boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    public void onPanelClosed(View view) {

    }

    @Override
    public void onPanelOpened(View view) {
        finish();
        this.overridePendingTransition(0, R.anim.in_form_left);
    }

    @Override
    public void onPanelSlide(View view, float v) {
    }
}
