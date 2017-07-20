package com.hb.rssai.view.subscription;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.util.Base64Util;
import com.hb.rssai.util.QRCodeUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class QrCodeActivity extends BaseActivity implements View.OnClickListener {

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

    public final static String KEY_CONTENT = "key_content";
    public final static String KEY_FROM =  "key_from";
    public final static String[] FROM_VALUES = {"sub", "content", "coll"};
    public final static String[] FROM_TEXT_VALUES = {"是否订阅", "是否收藏", ""};
    @BindView(R.id.qa_btn)
    Button mQaBtn;
    private String content = null;
    private String from = null;

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
            from = bundle.getString(KEY_FROM);
        }
    }

    @Override
    protected void initView() {
        if (content != null) {
            try {
                mQaIv.setImageBitmap(QRCodeUtil.createCode(this, content));
                if (FROM_VALUES[0].equals(from)) {
                    mQaBtn.setText(FROM_TEXT_VALUES[0]);
                } else if (FROM_VALUES[1].equals(from)) {
                    mQaBtn.setText(FROM_TEXT_VALUES[1]);
                } else if (FROM_VALUES[2].equals(from)) {
                    mQaBtn.setVisibility(View.GONE);
                }
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

    @OnClick({R.id.qa_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qa_btn:
                if (FROM_VALUES[0].equals(from)) {
                    String s = Base64Util.getEncodeStr(Constant.FLAG_PRESS_RSS_SOURCE + Base64Util.getDecodeStr(content));
                    try {
                        mQaIv.setImageBitmap(QRCodeUtil.createCode(this, s));
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } else if (FROM_VALUES[1].equals(from)) {
                    String s = Base64Util.getEncodeStr(Constant.FLAG_PRESS_COLLECTION_SOURCE + Base64Util.getDecodeStr(content));
                    try {
                        mQaIv.setImageBitmap(QRCodeUtil.createCode(this, s));
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } else if (FROM_VALUES[2].equals(from)) {
                    String s = Base64Util.getEncodeStr(Constant.FLAG_PRESS_URL_SOURCE + Base64Util.getDecodeStr(content));
                    try {
                        mQaIv.setImageBitmap(QRCodeUtil.createCode(this, s));
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
