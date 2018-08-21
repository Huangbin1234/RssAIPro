package com.hb.rssai.view.me;

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
import com.hb.rssai.bean.ResUser;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.MineEvent;
import com.hb.rssai.event.UserEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.EditSignaturePresenter;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IEditSignatureView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class EditSignatureActivity extends BaseActivity implements IEditSignatureView {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.esa_et_signature)
    EditText mEsaEtSignature;
    @BindView(R.id.esa_btn_save)
    Button mEsaBtnSave;

    private String signatureStr = "";
    public static final String KEY_SIGNATURE = "key_signature";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            signatureStr = bundle.getString(KEY_SIGNATURE);
        }
    }

    @Override
    protected void initView() {
        mEsaEtSignature.setText(signatureStr);
        mEsaBtnSave.setOnClickListener(v -> {
            ((EditSignaturePresenter) mPresenter).updateUserInfo();
        });
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_edit_signature;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_signature_title));
    }

    @Override
    protected BasePresenter createPresenter() {
        return new EditSignaturePresenter(this);
    }

    @Override
    public void setUpdateResult(ResUser resBase) {
        if (resBase.getRetCode() == 0) {
            EventBus.getDefault().post(new MineEvent(0));
            EventBus.getDefault().post(new UserEvent(0));
            finish();
        }
        T.ShowToast(this, resBase.getRetMsg());
    }


    @Override
    public void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
    }

    @Override
    public String getNewSignature() {
        return mEsaEtSignature.getText().toString().trim();
    }

    @Override
    public String getOldSignature() {
        return signatureStr;
    }

    @Override
    public void setCheckResult(String error) {
        T.ShowToast(this, error);
    }
}
