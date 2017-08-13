package com.hb.rssai.view.me;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.presenter.AdvicePresenter;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.view.iView.IAdviceView;

import butterknife.BindView;
import butterknife.OnClick;

public class AdviceActivity extends BaseActivity implements IAdviceView, View.OnClickListener {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.advice_et_content)
    EditText mAdviceEtContent;
    @BindView(R.id.advice_btn_save)
    Button mAdviceBtnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_advice;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_advice_title));
    }

    @Override
    protected BasePresenter createPresenter() {
        return new AdvicePresenter(this, this);
    }

    @Override
    public EditText getEtContent() {
        return mAdviceEtContent;
    }

    @Override
    public void toFinish() {
        finish();
    }

    @OnClick({R.id.advice_btn_save})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.advice_btn_save:
                ((AdvicePresenter) mPresenter).add();
                break;
        }
    }
}
