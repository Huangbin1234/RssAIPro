package com.hb.rssai.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


import com.hb.rssai.presenter.BasePresenter;

import butterknife.ButterKnife;

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {
    public T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        if(mPresenter!=null){
            mPresenter.attachView((V) this);
        }
        setContentView(providerContentViewId());
        ButterKnife.bind(this);
        setAppTitle();
        initIntent();
        initView();
    }

    //初始化控件信息
    protected abstract void initView();

    //设置布局
    protected abstract int providerContentViewId();

    //设置标题
    protected abstract void setAppTitle();

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

    protected abstract T createPresenter();

    //参数接收
    protected void initIntent() {

    }
}
