package com.hb.rssai.view.common;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.ForgetPresenter;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IForgetView;

import butterknife.BindView;

public class ForgetActivity extends BaseActivity implements IForgetView {


    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.fa_et_email)
    EditText mFaEtEmail;
    @BindView(R.id.fa_et_user_name)
    EditText mFaEtUserName;
    @BindView(R.id.fa_et_user_mobile)
    EditText mFaEtUserMobile;
    @BindView(R.id.fa_btn_sure)
    Button mFaBtnSure;
    @BindView(R.id.fa_rb_email)
    AppCompatRadioButton mFaRbEmail;
    @BindView(R.id.fa_rb_representation)
    AppCompatRadioButton mFaRbRepresentation;
    @BindView(R.id.fa_rg)
    RadioGroup mFaRg;
    private String type = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mFaRg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.fa_rb_email:
                    type = Constant.TYPE_EMAIL;
                    mFaEtEmail.setVisibility(View.VISIBLE);
                    mFaEtUserName.setVisibility(View.VISIBLE);
                    mFaEtUserMobile.setVisibility(View.GONE);
                    break;
                case R.id.fa_rb_representation:
                    type = Constant.TYPE_REPRESENTATION;
                    mFaEtEmail.setVisibility(View.GONE);
                    mFaEtUserName.setVisibility(View.VISIBLE);
                    mFaEtUserMobile.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        });
        mFaBtnSure.setOnClickListener(v -> ((ForgetPresenter) mPresenter).findPsd());
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_forget;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_fa_title));
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
        return new ForgetPresenter(this);
    }


    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getEmail() {
        return mFaEtEmail.getText().toString().trim();
    }

    @Override
    public String getUserName() {
        return mFaEtUserName.getText().toString().trim();
    }

    @Override
    public String getMobile() {
        return mFaEtUserMobile.getText().toString().trim();
    }

    @Override
    public void showFindResult(ResBase resBase) {
        T.ShowToast(this, resBase.getRetMsg());
    }

    @Override
    public void loadError(Throwable throwable) {
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
        throwable.printStackTrace();
    }

    @Override
    public void setCheckError(String error) {
        T.ShowToast(this, error);
    }
}
