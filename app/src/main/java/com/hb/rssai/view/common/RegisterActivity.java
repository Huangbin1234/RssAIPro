package com.hb.rssai.view.common;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.contract.RegisterContract;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.RegisterPresenter;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, RegisterContract.View {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.ra_et_userName)
    EditText mRaEtUserName;
    @BindView(R.id.ra_et_password)
    EditText mRaEtPassword;
    @BindView(R.id.ra_et_spassword)
    EditText mRaEtSpassword;
    @BindView(R.id.ra_chktv_spsd_control)
    CheckedTextView mRaChktvSpsdControl;
    @BindView(R.id.ra_btn_register)
    Button mRaBtnRegister;

    RegisterContract.Presenter mPresenter;
    @BindView(R.id.ra_tv_userName_label)
    TextView mRaTvUserNameLabel;
    @BindView(R.id.ra_tv_password_label)
    TextView mRaTvPasswordLabel;
    @BindView(R.id.ra_tv_spassword_label)
    TextView mRaTvSPasswordLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            mRaTvUserNameLabel.setVisibility(View.VISIBLE);
            mRaTvPasswordLabel.setVisibility(View.VISIBLE);
            mRaTvSPasswordLabel.setVisibility(View.VISIBLE);
        } else {
            mRaTvUserNameLabel.setVisibility(View.GONE);
            mRaTvPasswordLabel.setVisibility(View.GONE);
            mRaTvSPasswordLabel.setVisibility(View.GONE);

            //setCompoundDrawables设置前需要先setBounds

            Drawable draName = getResources().getDrawable(R.drawable.selector_ic_person);
            draName.setBounds(0, 0, draName.getMinimumWidth(), draName.getMinimumHeight());
            mRaEtUserName.setCompoundDrawables(draName, null, null, null);

            Drawable draPsd = getResources().getDrawable(R.drawable.selector_ic_lock);
            draPsd.setBounds(0, 0, draPsd.getMinimumWidth(), draPsd.getMinimumHeight());
            mRaEtPassword.setCompoundDrawables(draPsd, null, null, null);

            Drawable draSPsd = getResources().getDrawable(R.drawable.selector_ic_lock);
            draSPsd.setBounds(0, 0, draSPsd.getMinimumWidth(), draSPsd.getMinimumHeight());
            mRaEtSpassword.setCompoundDrawables(draSPsd, null, null, null);
        }
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_register;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_ra_title));
    }

    @Override
    protected BasePresenter createPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @OnClick({R.id.ra_btn_register, R.id.ra_chktv_spsd_control})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ra_btn_register:
                String name = mRaEtUserName.getText().toString().trim();
                String psd = mRaEtPassword.getText().toString().trim();
                String sPsd = mRaEtSpassword.getText().toString().trim();

                mPresenter.register(name, psd, sPsd);
                break;
            case R.id.ra_chktv_spsd_control:
                if (mRaChktvSpsdControl.isChecked()) {
                    mRaChktvSpsdControl.setChecked(false);
                    //否则隐藏密码
                    mRaEtSpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    mRaEtSpassword.setCompoundDrawables(null, null, getResources().getDrawable(R.mipmap.icon_psd_gone), null);
                } else {
                    mRaChktvSpsdControl.setChecked(true);
                    //如果选中，显示密码
                    mRaEtSpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    mRaEtSpassword.setCompoundDrawables(null, null, getResources().getDrawable(R.mipmap.icon_psd_view), null);
                }
                break;
        }
    }

    @Override
    public void showCheckError(String error) {
        T.ShowToast(this, error);
    }

    @Override
    public void showRegisterSuccess(ResBase resBase) {
        if (resBase.getRetCode() == 0) {
            String name = mRaEtUserName.getText().toString().trim();
            String psd = mRaEtSpassword.getText().toString().trim();
            SharedPreferencesUtil.setString(this, Constant.SP_LOGIN_USER_NAME, name);
            SharedPreferencesUtil.setString(this, Constant.SP_LOGIN_PSD, psd);

            finish();
        }
        T.ShowToast(this, resBase.getRetMsg());
    }

    @Override
    public void showRegisterFailed(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
    }

}
