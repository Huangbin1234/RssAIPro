package com.hb.rssai.view.subscription;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.T;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class AddSourceActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.asa_et_name)
    EditText mAsaEtName;
    @BindView(R.id.asa_et_url)
    EditText mAsaEtUrl;
    @BindView(R.id.asa_btn_save)
    Button mAsaBtnSave;
    @BindView(R.id.activity_add_source)
    LinearLayout mActivityAddSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_add_source;
    }

    @Override
    protected void setAppTitle() {
        mSysToolbar.setTitle("");
        setSupportActionBar(mSysToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//设置ActionBar一个返回箭头，主界面没有，次级界面有
        mSysTvTitle.setText(getResources().getString(R.string.str_asa_title));
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

    @OnClick({R.id.asa_btn_save})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.asa_btn_save:
                //TODO 写入首选
                String name = mAsaEtName.getText().toString().trim();
                String link = mAsaEtUrl.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    T.ShowToast(this, "请输入名称");
                    return;
                }
                if (TextUtils.isEmpty(link)) {
                    T.ShowToast(this, "请输入链接地址");
                    return;
                }
                RssSource rssSource = new RssSource();
                rssSource.setName(name);
                rssSource.setLink(link);
                LiteOrmDBUtil.insert(rssSource);
                T.ShowToast(this, "添加成功");
                EventBus.getDefault().post(new RssSourceEvent(0));
                finish();
                break;
        }
    }
}
