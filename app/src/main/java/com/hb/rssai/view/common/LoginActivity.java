package com.hb.rssai.view.common;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResLogin;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.contract.LoginContract;
import com.hb.rssai.event.FindMoreEvent;
import com.hb.rssai.event.MineEvent;
import com.hb.rssai.event.RssSourceEvent;
import com.hb.rssai.event.UserEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.LoginPresenter;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoginActivity extends BaseActivity implements LoginContract.View, View.OnClickListener {

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
    @BindView(R.id.la_chktv_psd_control)
    CheckedTextView laChkTvPsdControl;
    @BindView(R.id.la_tv_register)
    TextView mLaTvRegister;
    @BindView(R.id.la_tv_forget)
    TextView mLaTvForget;

    public LoginContract.Presenter mPresenter;
    @BindView(R.id.la_tv_userName_label)
    TextView mLaTvUserNameLabel;
    @BindView(R.id.la_tv_password_label)
    TextView mLaTvPasswordLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        //初始化
        if (!TextUtils.isEmpty(SharedPreferencesUtil.getString(this, Constant.SP_LOGIN_USER_NAME, ""))) {
            mLaEtUserName.setText(SharedPreferencesUtil.getString(this, Constant.SP_LOGIN_USER_NAME, ""));
            mLaEtPassword.setText(SharedPreferencesUtil.getString(this, Constant.SP_LOGIN_PSD, ""));
        } else {
            //密码
            laChkTvPsdControl.setVisibility(View.VISIBLE);
            laChkTvPsdControl.setOnClickListener(this);
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            mLaTvUserNameLabel.setVisibility(View.VISIBLE);
            mLaTvPasswordLabel.setVisibility(View.VISIBLE);
        } else {
            mLaTvUserNameLabel.setVisibility(View.GONE);
            mLaTvPasswordLabel.setVisibility(View.GONE);
            Drawable draName = getResources().getDrawable(R.drawable.selector_ic_person);
            draName.setBounds(0, 0, draName.getMinimumWidth(), draName.getMinimumHeight());
            mLaEtUserName.setCompoundDrawables(draName, null, null, null);
            //setCompoundDrawables设置前需要先setBounds
            Drawable draPsd = getResources().getDrawable(R.drawable.selector_ic_lock);
            draPsd.setBounds(0, 0, draPsd.getMinimumWidth(), draPsd.getMinimumHeight());
            mLaEtPassword.setCompoundDrawables(draPsd, null, null, null);
        }
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
        return new LoginPresenter(this);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @OnClick({R.id.la_btn_login, R.id.la_tv_register, R.id.la_chktv_psd_control, R.id.la_tv_forget})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.la_btn_login:
                String name = mLaEtUserName.getText().toString().trim();
                String password = mLaEtPassword.getText().toString().trim();

                mPresenter.login(name, password);
                break;
            case R.id.la_tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.la_tv_forget:
                startActivity(new Intent(this, ForgetActivity.class));
                break;
            case R.id.la_chktv_psd_control:
                if (laChkTvPsdControl.isChecked()) {
                    laChkTvPsdControl.setChecked(false);
                    //否则隐藏密码
                    mLaEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    mLaEtPassword.setCompoundDrawables(getResources().getDrawable(R.drawable.selector_ic_lock), null, null, null);
                } else {
                    laChkTvPsdControl.setChecked(true);
                    //如果选中，显示密码
                    mLaEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    mLaEtPassword.setCompoundDrawables(getResources().getDrawable(R.drawable.selector_ic_lock), null, null, null);
                }
                break;
        }
    }

    @Override
    public void showLoginSuccess(ResLogin bean) {
        if (bean.getRetCode() == 0) {
            String uName = mLaEtUserName.getText().toString().trim();
            String uPsd = mLaEtPassword.getText().toString().trim();
            SharedPreferencesUtil.setString(this, Constant.SP_LOGIN_USER_NAME, uName);
            SharedPreferencesUtil.setString(this, Constant.SP_LOGIN_PSD, uPsd);
            SharedPreferencesUtil.setString(this, Constant.TOKEN, bean.getRetObj() != null ? bean.getRetObj().getToken() : "");
            SharedPreferencesUtil.setString(this, Constant.USER_ID, bean.getRetObj() != null ? bean.getRetObj().getUserId() : "");

            toFinish();

            //TODO 更新数据
            EventBus.getDefault().post(new RssSourceEvent(0));
            EventBus.getDefault().post(new FindMoreEvent(0));
            EventBus.getDefault().post(new UserEvent(0));
            EventBus.getDefault().post(new MineEvent(0));
        }
        T.ShowToast(this, bean.getRetMsg());
    }

    @Override
    public void showLoginFailed(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
    }

    @Override
    public void showCheckError(String error) {
        T.ShowToast(this, error);
    }

    public void toFinish() {
        setResult(RESULT_OK);
        finish();
    }
}
