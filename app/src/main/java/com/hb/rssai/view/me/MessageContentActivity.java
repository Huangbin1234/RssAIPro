package com.hb.rssai.view.me;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResMessageList;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.util.HttpLoadImg;

import butterknife.BindView;

public class MessageContentActivity extends BaseActivity {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.mc_tv_title)
    TextView mMcTvTitle;
    @BindView(R.id.mc_iv_img)
    ImageView mMcIvImg;
    @BindView(R.id.mc_tv_pubTime)
    TextView mMcTvPubTime;
    @BindView(R.id.mc_tv_content)
    TextView mMcTvContent;
    @BindView(R.id.mc_tv_url)
    TextView mMcTvUrl;

    public static final String KEY_MSG_BEAN = "msgBean";
    @BindView(R.id.mc_ll_no_data)
    LinearLayout mMcLlNoData;

    private ResMessageList.RetObjBean.RowsBean message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            message = (ResMessageList.RetObjBean.RowsBean) bundle.getSerializable(KEY_MSG_BEAN);
        }
    }

    @Override
    protected void initView() {
        if (message != null) {
            mMcLlNoData.setVisibility(View.GONE);
            mMcTvTitle.setText(message.getTitle());
            if (!TextUtils.isEmpty(message.getImg())) {
                HttpLoadImg.loadImg(this, message.getImg(), mMcIvImg);
            }
            mMcTvPubTime.setText("发布时间："+message.getPubTime());
            mMcTvContent.setText(message.getContent());
            mMcTvUrl.setText(message.getUrl());
        } else {
            mMcLlNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_message_content;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_message_content_title));
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
