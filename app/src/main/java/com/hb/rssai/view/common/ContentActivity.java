package com.hb.rssai.view.common;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResShareCollection;
import com.hb.rssai.bean.UserCollection;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.contract.ContentContract;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.ContentPresenter;
import com.hb.rssai.util.Base64Util;
import com.hb.rssai.util.CommonHandler;
import com.hb.rssai.util.DateUtil;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.util.WifiUtil;

import java.util.Date;

import butterknife.BindView;

import static com.google.common.base.Preconditions.checkNotNull;

public class ContentActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, ContentContract.View {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.ca_load_progress)
    ProgressBar mCaLoadProgress;
    @BindView(R.id.ca_wv_content)
    WebView mCaWvContent;
    @BindView(R.id.activity_add_source)
    LinearLayout mActivityAddSource;

    public static final String KEY_URL = "url";
    public static final String KEY_TITLE = "title";
    public static final String KEY_INFORMATION_ID = "information_id";
    private String contentUrl;
    private String title = "";
    private String informationId = "";

    ContentContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.updateCount(informationId);
    }

    @Override
    protected void initIntent() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            contentUrl = bundle.getString(KEY_URL);
            title = bundle.getString(KEY_TITLE);
            informationId = bundle.getString(KEY_INFORMATION_ID);
        }
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_content;
    }

    @Override
    protected void setAppTitle() {
        mSysToolbar.setTitle("");
        setSupportActionBar(mSysToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//设置ActionBar一个返回箭头，主界面没有，次级界面有
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back_del);
        }
        mSysToolbar.setOnMenuItemClickListener(this);
    }

    @Override
    protected BasePresenter createPresenter() {
        return new ContentPresenter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void initView() {
        mSysTvTitle.setText(title);

        // 运行加载JS
        mCaWvContent.getSettings().setJavaScriptEnabled(true);
        // 采用缓存模式 需要对应清除缓存操作
        if (WifiUtil.isWIFIkAvailable(this)) {
            //当前有可用网络
            mCaWvContent.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式( 根据cache-control决定是否从网络上取数据。)
        } else {
            //当前没有可用网络
            mCaWvContent.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 缓存模式(只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。)
        }
        //msa_wv_content.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 自适应
        mCaWvContent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mCaWvContent.getSettings().setLoadWithOverviewMode(true);
        mCaWvContent.getSettings().setDomStorageEnabled(true);
        // 设置本地编码
        mCaWvContent.getSettings().setDefaultTextEncodingName("utf-8");
        mCaWvContent.loadUrl(contentUrl);
        // 此处能拦截超链接的url,即拦截href请求的内容.
        mCaWvContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mSysTvTitle.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int progress) {
                mCaLoadProgress.setVisibility(View.VISIBLE);
                mCaLoadProgress.setProgress(progress * 100);
                if (progress == 100) {
                    mCaLoadProgress.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, progress);
            }

        });
        mCaWvContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http") || url.startsWith("https"))
                    view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!mCaWvContent.getSettings().getLoadsImagesAutomatically()) {
                    mCaWvContent.getSettings().setLoadsImagesAutomatically(true);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                System.out.println("onReceivedError   ");
                // 加载空页面
                mCaWvContent.loadDataWithBaseURL(null, "<html><body><div style=\"margin:0 auto;width:100%;text-align:center;margin-top:20px;\" ><h2>网络连接失败</h2><p>请稍后重试.</p></div></body></html>", "text/html", "utf-8", null);
            }
        });
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_add_collection:
                if (!TextUtils.isEmpty(contentUrl)) {
                    String dateTime = DateUtil.format(new Date(), Constant.DATE_LONG_PATTERN);
                    UserCollection collection = new UserCollection();
                    collection.setLink(contentUrl);
                    collection.setTime(dateTime);
                    collection.setTitle(title);
                    LiteOrmDBUtil.insert(collection);
                    T.ShowToast(ContentActivity.this, "收藏成功！");

                    String userId = SharedPreferencesUtil.getString(this, Constant.USER_ID, "");
                    mPresenter.add(title,contentUrl,  informationId, userId);
                } else {
                    T.ShowToast(this, "收藏失败，链接错误！");
                }
                break;
            case R.id.toolbar_add_share:
                Intent intent = new Intent(this, QrCodeActivity.class);
                intent.putExtra(QrCodeActivity.KEY_FROM, QrCodeActivity.FROM_VALUES[2]);
                intent.putExtra(QrCodeActivity.KEY_TITLE, title);
                intent.putExtra(QrCodeActivity.KEY_CONTENT, Base64Util.getEncodeStr(Constant.FLAG_URL_SOURCE + contentUrl));
                startActivity(intent);
                break;
            case R.id.toolbar_web_browser:
                //从其他浏览器打开
                Intent urlIntent = new Intent();
                urlIntent.setAction(Intent.ACTION_VIEW);
                Uri content_url = Uri.parse(contentUrl);
                urlIntent.setData(content_url);
                if (urlIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(urlIntent, "请选择浏览器"));
                }
                break;
        }
        return false;
    }

    @Override
    public void showAddSuccess(ResShareCollection resShareCollection) {
        T.ShowToast(this, resShareCollection.getRetMsg());
    }

    @Override
    public void showUpdateSuccess(ResBase resBase) {
        //TODO
    }

    @Override
    public void showUpdateFailed(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void showAddFailed(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void setPresenter(ContentContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showFail(Throwable throwable) {
        CommonHandler.actionThrowable(throwable);
    }
}
