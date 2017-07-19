package com.hb.rssai.view.subscription;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.util.QRCodeUtils;
import com.hb.rssai.util.T;

import butterknife.BindView;

public class QrCodeActivity extends BaseActivity {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.qa_iv)
    ImageView mQaIv;
    @BindView(R.id.activity_add_source)
    LinearLayout mActivityAddSource;

    public final static String KEY_CONTENT = null;
    private String content = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initIntent() {
        super.initIntent();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            content = bundle.getString(KEY_CONTENT);
        }
    }

    @Override
    protected void initView() {
        if (content != null) {
            try {
                T.ShowToast(this,content);
                mQaIv.setImageBitmap(QRCodeUtils.createCode(this, content));
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_qrcode;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_qa_title));
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
