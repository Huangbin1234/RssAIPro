package com.hb.rssai.view.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.presenter.BasePresenter;

import butterknife.BindView;

public class ContentActivity extends BaseActivity {

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
    private String contenUrl;
    private String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initIntent() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            contenUrl = bundle.getString(KEY_URL);
            title = bundle.getString(KEY_TITLE);
        }
    }

    @Override
    protected void initView() {
        mSysTvTitle.setText(title);
        // 运行加载JS
        mCaWvContent.getSettings().setJavaScriptEnabled(true);
        // 采用缓存模式 需要对应清除缓存操作
        if (isWIFIkAvailable()) {
            //当前有可用网络
            mCaWvContent.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式( 根据cache-control决定是否从网络上取数据。)
        } else {
            //当前没有可用网络
            mCaWvContent.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 缓存模式(只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。)
        }
//        msa_wv_content.getSettings().setCacheMode(
//                WebSettings.LOAD_CACHE_ELSE_NETWORK);

        // 自适应
        mCaWvContent.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mCaWvContent.getSettings().setLoadWithOverviewMode(true);
        // 设置本地编码
        mCaWvContent.getSettings().setDefaultTextEncodingName("utf-8");
        mCaWvContent.loadUrl(contenUrl);
        // 此处能拦截超链接的url,即拦截href请求的内容.
        mCaWvContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
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
                view.loadUrl(url);
                return true;
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
                mCaWvContent.loadDataWithBaseURL(
                        null,
                        "<html><body><div style=\"margin:0 auto;width:100%;text-align:center;\" ><h1>网络连接失败</h1><p>请稍后重试.</p></div></body></html>",
                        "text/html", "utf-8", null);
            }
        });
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
        actionBar.setDisplayHomeAsUpEnabled(true);//设置ActionBar一个返回箭头，主界面没有，次级界面有
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

    private boolean isWIFIkAvailable() {
        ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cwjManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }
}