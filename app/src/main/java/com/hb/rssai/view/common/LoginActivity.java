package com.hb.rssai.view.common;

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
import com.hb.rssai.constants.Constant;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.LoginPresenter;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.view.iView.ILoginView;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements ILoginView, View.OnClickListener {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.la_et_userName)
    EditText mLaEtUserName;
    @BindView(R.id.la_et_password)
    EditText mLaEtPassword;
    @BindView(R.id.la_btn_login)
    Button mLaBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        //初始化
        mLaEtUserName.setText(SharedPreferencesUtil.getString(this, Constant.SP_LOGIN_USER_NAME, ""));
        mLaEtPassword.setText(SharedPreferencesUtil.getString(this, Constant.SP_LOGIN_PSD, ""));
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_login;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_la_title));
    }

    @Override
    protected BasePresenter createPresenter() {
        return new LoginPresenter(this, this);
    }

    @Override
    public void toFinish() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public EditText getEtUserName() {
        return mLaEtUserName;
    }

    @Override
    public EditText getEtPassword() {
        return mLaEtPassword;
    }

    @OnClick({R.id.la_btn_login})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.la_btn_login:
                ((LoginPresenter) mPresenter).login();
                break;
        }
    }
}
