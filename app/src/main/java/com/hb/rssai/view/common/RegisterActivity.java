package com.hb.rssai.view.common;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.RegisterPresenter;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IRegisterView;

import java.util.HashMap;
import java.util.Map;

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
        return new RegisterPresenter(this);
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
    public Map<String, String> getParams() {
        String name = mRaEtUserName.getText().toString().trim();
        String psd = mRaEtPassword.getText().toString().trim();
        String spsd = mRaEtSpassword.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            T.ShowToast(this, "请输入账号");
            return null;
        }
        if (TextUtils.isEmpty(psd)) {
            T.ShowToast(this, "请输入密码");
            return null;
        }
        if (TextUtils.isEmpty(spsd)) {
            T.ShowToast(this, "请再次输入密码");
            return null;
        }

        if (!spsd.equals(psd)) {
            T.ShowToast(this, "两次输入的密码不一致");
            return null;
        }
        Map<String, String> params = new HashMap<>();
        String jsonParams = "{\"userName\":\"" + name + "\",\"password\":\"" + psd + "\",\"sPassword\":\"" + spsd + "\"}";
        params.put("jsonParams", jsonParams);
        return params;
    }

    @Override
    public void setRegResult(ResBase resBase) {
        if (resBase.getRetCode() == 0) {
            String name = mRaEtUserName.getText().toString().trim();
            String psd = mRaEtSpassword.getText().toString().trim();
            SharedPreferencesUtil.setString(this, Constant.SP_LOGIN_USER_NAME, name);
            SharedPreferencesUtil.setString(this, Constant.SP_LOGIN_PSD, psd);

            toFinish();
        }
        T.ShowToast(this, resBase.getRetMsg());
    }

    @Override
    public void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
    }

    @Override
    public void toFinish() {
        finish();
    }
}
