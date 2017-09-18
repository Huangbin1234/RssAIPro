package com.hb.rssai.view.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.UserCollection;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.RichTextPresenter;
import com.hb.rssai.util.Base64Util;
import com.hb.rssai.util.DateUtil;
import com.hb.rssai.util.HtmlImageGetter;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.StatusBarUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IRichTextView;
import com.zzhoujay.richtext.RichText;

import java.text.ParseException;
import java.util.Date;

import butterknife.BindView;

public class RichTextActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, IRichTextView {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.ca_load_progress)
    ProgressBar mCaLoadProgress;
    @BindView(R.id.rta_tv_content)
    TextView mRtaTvContent;
    @BindView(R.id.activity_add_source)
    LinearLayout mActivityAddSource;
    @BindView(R.id.rta_tv_title)
    TextView mRtaTvTitle;
    @BindView(R.id.rta_tv_date)
    TextView mRtaTvDate;
    @BindView(R.id.rta_tv_whereFrom)
    TextView mRtaTvWhereFrom;
    @BindView(R.id.rta_tv_view)
    TextView mRtaTvView;
    @BindView(R.id.rta_recycler_view)
    RecyclerView mRtaRecyclerView;

    private LinearLayoutManager linearLayoutManager;

    private String abstractContent = "";
    private String pubDate = "";
    private String title = "";
    private String whereFrom = "";
    private String url = "";
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RichTextPresenter) mPresenter).getLikeByTitle();
        ((RichTextPresenter) mPresenter).updateCount();
    }

    @Override
    protected void initIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            abstractContent = bundle.getString("abstractContent");
            title = bundle.getString(ContentActivity.KEY_TITLE);
            whereFrom = bundle.getString("whereFrom");
            pubDate = bundle.getString("pubDate");
            url = bundle.getString("url");
            id = bundle.getString("id");
            //mSysTvTitle.setText(title);
        }
    }


    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(Constant.DATE_LONG_PATTERN);

    @Override
    protected void initView() {
        try {
            if (!TextUtils.isEmpty(pubDate))
                mRtaTvDate.setText(DateUtil.showDate(sdf.parse(pubDate), Constant.DATE_LONG_PATTERN));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mRtaTvTitle.setText(title.trim());
        mRtaTvWhereFrom.setText(whereFrom);

        HtmlImageGetter htmlImageGetter = new HtmlImageGetter(this, this, mRtaTvContent);
        Spanned spanned;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(abstractContent, Html.FROM_HTML_MODE_LEGACY, htmlImageGetter, null);
        } else {
            spanned = Html.fromHtml(abstractContent, htmlImageGetter, null); // or for older api
        }
        mRtaTvContent.setText(spanned);
        mRtaTvView.setOnClickListener(v -> {
            Intent intent = new Intent(RichTextActivity.this, ContentActivity.class);//创建Intent对象
            intent.putExtra(ContentActivity.KEY_TITLE, title);
            intent.putExtra(ContentActivity.KEY_URL, url);
            intent.putExtra(ContentActivity.KEY_INFORMATION_ID, id);
            startActivity(intent);
        });
        linearLayoutManager = new LinearLayoutManager(this);
        mRtaRecyclerView.setLayoutManager(linearLayoutManager);
        mRtaRecyclerView.setNestedScrollingEnabled(false);
        mRtaRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_rich_text;
    }

    @Override
    protected void setAppTitle() {
        mSysToolbar.setTitle("");
        setSupportActionBar(mSysToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);//设置ActionBar一个返回箭头，主界面没有，次级界面有
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mSysToolbar.setNavigationIcon(R.mipmap.ic_back);
        mSysToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSysToolbar.setOnMenuItemClickListener(this);
        //修改状态栏文字图标为深色
        StatusBarUtil.StatusBarLightMode(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_add_collection:
                if (!TextUtils.isEmpty(url)) {
                    String dateTime = DateUtil.format(new Date(), Constant.DATE_LONG_PATTERN);
                    UserCollection collection = new UserCollection();
                    collection.setLink(url);
                    collection.setTime(dateTime);
                    collection.setTitle(title);
                    LiteOrmDBUtil.insert(collection);
                    T.ShowToast(RichTextActivity.this, "收藏成功！");
                    ((RichTextPresenter) mPresenter).add();
                } else {
                    T.ShowToast(this, "收藏失败，链接错误！");
                }
                break;
            case R.id.toolbar_add_share:
                Intent intent = new Intent(this, QrCodeActivity.class);
                intent.putExtra(QrCodeActivity.KEY_FROM, QrCodeActivity.FROM_VALUES[2]);
                intent.putExtra(QrCodeActivity.KEY_TITLE, title);
                intent.putExtra(QrCodeActivity.KEY_CONTENT, Base64Util.getEncodeStr(Constant.FLAG_URL_SOURCE + url));
                startActivity(intent);
                break;
        }
        return false;
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
        return new RichTextPresenter(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RichText.recycle();
    }

    @Override
    public RecyclerView getRtaRecyclerView() {
        return mRtaRecyclerView;
    }

    @Override
    public String getNewTitle() {
        return title;
    }

    @Override
    public String getNewLink() {
        return url;
    }

    @Override
    public String getInformationId() {
        return id;
    }
}
