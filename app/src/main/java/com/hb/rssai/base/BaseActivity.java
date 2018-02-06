package com.hb.rssai.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hb.rssai.presenter.BasePresenter;
import com.jaeger.library.StatusBarUtil;

import butterknife.ButterKnife;

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {
    public T mPresenter;

    //初始化控件信息
    protected abstract void initView();

    //设置布局
    protected abstract int providerContentViewId();

    //设置标题
    protected abstract void setAppTitle();

    //创建Presenter
    protected abstract T createPresenter();

    //参数接收
    protected void initIntent() {
    }

    //下拉刷新
    protected void onRefresh() {
    }

    //上拉加载更多
    protected void loadMore() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);
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
            sysToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
