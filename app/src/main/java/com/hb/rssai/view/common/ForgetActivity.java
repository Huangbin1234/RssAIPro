package com.hb.rssai.view.common;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.contract.ForgetContract;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.ForgetPresenter;
import com.hb.rssai.util.T;

import butterknife.BindView;

import static com.google.common.base.Preconditions.checkNotNull;

public class ForgetActivity extends BaseActivity implements ForgetContract.View {


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
    @BindView(R.id.fa_v_mobile)
    View vMobile;
    @BindView(R.id.fa_v_email)
    View vEmail;
    @BindView(R.id.fa_v_email_end)
    View vMailEnd;
    @BindView(R.id.fa_v_mobile_end)
    View vMobileEnd;

    private String type = "email";

    ForgetContract.Presenter mPresenter;

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
                    mFaEtUserName.clearFocus();
                    mFaEtUserName.setFocusable(false);

                    mFaEtEmail.setFocusable(true);
                    mFaEtEmail.setFocusableInTouchMode(true);
                    mFaEtEmail.requestFocus();

                    mFaEtUserName.setFocusable(true);
                    mFaEtUserName.setFocusableInTouchMode(true);

                    mFaEtEmail.setVisibility(View.VISIBLE);
                    mFaEtUserName.setVisibility(View.VISIBLE);
                    mFaEtUserMobile.setVisibility(View.GONE);
                    vMobile.setVisibility(View.GONE);
                    vEmail.setVisibility(View.VISIBLE);
                    vMobileEnd.setVisibility(View.GONE);
                    vMailEnd.setVisibility(View.VISIBLE);
                    break;
                case R.id.fa_rb_representation:
                    type = Constant.TYPE_REPRESENTATION;
                    mFaEtEmail.clearFocus();
                    mFaEtEmail.setFocusable(false);

                    mFaEtUserName.setFocusable(true);
                    mFaEtUserName.setFocusableInTouchMode(true);
                    mFaEtUserName.requestFocus();

                    mFaEtEmail.setFocusable(true);
                    mFaEtEmail.setFocusableInTouchMode(true);

                    mFaEtEmail.setVisibility(View.GONE);
                    mFaEtUserName.setVisibility(View.VISIBLE);
                    mFaEtUserMobile.setVisibility(View.VISIBLE);
                    vMobile.setVisibility(View.VISIBLE);
                    vEmail.setVisibility(View.GONE);
                    vMobileEnd.setVisibility(View.VISIBLE);
                    vMailEnd.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        });
        mFaBtnSure.setOnClickListener(v -> {
            String email = mFaEtEmail.getText().toString().trim();
            String userName = mFaEtUserName.getText().toString().trim();
            String mobile = mFaEtUserMobile.getText().toString().trim();
            mPresenter.retrievePassword(type, email, userName, mobile);
        });
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
    protected BasePresenter createPresenter() {
        return new ForgetPresenter(this);
    }

    @Override
    public void showRetrieveSuccess(ResBase resBase) {
        T.ShowToast(this, resBase.getRetMsg());
        if (resBase.getRetCode() == 0) {
            finish();
        }
    }

    @Override
    public void showRetrieveFailed(Throwable throwable) {
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
        throwable.printStackTrace();
    }

    @Override
    public void showCheckError(String error) {
        T.ShowToast(this, error);
    }

    @Override
    public void setPresenter(ForgetContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
