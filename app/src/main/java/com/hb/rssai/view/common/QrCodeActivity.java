package com.hb.rssai.view.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
    //新版本收藏和订阅
    public static final String KEY_INFO_ID = "key_info_id";
    public static final String KEY_SUBSCRIBE_ID = "key_subscribe_id";

    public final static String KEY_CONTENT = "key_content";
    public final static String KEY_FROM = "key_from";
    public final static String KEY_TITLE = "key_title";
    public final static String[] FROM_VALUES = {"sub", "content", "coll"};
    public final static String[] FROM_TEXT_VALUES = {"是否订阅", "是否收藏", ""};
    @BindView(R.id.qa_btn)
    Button mQaBtn;
    @BindView(R.id.qa_tv_title)
    TextView mQaTvTitle;
    private String content = null;
    private String from = null;
    private String title = null;
    private String infoId = null;
    private String subscribeId = null;

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
            title = bundle.getString(KEY_TITLE);
            infoId = bundle.getString(KEY_INFO_ID);
            subscribeId = bundle.getString(KEY_SUBSCRIBE_ID);
        }
    }

    @Override
    protected void initView() {
        mQaTvTitle.setText(title);
        if (content != null) {
            try {
                Bitmap logo = BitmapFactory.decodeResource(super.getResources(), R.mipmap.ic_launcher);
                mQaIv.setImageBitmap(QRCodeUtil.createCode(this, content, logo));
                logo.recycle();
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
        return null;
    }

    @OnClick({R.id.qa_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qa_btn:
                Bitmap logo = BitmapFactory.decodeResource(super.getResources(), R.mipmap.ic_launcher);
                if (FROM_VALUES[0].equals(from)) {//从订阅点过来的
//                    String s = Base64Util.getEncodeStr(Constant.FLAG_PRESS_RSS_SOURCE + Base64Util.getDecodeStr(content));
                    String encodeSubscribeId = Base64Util.getEncodeStr(Constant.FLAG_PRESS_RSS_SOURCE + Base64Util.getDecodeStr(subscribeId));
                    try {
//                        mQaIv.setImageBitmap(QRCodeUtil.createCode(this, s, logo));
                        mQaIv.setImageBitmap(QRCodeUtil.createCode(this, encodeSubscribeId, logo));
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } else if (FROM_VALUES[1].equals(from)) {//从收藏点过来的

                    String encodeInfoId = Base64Util.getEncodeStr(Constant.FLAG_PRESS_COLLECTION_SOURCE + infoId);
                    //old
                    String s = Base64Util.getEncodeStr(Constant.FLAG_PRESS_COLLECTION_SOURCE + Base64Util.getDecodeStr(content));
                    try {
                        // mQaIv.setImageBitmap(QRCodeUtil.createCode(this, s, logo));
                        mQaIv.setImageBitmap(QRCodeUtil.createCode(this, encodeInfoId, logo));
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } else if (FROM_VALUES[2].equals(from)) {
                    String s = Base64Util.getEncodeStr(Constant.FLAG_PRESS_URL_SOURCE + Base64Util.getDecodeStr(content));
                    try {
                        mQaIv.setImageBitmap(QRCodeUtil.createCode(this, s, logo));
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
                logo.recycle();
                break;
        }
    }
}
