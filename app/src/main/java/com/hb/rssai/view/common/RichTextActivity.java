package com.hb.rssai.view.common;

import android.content.Intent;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.Evaluate;
import com.hb.rssai.bean.UserCollection;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.RichTextPresenter;
import com.hb.rssai.util.Base64Util;
import com.hb.rssai.util.DateUtil;
import com.hb.rssai.util.GsonUtil;
import com.hb.rssai.util.HtmlImageGetter;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.StatusBarUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IRichTextView;
import com.hb.rssai.view.widget.MyDecoration;
import com.zzhoujay.richtext.RichText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class RichTextActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, IRichTextView, View.OnClickListener {

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
    @BindView(R.id.rta_iv_good)
    ImageView mRtaIvGood;
    @BindView(R.id.rta_tv_good)
    TextView mRtaTvGood;
    @BindView(R.id.rta_ll_good)
    LinearLayout mRtaLlGood;
    @BindView(R.id.rta_iv_not_good)
    ImageView mRtaIvNotGood;
    @BindView(R.id.rta_tv_not_good)
    TextView mRtaTvNotGood;
    @BindView(R.id.rta_ll_not_good)
    LinearLayout mRtaLlNotGood;
    @BindView(R.id.ff_find_hot_label)
    TextView mFfFindHotLabel;

    private LinearLayoutManager linearLayoutManager;

    private String abstractContent = "";
    private String pubDate = "";
    private String title = "";
    private String whereFrom = "";
    private String url = "";
    private String id = "";
    private String evaluateType = "";
    private long clickGood;
    private long clickNotGood;
    private SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_LONG_PATTERN);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RichTextPresenter) mPresenter).getLikeByTitle();
        ((RichTextPresenter) mPresenter).updateCount();
        ((RichTextPresenter) mPresenter).getInformation();

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

            clickGood = bundle.getLong("clickGood");
            clickNotGood = bundle.getLong("clickNotGood");
            //mSysTvTitle.setText(title);
        }
    }


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

        mRtaTvNotGood.setText("" + clickNotGood);
        mRtaTvGood.setText("" + clickGood);

        HtmlImageGetter htmlImageGetter = new HtmlImageGetter(this, this, mRtaTvContent);
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
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
        mRtaRecyclerView.addItemDecoration(new MyDecoration(this, LinearLayoutManager.VERTICAL));

        //初始化
        String eStr = SharedPreferencesUtil.getString(this, id, "");
        if (!TextUtils.isEmpty(eStr)) {//如果不是空
            Evaluate eva = GsonUtil.getGsonUtil().getBean(eStr, Evaluate.class);
            if ("1".equals(eva.getClickGood())) {
                mRtaIvGood.setImageResource(R.mipmap.ic_good_press);
            } else if ("2".equals(eva.getClickGood())) {
                mRtaIvGood.setImageResource(R.mipmap.ic_good);
            }
            if ("1".equals(eva.getClickNotGood())) {
                mRtaIvNotGood.setImageResource(R.mipmap.ic_not_good_press);
            } else if ("2".equals(eva.getClickNotGood())) {
                mRtaIvNotGood.setImageResource(R.mipmap.ic_not_good);
            }
        }
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
        MenuItem item = menu.findItem(R.id.toolbar_add_collection);
        this.item = item;
        ((RichTextPresenter) mPresenter).getCollectionByInfoId();
        return super.onCreateOptionsMenu(menu);
    }

    private MenuItem item;

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_add_collection:
                this.item = item;
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

    @Override
    public String getEvaluateType() {
        return evaluateType;
    }

    @Override
    public TextView getTvNotGood() {
        return mRtaTvNotGood;
    }

    @Override
    public TextView getTvGood() {
        return mRtaTvGood;
    }

    @Override
    public ImageView getIvNotGood() {
        return mRtaIvNotGood;
    }

    @Override
    public ImageView getIvGood() {
        return mRtaIvGood;
    }

    @Override
    public LinearLayout getLlNotGood() {
        return mRtaLlNotGood;
    }

    @Override
    public LinearLayout getLlGood() {
        return mRtaLlGood;
    }

    @Override
    public MenuItem getItem() {
        return item;
    }


    @OnClick({R.id.rta_ll_good, R.id.rta_ll_not_good})
    @Override
    public void onClick(View v) {
        String eStr = SharedPreferencesUtil.getString(this, id, "");
        Evaluate eva = GsonUtil.getGsonUtil().getBean(eStr, Evaluate.class);
        switch (v.getId()) {
            case R.id.rta_ll_good:
                evaluateType = "1";
                if (null!=eva&&"1".equals(eva.getClickNotGood())) {
                    T.ShowToast(this, "您已踩过了，请先取消！");
                } else {
                    mRtaLlGood.setEnabled(false);
                    mRtaLlNotGood.setEnabled(false);
                    ((RichTextPresenter) mPresenter).updateEvaluateCount();
                }
                break;
            case R.id.rta_ll_not_good:
                evaluateType = "0";
                if (null!=eva&&"1".equals(eva.getClickGood())) {
                    T.ShowToast(this, "您已点过赞了，请先取消！");
                } else {
                    mRtaLlGood.setEnabled(false);
                    mRtaLlNotGood.setEnabled(false);
                    ((RichTextPresenter) mPresenter).updateEvaluateCount();
                }
                break;
        }
    }
}
