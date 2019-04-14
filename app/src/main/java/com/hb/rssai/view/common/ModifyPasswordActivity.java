package com.hb.rssai.view.common;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
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
import com.hb.rssai.presenter.ModifyPasswordPresenter;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IModifyPasswordView;

import butterknife.BindView;

public class ModifyPasswordActivity extends BaseActivity implements IModifyPasswordView {
    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.mpa_et_old_psd)
    EditText mMpaEtOldPsd;
    @BindView(R.id.mpa_et_new_psd)
    EditText mMpaEtNewPsd;
    @BindView(R.id.mpa_et_new_spsd)
    EditText mMpaEtNewSpsd;
    @BindView(R.id.mpa_btn_sure)
    Button mMpaBtnSure;
    @BindView(R.id.mpa_tv_old_psd_label)
    TextView mMpaTvOldPsdLabel;
    @BindView(R.id.la_chktv_old_psd_control)
    CheckedTextView mLaChktvOldPsdControl;
    @BindView(R.id.mpa_tv_new_psd_label)
    TextView mMpaTvNewPsdLabel;
    @BindView(R.id.la_chktv_new_psd_control)
    CheckedTextView mLaChktvNewPsdControl;
    @BindView(R.id.mpa_tv_new_spsd_label)
    TextView mMpaTvNewSpsdLabel;
    @BindView(R.id.la_chktv_new_spsd_control)
    CheckedTextView mLaChktvNewSpsdControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            mMpaTvOldPsdLabel.setVisibility(View.VISIBLE);
            mMpaTvNewPsdLabel.setVisibility(View.VISIBLE);
            mMpaTvNewSpsdLabel.setVisibility(View.VISIBLE);
        } else {
            mMpaTvOldPsdLabel.setVisibility(View.GONE);
            mMpaTvNewPsdLabel.setVisibility(View.GONE);
            mMpaTvNewSpsdLabel.setVisibility(View.GONE);

            Drawable draOldPsd = getResources().getDrawable(R.drawable.selector_ic_lock);
            draOldPsd.setBounds(0, 0, draOldPsd.getMinimumWidth(), draOldPsd.getMinimumHeight());
            mMpaEtOldPsd.setCompoundDrawables(draOldPsd, null, null, null);
            //setCompoundDrawables设置前需要先setBounds
            Drawable draNewPsd = getResources().getDrawable(R.drawable.selector_ic_lock);
            draNewPsd.setBounds(0, 0, draNewPsd.getMinimumWidth(), draNewPsd.getMinimumHeight());
            mMpaEtNewPsd.setCompoundDrawables(draNewPsd, null, null, null);

            Drawable draNewSPsd = getResources().getDrawable(R.drawable.selector_ic_lock);
            draNewSPsd.setBounds(0, 0, draNewSPsd.getMinimumWidth(), draNewSPsd.getMinimumHeight());
            mMpaEtNewSpsd.setCompoundDrawables(draNewSPsd, null, null, null);
        }

        mMpaBtnSure.setOnClickListener(v -> {
            ((ModifyPasswordPresenter) mPresenter).modify();
        });
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_modify_password;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_mpa_title));
    }

    @Override
    protected BasePresenter createPresenter() {
        return new ModifyPasswordPresenter(this);
    }


    @Override
    public void showModifyResult(ResBase resBase) {
        T.ShowToast(this, resBase.getRetMsg());
        if (resBase.getRetCode() == 0) {
            finish();
        }
    }

    @Override
    public void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
    }

    @Override
    public String getOldPsd() {
        return mMpaEtOldPsd.getText().toString().trim();
    }

    @Override
    public String getNewPsd() {
        return mMpaEtNewPsd.getText().toString().trim();
    }

    @Override
    public String getNewSPsd() {
        return mMpaEtNewSpsd.getText().toString().trim();
    }

    @Override
    public void setCheckError(String error) {
        T.ShowToast(this, error);
    }

}
