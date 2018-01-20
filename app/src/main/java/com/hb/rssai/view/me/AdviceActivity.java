package com.hb.rssai.view.me;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.presenter.AdvicePresenter;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IAdviceView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class AdviceActivity extends BaseActivity implements IAdviceView, View.OnClickListener {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.advice_et_content)
    EditText mAdviceEtContent;
    @BindView(R.id.advice_btn_save)
    Button mAdviceBtnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_advice;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_advice_title));
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
        return new AdvicePresenter(this);
    }

    @Override
    public void toFinish() {
        finish();
    }

    @Override
    public void setAddResult(ResBase resBase) {
        T.ShowToast(this, resBase.getRetMsg());
        toFinish();
    }

    @Override
    public void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
    }

    @Override
    public Map<String, String> getParams() {
        String content = mAdviceEtContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            T.ShowToast(this, "请输入意见内容");
            return null;
        }
        Map<String, String> params = new HashMap<>();
        String jsonParams = "{\"content\":\"" + content + "\"}";
        params.put("jsonParams", jsonParams);
        return params;
    }

    @OnClick({R.id.advice_btn_save})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.advice_btn_save:
                ((AdvicePresenter) mPresenter).add();
                break;
        }
    }
}
