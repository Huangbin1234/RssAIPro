package com.hb.rssai.view.common;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.RegisterPresenter;
import com.hb.rssai.view.iView.IRegisterView;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, IRegisterView {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

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
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back_del);
        }
        mSysTvTitle.setText(getResources().getString(R.string.str_ra_title));
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

    @Override
    protected BasePresenter createPresenter() {
        return new RegisterPresenter(this, this);
    }

    @OnClick({R.id.ra_btn_register, R.id.ra_chktv_spsd_control})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ra_btn_register:
                ((RegisterPresenter) mPresenter).register();
                break;
            case R.id.ra_chktv_spsd_control:
                if (mRaChktvSpsdControl.isChecked()) {
                    mRaChktvSpsdControl.setChecked(false);
                    //否则隐藏密码
                    mRaEtSpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mRaEtSpassword.setCompoundDrawables(null, null, getResources().getDrawable(R.mipmap.icon_psd_gone), null);
                } else {
                    mRaChktvSpsdControl.setChecked(true);
                    //如果选中，显示密码
                    mRaEtSpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mRaEtSpassword.setCompoundDrawables(null, null, getResources().getDrawable(R.mipmap.icon_psd_view), null);
                }
                break;
        }
    }

    @Override
    public EditText getEtPsd() {
        return mRaEtPassword;
    }

    @Override
    public EditText getEtSpsd() {
        return mRaEtSpassword;
    }

    @Override
    public EditText getEtName() {
        return mRaEtUserName;
    }

    @Override
    public void toFinish() {
        finish();
    }
}
