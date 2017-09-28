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
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.EditSignaturePresenter;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IEditSignatureView;

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
            String str = mEsaEtSignature.getText().toString().trim();
            if (!"".equals(str)) {
                if (str.equals(signatureStr)) {
                    T.ShowToast(EditSignatureActivity.this, "内容没有任何变化，请修改后保存。");
                } else {
                    ((EditSignaturePresenter) mPresenter).updateUserInfo();
                }
            } else {
                T.ShowToast(EditSignatureActivity.this, "请输入签名后，再进行保存。");
            }
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
        return new EditSignaturePresenter(this, this);
    }

    @Override
    public EditText getEtContent() {
        return mEsaEtSignature;
    }

    @Override
    public void toFinish() {
        finish();
    }
}
