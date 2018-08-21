package com.hb.rssai.view.common;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
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
        T.ShowToast(this,error);
    }

}
