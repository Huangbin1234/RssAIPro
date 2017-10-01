package com.hb.rssai.view.subscription;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.event.RssSourceEvent;
import com.hb.rssai.presenter.AddRssPresenter;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IAddRssView;
import com.rometools.opml.feed.opml.Outline;
import com.rometools.rome.io.XmlReader;
import com.rss.util.ReadXML;
import com.zbar.lib.CaptureActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddSourceActivity extends BaseActivity implements View.OnClickListener, IAddRssView {

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
    @BindView(R.id.asa_iv_scan)
    ImageView mAsaIvScan;

    public final static int REQUESTCODE = 1;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.asa_btn_opml)
    Button mAsaBtnOpml;
    @BindView(R.id.asa_tv_opml)
    TextView mAsaTvOpml;
    OpmlTask opmlTask;
    @BindView(R.id.asa_et_key)
    EditText mAsaEtKey;
    @BindView(R.id.asa_btn_key)
    Button mAsaBtnKey;

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
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//设置ActionBar一个返回箭头，主界面没有，次级界面有
            actionBar.setDisplayShowTitleEnabled(false);
        }
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
        return new AddRssPresenter(this, this);
    }

    String rssTitle = "";
    String rssLink = "";

    @OnClick({R.id.asa_btn_save, R.id.asa_iv_scan, R.id.asa_btn_opml, R.id.asa_btn_key})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.asa_btn_save:
                //TODO 写入首选
                rssTitle = mAsaEtName.getText().toString().trim();
                rssLink = mAsaEtUrl.getText().toString().trim();
                if (TextUtils.isEmpty(rssTitle)) {
                    T.ShowToast(this, "请输入名称");
                    return;
                }
                if (TextUtils.isEmpty(rssLink)) {
                    T.ShowToast(this, "请输入链接地址");
                    return;
                }
                RssSource rssSource = new RssSource();
                rssSource.setName(rssTitle);
                rssSource.setLink(rssLink);
                LiteOrmDBUtil.insert(rssSource);
                T.ShowToast(this, "添加成功");
                EventBus.getDefault().post(new RssSourceEvent(0));
                ((AddRssPresenter) mPresenter).addRss();
                break;
            case R.id.asa_iv_scan:
                startActivityForResult(new Intent(this, CaptureActivity.class), REQUESTCODE);
                break;
            case R.id.asa_btn_opml:
                String link2 = mAsaEtUrl.getText().toString().trim();
                opmlTask = new OpmlTask();
                opmlTask.execute(link2);
                break;
            case R.id.asa_btn_key:
                rssTitle = mAsaEtKey.getText().toString().trim();
                if (TextUtils.isEmpty(rssTitle)) {
                    T.ShowToast(this, "请输入关键字");
                    return;
                }
                RssSource keySource = new RssSource();
                keySource.setName(rssTitle);
                try {
                    rssLink = "http://news.baidu.com/ns?word=" + URLEncoder.encode(rssTitle, "UTF-8") + "&tn=newsrss&sr=0&cl=2&rn=20&ct=0";
                    keySource.setLink(rssLink);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                LiteOrmDBUtil.insert(keySource);

                ((AddRssPresenter) mPresenter).addRss();
                break;
        }
    }



    @Override
    public String getRssLink() {
        return rssLink;
    }

    @Override
    public String getRssTitle() {
        return rssTitle;
    }


    class OpmlTask extends AsyncTask<String, Void, List<Outline>> {

        @Override
        protected List<Outline> doInBackground(String... params) {
            return readOPML(params[0]);
        }

        @Override
        protected void onPostExecute(List<Outline> outlines) {
            if (outlines != null && outlines.size() > 0) {
                RssSource rssSource;
                for (Outline outline : outlines) {
                    if (!TextUtils.isEmpty(outline.getXmlUrl())) {
                        rssSource = new RssSource();
                        try {
                            String strUTF = new String(outline.getTitle().getBytes(), "UTF-8");
                            rssSource.setName(strUTF);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        rssSource.setLink(outline.getXmlUrl());
                        LiteOrmDBUtil.insert(rssSource);
                    }
                    for (Outline subOutline : outline.getChildren()) {
                        if (!TextUtils.isEmpty(subOutline.getXmlUrl())) {
                            rssSource = new RssSource();
                            try {
                                String strUTF = new String(subOutline.getTitle().getBytes(), "UTF-8");
                                rssSource.setName(strUTF);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            rssSource.setLink(subOutline.getXmlUrl());
                            LiteOrmDBUtil.insert(rssSource);
                        }
                    }
                }
                T.ShowToast(AddSourceActivity.this, "添加成功");
                EventBus.getDefault().post(new RssSourceEvent(0));
                finish();
            }
        }
    }

    private List<Outline> readOPML(String opmlUrl) {
        opmlUrl = "http://www.williamlong.info/download/opml.xml";
        URL feedUrl = null;//SyndFeedInput:从远程读到xml结构的内容转成SyndFeedImpl实例
        ReadXML readXML = ReadXML.getInstance();
        try {
            feedUrl = new URL(opmlUrl);
            XmlReader xmlReader = null;
            try {
                xmlReader.setDefaultEncoding("UTF-8");
                xmlReader = new XmlReader(feedUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return readXML.readRss(xmlReader);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK == resultCode) {
            if (requestCode == REQUESTCODE) {
                mAsaEtUrl.setText(data.getStringExtra("info"));
            }
        }
    }
}
